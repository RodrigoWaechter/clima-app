package com.unisc.projeto.clima_app.controller;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.unisc.projeto.clima_app.dao.PreferenciaDAO;
import com.unisc.projeto.clima_app.domain.Preferencia;
import com.unisc.projeto.clima_app.domain.Tema;
import com.unisc.projeto.clima_app.view.PreferenciaFrm;

public class PreferenciaController {

	private final PreferenciaFrm view;
	private final PreferenciaDAO dao;
	private Preferencia preferenciaAtual;

	public PreferenciaController(PreferenciaFrm view) {
		this.view = view;
		this.dao = new PreferenciaDAO();
		initListeners();
		loadPreferencias();
	}

	private void initListeners() {
		view.getBtnSalvar().addActionListener(this::salvarPreferencias);
	}

	private void loadPreferencias() {
		dao.findPreferencia().ifPresent(pref -> {
			this.preferenciaAtual = pref;
			view.getCampoCidadePreferida().setText(pref.getCidadePreferida());
			view.getComboTema().setSelectedItem(Tema.fromDisplayName(pref.getTemaApp().getDisplayName()));
		});

		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			Properties props = new Properties();
			if (input == null) {
				view.getCampoApiGeo().setText("Erro: config.properties não encontrado.");
				view.getCampoApiClima().setText("Erro: config.properties não encontrado.");
				return;
			}
			props.load(input);
			view.getCampoApiGeo().setText(props.getProperty("api.geocoding.url", ""));
			view.getCampoApiClima().setText(props.getProperty("api.open-meteo.url", ""));
		} catch (IOException ex) {
			view.getCampoApiGeo().setText("Erro ao ler arquivo de configurações.");
			view.getCampoApiClima().setText("Erro ao ler arquivo de configurações.");
		}
	}

	public static void inicializarTemaAplicacao() {
		Tema temaSalvo = new PreferenciaDAO().findPreferencia().map(Preferencia::getTemaApp).orElse(Tema.CLARO);

		String className;
		if (temaSalvo == Tema.SISTEMA) {
			className = UIManager.getSystemLookAndFeelClassName();
		} else {
			className = temaSalvo.getClassName();
		}

		try {
			UIManager.setLookAndFeel(className);
		} catch (Exception ex) {
			System.err.println("Falha ao inicializar o Look and Feel: " + ex.getMessage());
		}
	}

	private void salvarPreferencias(ActionEvent e) {
		String cidade = view.getCampoCidadePreferida().getText().trim();
		Tema temaSelecionado = (Tema) view.getComboTema().getSelectedItem();

		if (cidade.isEmpty()) {
			JOptionPane.showMessageDialog(view, "O campo da cidade preferida não pode estar vazio.",
					"Erro de Validação", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (preferenciaAtual == null) {
			preferenciaAtual = new Preferencia();
		}
		preferenciaAtual.setCidadePreferida(cidade);
		preferenciaAtual.setTemaApp(temaSelecionado);

		try {
			dao.saveOrUpdate(preferenciaAtual);
			aplicarTema(temaSelecionado);
			JOptionPane.showMessageDialog(view, "Preferências salvas com sucesso!", "Sucesso",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view, "Erro ao salvar ou aplicar preferências: " + ex.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void aplicarTema(Tema tema) throws Exception {
		String className;
		if (tema == Tema.SISTEMA) {
			className = UIManager.getSystemLookAndFeelClassName();
		} else {
			className = tema.getClassName();
		}

		UIManager.setLookAndFeel(className);
		for (Window window : Window.getWindows()) {
			SwingUtilities.updateComponentTreeUI(window);
		}
	}

}