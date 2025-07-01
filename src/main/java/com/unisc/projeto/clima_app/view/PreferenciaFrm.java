package com.unisc.projeto.clima_app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

		// header/basicamente so tem o titulo
		JPanel headerPanel = ComponentFactory.createPanel(new BorderLayout());
		headerPanel.setBorder(new EmptyBorder(0, 5, 10, 5));
		JLabel lblTitle = new JLabel("Preferências");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		headerPanel.add(lblTitle, BorderLayout.WEST);
		
		
		JPanel contentPanel = ComponentFactory.createPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.NORTHWEST; 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;

		// preferencias gerais/ basicamente a cidade e o tema
		JPanel panelGeral = ComponentFactory.createPanel(new GridBagLayout());
		panelGeral.setBorder(new TitledBorder("Geral"));
		
		GridBagConstraints gbcGeral = new GridBagConstraints();
		gbcGeral.insets = new Insets(5, 5, 5, 5);
		gbcGeral.anchor = GridBagConstraints.WEST;

		gbcGeral.gridx = 0;
		gbcGeral.gridy = 0;
		gbcGeral.weightx = 0;
		gbcGeral.fill = GridBagConstraints.NONE;
		panelGeral.add(ComponentFactory.createLabel("Cidade Preferida:"), gbcGeral);
		
		gbcGeral.gridx = 1;
		gbcGeral.weightx = 1;
		gbcGeral.fill = GridBagConstraints.HORIZONTAL;
		panelGeral.add(campoCidadePreferida, gbcGeral);

		gbcGeral.gridx = 0;
		gbcGeral.gridy = 1;
		gbcGeral.weightx = 0;
		gbcGeral.fill = GridBagConstraints.NONE;
		panelGeral.add(ComponentFactory.createLabel("Tema do Aplicativo:"), gbcGeral);
		
		gbcGeral.gridx = 1;
		gbcGeral.weightx = 1;
		gbcGeral.fill = GridBagConstraints.HORIZONTAL;
		panelGeral.add(comboTema, gbcGeral);

		// URLs das APIs
		JPanel panelApi = ComponentFactory.createPanel(new GridBagLayout());
		panelApi.setBorder(new TitledBorder("Visualização das URLs da API"));
		
		GridBagConstraints gbcApi = new GridBagConstraints();
		gbcApi.insets = new Insets(5, 5, 5, 5);
		gbcApi.anchor = GridBagConstraints.WEST;

		gbcApi.gridx = 0;
		gbcApi.gridy = 0;
		gbcApi.weightx = 0;
		gbcApi.fill = GridBagConstraints.NONE;
		panelApi.add(ComponentFactory.createLabel("URL API Geocoding:"), gbcApi);
		
		gbcApi.gridx = 1;
		gbcApi.weightx = 1;
		gbcApi.fill = GridBagConstraints.HORIZONTAL;
		panelApi.add(campoApiGeo, gbcApi);

		gbcApi.gridx = 0;
		gbcApi.gridy = 1;
		gbcApi.weightx = 0;
		gbcApi.fill = GridBagConstraints.NONE;
		panelApi.add(ComponentFactory.createLabel("URL API Clima:"), gbcApi);
		
		gbcApi.gridx = 1;
		gbcApi.weightx = 1;
		gbcApi.fill = GridBagConstraints.HORIZONTAL;
		panelApi.add(campoApiClima, gbcApi);
		
		gbc.gridy = 0;
		contentPanel.add(panelGeral, gbc);
		gbc.gridy = 1;
		contentPanel.add(panelApi, gbc);

		
		gbc.gridy = 2;
		gbc.weighty = 1.0;
		contentPanel.add(Box.createVerticalGlue(), gbc);

		JPanel buttonPanel = ComponentFactory.createPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		buttonPanel.add(btnSalvar);

		this.add(headerPanel, BorderLayout.NORTH);
		this.add(contentPanel, BorderLayout.CENTER);
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