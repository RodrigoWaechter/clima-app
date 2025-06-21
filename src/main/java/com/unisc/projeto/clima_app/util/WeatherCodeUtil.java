package com.unisc.projeto.clima_app.util;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherCodeUtil {
	
	// mapeia o codigo do clima da API para retornar a descricao e icones corretos pra cada
    private static final Map<Integer, String> WEATHER_CODES = Stream.of(new Object[][] {
        { 0, "Céu limpo" }, 
        { 1, "Principalmente limpo" },
        { 2, "Parcialmente nublado" },
        { 3, "Nublado" },
        { 45, "Nevoeiro" },
        { 48, "Nevoeiro com geada" },
        { 51, "Garoa leve" },
        { 53, "Garoa moderada" },
        { 55, "Garoa densa" },
        { 56, "Garoa gelada leve" },
        { 57, "Garoa gelada densa" },
        { 61, "Chuva fraca" },
        { 63, "Chuva moderada" },
        { 65, "Chuva forte" },
        { 66, "Chuva gelada leve" },
        { 67, "Chuva gelada forte" },
        { 71, "Queda de neve fraca" },
        { 73, "Queda de neve moderada" },
        { 75, "Queda de neve forte" },
        { 77, "Grãos de neve" },
        { 80, "Aguaceiros fracos" },
        { 81, "Aguaceiros moderados" },
        { 82, "Aguaceiros violentos" },
        { 85, "Aguaceiros de neve fracos" },
        { 86, "Aguaceiros de neve fortes" },
        { 95, "Trovoada" },
        { 96, "Trovoada com granizo fraco" },
        { 99, "Trovoada com granizo forte" }
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));

    public static String getWeatherDescription(int code) {
        return WEATHER_CODES.getOrDefault(code, "Desconhecido");
    }
    
    //TODO pegar cada icon pra cada clima, dos principais
    public static String getIconPath(int code) {
        if (code == 0 || code == 1) return IconUtils.SUNNY;
        if (code == 2) return IconUtils.CLOUD;
        if (code == 3) return IconUtils.CLOUD;
        if (code >= 45 && code <= 48) return IconUtils.CLOUD;
        if ((code >= 51 && code <= 67) || (code >= 80 && code <= 82)) return IconUtils.RAIN;
        if ((code >= 71 && code <= 77) || (code >= 85 && code <= 86)) return "/icons/snow.png";
        if (code >= 95) return "/icons/storm.png";
        return IconUtils.CLOUD; // Padrão
    }
}
