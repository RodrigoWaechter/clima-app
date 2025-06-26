package com.unisc.projeto.clima_app.api;

import com.unisc.projeto.clima_app.domain.DadoDiario;
import com.unisc.projeto.clima_app.domain.DadoHorario;
import com.unisc.projeto.clima_app.domain.Localizacao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenMeteoAPI {
	private static final Logger LOGGER = Logger.getLogger(OpenMeteoAPI.class.getName());
	private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
	private static final String API_URL_FORMAT;

	// bloco estático porque só precisa carregar o config.properties 1 vez
	static {
		Properties props = new Properties();
		try (InputStream input = OpenMeteoAPI.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new IllegalStateException("Te liga!! Falta o config.properties no classpath.");
			}
			props.load(input);
			API_URL_FORMAT = props.getProperty("api.open-meteo.url");
			if (API_URL_FORMAT == null || API_URL_FORMAT.isBlank()) {
				throw new IllegalStateException(
						"A propriedade 'api.open-meteo.url' está com problemas no config.properties.");
			}
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Erro ao carregar config.properties", ex);
			throw new RuntimeException(ex);
		}
	}

	public Optional<DadoHorario> queryClimaAtualFromJSON(Localizacao localizacao) {
		Optional<JSONObject> jsonOpt = getApiConnection(localizacao);

		if (jsonOpt.isEmpty()) {
			return Optional.empty();
		}

		try {
			JSONObject current = jsonOpt.get().getJSONObject("current");
			DadoHorario dado = new DadoHorario();
			dado.setLocalizacao(localizacao);
			dado.setHorario(LocalDateTime.parse(current.getString("time"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			dado.setTemperatura(current.getDouble("temperature_2m"));
			dado.setUmidadeRelativa(current.getDouble("relative_humidity_2m"));
			dado.setSensacaoTermica(current.getDouble("apparent_temperature"));
			dado.setVelocidadeVento(current.getDouble("wind_speed_10m"));
			dado.setDirecaoVento((short) current.getInt("wind_direction_10m"));
			dado.setPrecipitacao(current.getDouble("precipitation"));
			dado.setCdClima(current.getInt("weather_code"));
			return Optional.of(dado);
		} catch (JSONException e) {
			LOGGER.log(Level.WARNING, "Erro ao extrair dados do clima atual do JSON.", e);
			return Optional.empty();
		}
	}

	public List<DadoDiario> queryPrevisaoDiariaFromJSON(Localizacao localizacao) {
		Optional<JSONObject> jsonOpt = getApiConnection(localizacao);
		List<DadoDiario> previsao = new ArrayList<>();

		if (jsonOpt.isEmpty()) {
			return previsao;
		}

		try {
			JSONObject daily = jsonOpt.get().getJSONObject("daily");
			JSONArray timeArray = daily.getJSONArray("time");
			for (int i = 0; i < timeArray.length(); i++) {
				DadoDiario dia = new DadoDiario();
				dia.setLocalizacao(localizacao);
				dia.setData(LocalDate.parse(timeArray.getString(i), DateTimeFormatter.ISO_LOCAL_DATE));
				dia.setTemperaturaMax(daily.getJSONArray("temperature_2m_max").getDouble(i));
				dia.setTemperaturaMin(daily.getJSONArray("temperature_2m_min").getDouble(i));
				dia.setPrecipitacaoTotal(daily.getJSONArray("precipitation_sum").getDouble(i));
				dia.setVelocidadeVentoMax(daily.getJSONArray("wind_speed_10m_max").getDouble(i));
				dia.setCdClima(daily.getJSONArray("weather_code").getInt(i));
				previsao.add(dia);
			}
		} catch (JSONException e) {
			LOGGER.log(Level.WARNING, "Erro ao extrair dados da previsão diária do JSON.", e);
		}

		return previsao;
	}

	public List<DadoHorario> queryPrevisaoHorariaFromJSON(Localizacao localizacao) {
		Optional<JSONObject> jsonOpt = getApiConnection(localizacao);
		List<DadoHorario> previsao = new ArrayList<>();

		if (jsonOpt.isEmpty()) {
			return previsao;
		}

		try {
			JSONObject hourly = jsonOpt.get().getJSONObject("hourly");
			JSONArray timeArray = hourly.getJSONArray("time");
			JSONArray tempArray = hourly.getJSONArray("temperature_2m");
			JSONArray humidityArray = hourly.getJSONArray("relative_humidity_2m");
			JSONArray apparentTempArray = hourly.getJSONArray("apparent_temperature");
			JSONArray windSpeedArray = hourly.getJSONArray("wind_speed_10m");
			JSONArray windDirArray = hourly.getJSONArray("wind_direction_10m");
			JSONArray precipArray = hourly.getJSONArray("precipitation");
			JSONArray codeArray = hourly.getJSONArray("weather_code");

			for (int i = 0; i < timeArray.length(); i++) {
				DadoHorario hora = new DadoHorario();
				hora.setLocalizacao(localizacao);

				hora.setHorario(LocalDateTime.parse(timeArray.getString(i), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				hora.setTemperatura(tempArray.getDouble(i));
				hora.setUmidadeRelativa(humidityArray.getDouble(i));
				hora.setSensacaoTermica(apparentTempArray.getDouble(i));
				hora.setVelocidadeVento(windSpeedArray.getDouble(i));
				hora.setDirecaoVento((short) windDirArray.getInt(i));
				hora.setPrecipitacao(precipArray.getDouble(i));
				hora.setCdClima(codeArray.getInt(i));

				previsao.add(hora);
			}
		} catch (JSONException e) {
			LOGGER.log(Level.WARNING, "Erro ao extrair dados da previsão por hora do JSON.", e);
		}

		return previsao;
	}

	//  retorna um JSON com os dados via http request da api, é usado nos outros metodos
	private Optional<JSONObject> getApiConnection(Localizacao localizacao) {
		String apiUrl = String.format(Locale.US, API_URL_FORMAT, localizacao.getLatitude(), localizacao.getLongitude());
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();
		try {
			HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				return Optional.of(new JSONObject(response.body()));
			} else {
				LOGGER.severe("Falha na chamada da API Open-Meteo. Status Code: " + response.statusCode());
			}
		} catch (IOException | InterruptedException | JSONException e) {
			LOGGER.log(Level.SEVERE, "Erro ao comunicar com a API Open-Meteo ou ao processar sua resposta.", e);
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
		}
		return Optional.empty();
	}
}