package com.unisc.projeto.clima_app.database;

public final class ConfigManager {

	private static final String API_GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json";
	private static final String API_OPEN_METEO_URL = "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m,wind_direction_10m&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,wind_speed_10m_max&timezone=auto";

	private static final String DB_SERVER_URL = "jdbc:mysql://localhost:3306/";
	private static final String DB_NAME = "clima_app";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	private ConfigManager() {
	}

	public static String getApiGeocodingUrl() {
		return API_GEOCODING_URL;
	}

	public static String getApiOpenMeteoUrl() {
		return API_OPEN_METEO_URL;
	}

	public static String getDbUrl() {
		return DB_SERVER_URL + DB_NAME;
	}

	public static String getDbServerUrl() {
		return DB_SERVER_URL;
	}

	public static String getDbName() {
		return DB_NAME;
	}

	public static String getDbUser() {
		return DB_USER;
	}

	public static String getDbPassword() {
		return DB_PASSWORD;
	}
}