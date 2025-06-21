package com.unisc.projeto.clima_app.view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.unisc.projeto.clima_app.controller.DashboardController;
import com.unisc.projeto.clima_app.util.ComponentFactory;
import com.unisc.projeto.clima_app.util.IconUtils;

@SuppressWarnings("serial")
public class DashboardFrm extends JFrame {

	private DashboardController controller;
	private JButton btnBuscar;
	private JTextField campoBusca;
	private JMenuItem menuItemDashboard;
	private JMenuItem menuItemSair;
	private JMenuItem menuItemAtualizar;
	private JMenuItem menuItemHistorico;

	private JLabel lblIconeCondicaoAtual;
	private JLabel lblTemperaturaAtual;
	private JLabel lblCondicaoAtualDescricao;
	private JLabel lblSensacaoTermica;
	private JLabel lblUmidade;
	private JLabel lblVento;
	private JLabel lblPrecipitacao;
	private JLabel lblUltimaAtualizacao;

	private JLabel lblLocalizacao;
	private JLabel lblLatitude;
	private JLabel lblLongitude;
	private JLabel lblAltitude;
	private JLabel lblFusoHorario;

	private JPanel panelPrevisaoHoraria;

	private CardClima cardTempMax;
	private CardClima cardTempMin;
	private CardClima cardVento;
	private CardClima cardPrecipitacao;

	private JTable tabelaPrevisao;

	public DashboardFrm() {
		initComponents();
		initLayout();
		initListeners();
		controller = new DashboardController(this);
		controller.carregarDadosIniciais();
	}

	private void initLayout() {
		DashboardBuilder builder = new DashboardBuilder(this);
		builder.build();
	}

	private void initComponents() {
		btnBuscar = new JButton("Buscar");
		campoBusca = new JTextField(25);

		lblIconeCondicaoAtual = new JLabel();
		lblTemperaturaAtual = ComponentFactory.createLabel("...°C", new Font("Segoe UI", Font.BOLD, 48));
		lblCondicaoAtualDescricao = ComponentFactory.createDefaultLabel("...");
		lblSensacaoTermica = ComponentFactory.createDefaultLabel("Sensação térmica: ...");
		lblUmidade = ComponentFactory.createDefaultLabel("Umidade: ...");
		lblVento = ComponentFactory.createDefaultLabel("Vento: ...");
		lblPrecipitacao = ComponentFactory.createDefaultLabel("Precipitação: ...");
		lblUltimaAtualizacao = ComponentFactory.createLabel("Última atualização: ...",
				new Font("Segoe UI", Font.ITALIC, 11));

		lblLocalizacao = ComponentFactory.createLabel("...", new Font("Segoe UI", Font.BOLD, 18));
		lblLatitude = ComponentFactory.createDefaultLabel("Latitude: ...");
		lblLongitude = ComponentFactory.createDefaultLabel("Longitude: ...");
		lblAltitude = ComponentFactory.createDefaultLabel("Altitude: ...");
		lblFusoHorario = ComponentFactory.createDefaultLabel("Fuso Horário: ...");

		tabelaPrevisao = new JTable();
		tabelaPrevisao.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Dia", "Condição", "Máx.", "Mín.", "Chuva", "Vento" }));

		panelPrevisaoHoraria = new JPanel();

		// Inicializa os 4 cards de resumo
		ImageIcon iconePlaceholder = IconUtils.carregarIconeRedimensionado(IconUtils.CLOUD, 48, 48);
		cardTempMax = new CardClima("Temp. Máxima", "...", "...", iconePlaceholder);
		cardTempMin = new CardClima("Temp. Mínima", "...", "...", iconePlaceholder);
		cardVento = new CardClima("Vento", "...", "...", iconePlaceholder);
		cardPrecipitacao = new CardClima("Precipitação", "...", "...", iconePlaceholder);

		menuItemDashboard = new JMenuItem("Dashboard");
		menuItemSair = new JMenuItem("Sair");
		menuItemAtualizar = new JMenuItem("Atualizar dados");
		menuItemHistorico = new JMenuItem("Histórico");
	}

	private void initListeners() {
		btnBuscar.addActionListener(e -> controller.buscarLocalizacao());
		campoBusca.addActionListener(e -> controller.buscarLocalizacao());
		menuItemAtualizar.addActionListener(e -> controller.atualizarDados());

		// Listener para o ícone de atualização
		lblUltimaAtualizacao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.atualizarDados();
			}
		});

		menuItemSair.addActionListener(e -> System.exit(0));
	}

	public JButton getBtnBuscar() {
		return btnBuscar;
	}

	public JTextField getCampoBusca() {
		return campoBusca;
	}

	public JMenuItem getMenuItemDashboard() {
		return menuItemDashboard;
	}

	public JMenuItem getMenuItemSair() {
		return menuItemSair;
	}

	public JMenuItem getMenuItemAtualizar() {
		return menuItemAtualizar;
	}

	public JMenuItem getMenuItemHistorico() {
		return menuItemHistorico;
	}

	public JLabel getLblCondicaoAtualDescricao() {
		return lblCondicaoAtualDescricao;
	}

	public JLabel getLblTemperaturaAtual() {
		return lblTemperaturaAtual;
	}

	public JLabel getLblSensacaoTermica() {
		return lblSensacaoTermica;
	}

	public JLabel getLblUmidade() {
		return lblUmidade;
	}

	public JLabel getLblVento() {
		return lblVento;
	}

	public JLabel getLblPrecipitacao() {
		return lblPrecipitacao;
	}

	public JLabel getLblUltimaAtualizacao() {
		return lblUltimaAtualizacao;
	}

	public JLabel getLblLocalizacao() {
		return lblLocalizacao;
	}

	public JLabel getLblLatitude() {
		return lblLatitude;
	}

	public JLabel getLblLongitude() {
		return lblLongitude;
	}

	public JLabel getLblAltitude() {
		return lblAltitude;
	}

	public JLabel getLblFusoHorario() {
		return lblFusoHorario;
	}

	public JTable getTabelaPrevisao() {
		return tabelaPrevisao;
	}

	public JPanel getPanelPrevisaoHoraria() {
		return panelPrevisaoHoraria;
	}

	public JLabel getLblIconeCondicaoAtual() {
		return lblIconeCondicaoAtual;
	}

	public CardClima getCardTempMax() {
		return cardTempMax;
	}

	public CardClima getCardTempMin() {
		return cardTempMin;
	}

	public CardClima getCardVento() {
		return cardVento;
	}

	public CardClima getCardPrecipitacao() {
		return cardPrecipitacao;
	}
}
