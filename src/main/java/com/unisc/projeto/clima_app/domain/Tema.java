package com.unisc.projeto.clima_app.domain;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import com.jtattoo.plaf.mint.MintLookAndFeel;

public enum Tema {
	CLARO("Claro (FlatLaf Light)", FlatLightLaf.class.getName()),
	ESCURO("Escuro (FlatLaf Dark)", FlatDarkLaf.class.getName()),
	INTELLIJ("IntelliJ (Claro)", FlatIntelliJLaf.class.getName()),
	DARCULA("Darcula (Escuro)", FlatDarculaLaf.class.getName()),
	MAC_LIGHT("macOS (Claro)", FlatMacLightLaf.class.getName()),
	MAC_DARK("macOS (Escuro)", FlatMacDarkLaf.class.getName()),
	SISTEMA("Padrão do Sistema", null),
	JTATTOO_ACRYL("JTattoo (Acrílico)", AcrylLookAndFeel.class.getName()),
	JTATTOO_ALUMINIUM("JTattoo (Alumínio)", AluminiumLookAndFeel.class.getName()),
	JTATTOO_GRAPHITE("JTattoo (Grafite)", GraphiteLookAndFeel.class.getName()),
	JTATTOO_MINT("JTattoo (Menta)", MintLookAndFeel.class.getName());

	private final String displayName;
	private final String className;

	Tema(String displayName, String className) {
		this.displayName = displayName;
		this.className = className;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public String toString() {
		return this.displayName;
	}

	public static Tema fromDisplayName(String displayName) {
		for (Tema tema : Tema.values()) {
			if (tema.getDisplayName().equals(displayName)) {
				return tema;
			}
		}
		return CLARO;
	}
}