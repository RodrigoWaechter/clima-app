package com.unisc.projeto.clima_app.util;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public final class IconUtils {
	public static final String SEARCH = "/icons/search.png";
	public static final String HUMIDITY = "/icons/humidity.png";
	public static final String WIND = "/icons/wind.png";
	public static final String RAINFALL = "/icons/rainfall.png";
	public static final String CLOUD = "/icons/cloud.png";
	public static final String SUNNY = "/icons/sunny.png";
	public static final String LOC = "/icons/loc.png";
	public static final String CHUVA = "/icons/chuva.png";
	public static final String GOTA = "/icons/gita.png";
	public static final String SYNC = "/icons/sync.png";
	public static final String VENTO = "/icons/vento.png";
	public static final String SUN = "/icons/sun.png";
	public static final String SNOWING = "/icons/snowing.png";
	public static final String STORM = "/icons/storm.png";
	public static final String CLOUDS = "/icons/cloud.png";
	public static final String CLOUD2 = "/icons/cloud2.png";
	public static final String PARTLY_CLOUDY = "/icons/partly_cloudy.png";
	public static final String CLOUDY = "/icons/cloudy.png";
	public static final String FOG = "/icons/fog.png";
	public static final String DRIZZLE = "/icons/drizzle.png";
	public static final String RAIN = "/icons/rain.png";

	private IconUtils() {
	}

	public static ImageIcon carregarIconeRedimensionado(String caminho) {
		return carregarIconeRedimensionado(caminho, 40, 40);
	}

	public static ImageIcon carregarIconeRedimensionado(String caminho, int largura, int altura) {
		URL resource = IconUtils.class.getResource(caminho);
		if (resource == null) {
			System.err.println("Ícone não encontrado: " + caminho);
			return new ImageIcon(new byte[0]);
		}
		try {
			ImageIcon icon = new ImageIcon(resource);
			Image imagem = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
			return new ImageIcon(imagem);
		} catch (Exception e) {
			System.err.println("Erro ao carregar ícone: " + caminho);
			return new ImageIcon(new byte[0]);
		}
	}
}
