package com.unisc.projeto.clima_app.api;

import com.unisc.projeto.clima_app.domain.DadoHorario;
import com.unisc.projeto.clima_app.domain.Localizacao;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável por comunicar com a API Open-Meteo
 * para buscar dados climáticos atuais.
 */
public class OpenMeteoAPI {

    private static final Logger LOGGER = Logger.getLogger(OpenMeteoAPI.class.getName());
    private static final String API_URL_FORMAT = "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,wind_speed_10m,wind_direction_10m";

    public Optional<DadoHorario> fetchCurrentWeather(Localizacao localizacao) {
        // CORREÇÃO CRÍTICA: Usa Locale.US para garantir que o separador decimal seja sempre
        // um PONTO (.), independentemente da configuração do sistema operativo.
        String apiUrl = String.format(Locale.US, API_URL_FORMAT, localizacao.getLatitude(), localizacao.getLongitude());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONObject currentData = jsonResponse.getJSONObject("current");

                DadoHorario dado = new DadoHorario();
                dado.setLocalizacao(localizacao);
                dado.setHorario(LocalDateTime.parse(currentData.getString("time"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                dado.setTemperatura(currentData.getDouble("temperature_2m"));
                dado.setUmidadeRelativa(currentData.getDouble("relative_humidity_2m"));
                dado.setSensacaoTermica(currentData.getDouble("apparent_temperature"));
                dado.setVelocidadeVento(currentData.getDouble("wind_speed_10m"));
                dado.setDirecaoVento((short) currentData.getInt("wind_direction_10m"));
                dado.setPrecipitacao(currentData.getDouble("precipitation"));
                
                return Optional.of(dado);
            } else {
                LOGGER.log(Level.SEVERE, "Falha na API Open-Meteo. Status: " + response.statusCode() + " | URL: " + apiUrl);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao comunicar ou processar resposta da API Open-Meteo.", e);
            if (e instanceof InterruptedException) {
                 Thread.currentThread().interrupt();
            }
        }
        
        return Optional.empty();
    }
}