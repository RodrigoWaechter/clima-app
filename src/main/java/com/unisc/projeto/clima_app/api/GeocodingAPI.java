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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GeocodingAPI {
    private static final Logger LOGGER = Logger.getLogger(GeocodingAPI.class.getName());
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String API_SEARCH_URL = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json";

    public Optional<Localizacao> queryLocalizacaoPorNome(String nomeCidade) {
        List<Localizacao> resultados = queryLocalizacoesFromJSON(nomeCidade);

        if (resultados.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(resultados.get(0));
        }
    }

    //conecta com a api via http request, pega os dados via JSON e cria uma lista de localizacao com os dados
    private List<Localizacao> queryLocalizacoesFromJSON(String nomeDaCidade) {
        List<Localizacao> localizacoesEncontradas = new ArrayList<>();

        try {
            String encodedQuery = URLEncoder.encode(nomeDaCidade, StandardCharsets.UTF_8);
            String apiUrl = String.format(API_SEARCH_URL, encodedQuery);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

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
                        localizacoesEncontradas.add(loc);
                    }
                }
            } else {
                LOGGER.severe("Falha na chamada da API de Geocoding. Status Code: " + response.statusCode());
            }

        } catch (IOException | InterruptedException | JSONException e) {
            LOGGER.log(Level.SEVERE, "Erro ao comunicar com a API Geocoding ou ao processar sua resposta.", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }

        return localizacoesEncontradas;
    }
}