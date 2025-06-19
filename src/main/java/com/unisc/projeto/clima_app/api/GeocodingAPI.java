package com.unisc.projeto.clima_app.api;

import com.unisc.projeto.clima_app.domain.Localizacao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe para interagir com a API de Geocoding da Open-Meteo.
 * Transforma um nome de cidade em um objeto Localizacao com coordenadas.
 */
public class GeocodingAPI {

    private static final Logger LOGGER = Logger.getLogger(GeocodingAPI.class.getName());
    // ATUALIZADO: URL para buscar múltiplas sugestões para o autocomplete
    private static final String API_SEARCH_URL = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=5&language=pt&format=json";

    /**
     * NOVO MÉTODO: Busca uma lista de cidades para o recurso de autocompletar.
     * Este é o método que o DashboardController usa para as sugestões.
     * @param query O texto parcial digitado pelo usuário.
     * @return Uma lista de objetos Localizacao correspondentes à busca.
     */
    public List<Localizacao> searchCitiesByName(String query) {
        List<Localizacao> suggestions = new ArrayList<>();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String apiUrl = String.format(API_SEARCH_URL, encodedQuery);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                if (jsonResponse.has("results")) {
                    JSONArray results = jsonResponse.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        Localizacao loc = new Localizacao();
                        loc.setNomeCidade(result.getString("name"));
                        loc.setLatitude(result.getDouble("latitude"));
                        loc.setLongitude(result.getDouble("longitude"));
                        suggestions.add(loc);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Erro de comunicação ao buscar sugestões de cidades.", e);
            Thread.currentThread().interrupt();
        } catch (JSONException e) {
            LOGGER.log(Level.SEVERE, "Erro ao processar JSON de sugestões de cidades.", e);
        }
        return suggestions;
    }

    /**
     * Busca as coordenadas para um nome de cidade específico, retornando apenas o melhor resultado.
     * @param nomeCidade O nome completo da cidade.
     * @return Um Optional contendo a Localizacao se encontrada.
     */
    public Optional<Localizacao> findCoordinatesByCityName(String nomeCidade) {
        // Reutiliza o método de busca e pega apenas o primeiro resultado, se houver.
        List<Localizacao> results = searchCitiesByName(nomeCidade);
        if (!results.isEmpty()) {
            Localizacao firstResult = results.get(0);
            firstResult.setDataHoraRegistro(LocalDateTime.now());
            return Optional.of(firstResult);
        }
        return Optional.empty();
    }
}
