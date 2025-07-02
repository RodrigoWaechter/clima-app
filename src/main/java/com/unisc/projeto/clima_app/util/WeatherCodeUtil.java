package com.unisc.projeto.clima_app.util;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherCodeUtil {
	
	// Mapeia o código do clima da API para retornar a descrição e ícones corretos para cada
    private static final Map<Integer, String> WEATHER_CODES = Stream.of(new Object[][] {
        { 0, "Céu limpo" },
        { 1, "Principalmente limpo" },
        { 2, "Parcialmente nublado" },
        { 3, "Nublado" },
        { 45, "Nevoeiro" },
        { 48, "Nevoeiro depositando cristais de gelo" },
        { 51, "Garoa leve" },
        { 53, "Garoa moderada" },
        { 55, "Garoa forte" },
        { 56, "Garoa congelante leve" },
        { 57, "Garoa congelante forte" },
        { 61, "Chuva leve" },
        { 63, "Chuva moderada" },
        { 65, "Chuva forte" },
        { 66, "Chuva congelante leve" },
        { 67, "Chuva congelante forte" },
        { 71, "Neve leve" },
        { 73, "Neve moderada" },
        { 75, "Neve forte" },
        { 77, "Grãos de neve" },
        { 80, "Pancadas de chuva leve" },
        { 81, "Pancadas de chuva moderada" },
        { 82, "Pancadas de chuva violenta" },
        { 85, "Pancadas de neve leve" },
        { 86, "Pancadas de neve forte" },
        { 95, "Trovoada" },
        { 96, "Trovoada com granizo" },
        { 99, "Trovoada com granizo forte" }
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));

    public static String getWeatherDescription(int code) {
        return WEATHER_CODES.getOrDefault(code, "Desconhecido");
    }
    
    public static String getIcon(int code) {
        if (code == 0 || code == 1) return IconUtils.SUNNY;
        if (code == 2) return IconUtils.PARTLY_CLOUDY;
        if (code == 3) return IconUtils.CLOUD2;
        if (code >= 45 && code <= 48) return IconUtils.FOG;
        if (code >= 51 && code <= 57) return IconUtils.DRIZZLE;    
        if (code >= 61 && code <= 67) return IconUtils.RAIN;
        if (code >= 80 && code <= 82) return IconUtils.RAINFALL;
        if ((code >= 71 && code <= 77) || (code >= 85 && code <= 86)) return IconUtils.SNOWING;
        if (code >= 95 && code <= 99) return IconUtils.STORM; // Agrupado para todas as trovoadas
        return IconUtils.PARTLY_CLOUDY; // Padrão
    }
}