package com.unisc.projeto.clima_app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.unisc.projeto.clima_app.controller.PreferenciaController;
import com.unisc.projeto.clima_app.domain.Tema;
import com.unisc.projeto.clima_app.util.ComponentFactory;

@SuppressWarnings("serial")
public class PreferenciaFrm extends JPanel {

	private JTextField campoCidadePreferida;
	private JComboBox<Tema> comboTema;
	private JTextField campoApiGeo;
	private JTextField campoApiClima;
	private JButton btnSalvar;
	private PreferenciaController controller;

	public PreferenciaFrm() {
		initComponents();
		initLayout();
		controller = new PreferenciaController(this);
	}

	private void initComponents() {
		campoCidadePreferida = ComponentFactory.createTextField(20);
		campoApiGeo = ComponentFactory.createTextField(40);
		campoApiClima = ComponentFactory.createTextField(40);
		btnSalvar = ComponentFactory.createButton("Salvar e Aplicar");

		comboTema = ComponentFactory.createCombobox(Tema.values());

		campoApiGeo.setEditable(false);
		campoApiClima.setEditable(false);
	}

	private void initLayout() {
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel mainPanel = ComponentFactory.createPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		JPanel panelGeral = ComponentFactory.createPanel(new GridBagLayout());
		panelGeral.setBorder(new TitledBorder("Geral"));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		panelGeral.add(ComponentFactory.createLabel("Cidade Preferida:"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelGeral.add(campoCidadePreferida, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		panelGeral.add(ComponentFactory.createLabel("Tema do Aplicativo:"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelGeral.add(comboTema, gbc);

		JPanel panelApi = ComponentFactory.createPanel(new GridBagLayout());
		panelApi.setBorder(new TitledBorder("Visualização das URLs da API"));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		panelApi.add(ComponentFactory.createLabel("URL API Geocoding:"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelApi.add(campoApiGeo, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		panelApi.add(ComponentFactory.createLabel("URL API Clima:"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelApi.add(campoApiClima, gbc);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(panelGeral, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainPanel.add(panelApi, gbc);

		JPanel buttonPanel = ComponentFactory.createPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		buttonPanel.add(btnSalvar);

		this.add(mainPanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	public PreferenciaController getController() {
		return controller;
	}

	public JComboBox<Tema> getComboTema() {
		return comboTema;
	}

	public JTextField getCampoCidadePreferida() {
		return campoCidadePreferida;
	}

	public JTextField getCampoApiGeo() {
		return campoApiGeo;
	}

	public JTextField getCampoApiClima() {
		return campoApiClima;
	}

	public JButton getBtnSalvar() {
		return btnSalvar;
	}
}