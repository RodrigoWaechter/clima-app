package com.unisc.projeto.clima_app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.unisc.projeto.clima_app.util.ComponentFactory;

public class DashboardBuilder {

	private final DashboardFrm view;

	public DashboardBuilder(DashboardFrm view) {
		this.view = view;
	}

	public void build() {
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setTitle("Clima App - Dashboard");
		view.setMinimumSize(new Dimension(1200, 850));
		view.setJMenuBar(buildMenuBar());
		view.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(5, 0, 5, 0);

		// --- Linha 0: Header ---
		gbc.gridy = 0;
		mainPanel.add(buildHeaderPanel(), gbc);

		// --- Linha 1: Informações do Topo (
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0;
		mainPanel.add(buildTopInfoPanel(), gbc);

		// --- Linha 2: Previsão Horária ---
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipady = 180;
		mainPanel.add(buildHourlyForecastPanel(), gbc);

		// --- Linha 3: Cards de Resumo ---
		gbc.ipady = 0;
		gbc.gridy = 3;
		mainPanel.add(buildCardsPanel(), gbc);

		// --- Linha 4: Tabela de 7 dias ---
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		mainPanel.add(build7DayForecastPanel(), gbc);

		view.setContentPane(mainPanel);
		view.pack();
		view.setLocationRelativeTo(null);
	}

	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuArquivo = new JMenu("Arquivo");
		menuArquivo.add(view.getMenuItemDashboard());
		menuArquivo.add(view.getMenuItemSair());

		JMenu menuDados = new JMenu("Dados");
		menuDados.add(view.getMenuItemAtualizar());
		menuDados.add(view.getMenuItemHistorico());

		menuBar.add(menuArquivo);
		menuBar.add(menuDados);
		return menuBar;
	}

	private JPanel buildHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));

		JLabel lblTitle = ComponentFactory.createLabel("Dashboard", new Font("Segoe UI", Font.BOLD, 24));
		panel.add(lblTitle, BorderLayout.WEST);

		JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
		searchPanel.add(view.getCampoBusca(), BorderLayout.CENTER);
		searchPanel.add(view.getBtnBuscar(), BorderLayout.EAST);
		panel.add(searchPanel, BorderLayout.EAST);

		return panel;
	}

	private JPanel buildTopInfoPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;

		gbc.gridx = 0;
		gbc.weightx = 0.65;
		panel.add(buildCurrentConditionsPanel(), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.35;
		panel.add(buildLocationPanel(), gbc);

		return panel;
	}

	private JPanel buildCurrentConditionsPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createTitledBorder("Condições Atuais"));
		panel.add(view.getLblUltimaAtualizacao(), BorderLayout.SOUTH);

		JPanel topPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(5, 5, 5, 20);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 3;
		topPanel.add(view.getLblIconeCondicaoAtual(), gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.insets = new Insets(2, 2, 2, 2);
		topPanel.add(view.getLblTemperaturaAtual(), gbc);
		gbc.gridx = 2;
		topPanel.add(view.getLblVento(), gbc);
		gbc.gridy = 1;
		topPanel.add(view.getLblUmidade(), gbc);
		gbc.gridy = 2;
		topPanel.add(view.getLblPrecipitacao(), gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(8, 5, 5, 5);
		topPanel.add(view.getLblSensacaoTermica(), gbc);

		panel.add(topPanel, BorderLayout.CENTER);
		return panel;
	}

	private JScrollPane buildHourlyForecastPanel() {
		JPanel previsaoContainer = view.getPanelPrevisaoHoraria();

		previsaoContainer.setLayout(new GridBagLayout());
		previsaoContainer.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(previsaoContainer);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Previsão para hoje"));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		scrollPane.getViewport().setBackground(Color.WHITE);

		return scrollPane;
	}

	private JPanel buildLocationPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Localização Atual"));

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

		view.getLblLocalizacao().setAlignmentX(Component.LEFT_ALIGNMENT);
		infoPanel.add(view.getLblLocalizacao());
		infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		infoPanel.add(new JSeparator());
		infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		view.getLblLatitude().setAlignmentX(Component.LEFT_ALIGNMENT);
		infoPanel.add(view.getLblLatitude());
		infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		view.getLblLongitude().setAlignmentX(Component.LEFT_ALIGNMENT);
		infoPanel.add(view.getLblLongitude());
		infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		view.getLblAltitude().setAlignmentX(Component.LEFT_ALIGNMENT);
		infoPanel.add(view.getLblAltitude());
		infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		view.getLblFusoHorario().setAlignmentX(Component.LEFT_ALIGNMENT);
		infoPanel.add(view.getLblFusoHorario());

		panel.add(infoPanel, BorderLayout.NORTH);

		return panel;
	}

	private JPanel buildCardsPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Resumo do Dia"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		view.getCardTempMax().setPreferredSize(new Dimension(200, 120));
		view.getCardTempMin().setPreferredSize(new Dimension(200, 120));
		view.getCardVento().setPreferredSize(new Dimension(200, 120));
		view.getCardPrecipitacao().setPreferredSize(new Dimension(200, 120));

		gbc.gridx = 0;
		panel.add(view.getCardTempMax(), gbc);
		gbc.gridx = 1;
		panel.add(view.getCardTempMin(), gbc);
		gbc.gridx = 2;
		panel.add(view.getCardVento(), gbc);
		gbc.gridx = 3;
		panel.add(view.getCardPrecipitacao(), gbc);

		return panel;
	}

	private JPanel build7DayForecastPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Previsão para 7 dias"));

		JScrollPane scrollPane = new JScrollPane(view.getTabelaPrevisao());
		scrollPane.getViewport().setBackground(Color.WHITE);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}
}
