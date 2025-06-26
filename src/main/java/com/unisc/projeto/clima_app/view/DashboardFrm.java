package com.unisc.projeto.clima_app.view;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.unisc.projeto.clima_app.controller.DashboardController;
import com.unisc.projeto.clima_app.util.ComponentFactory;
import com.unisc.projeto.clima_app.util.IconUtils;

@SuppressWarnings("serial")
public class DashboardFrm extends JPanel {

	private JButton btnBuscar;
	private JTextField campoBusca;
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
	private DashboardController controller;

	public DashboardFrm() {
		initComponents();
		initLayout();
		controller = new DashboardController(this);
	}

	private void initComponents() {
		btnBuscar = ComponentFactory.createButton("Buscar");
		campoBusca = ComponentFactory.createTextField(25);
		lblIconeCondicaoAtual = ComponentFactory.createLabel("...");
		lblTemperaturaAtual = ComponentFactory.createLabel("...°C", new Font("Segoe UI", Font.BOLD, 48));
		lblCondicaoAtualDescricao = ComponentFactory.createLabel("...");
		lblSensacaoTermica = ComponentFactory.createLabel("Sensação térmica: ...");
		lblUmidade = ComponentFactory.createLabel("Umidade: ...");
		lblVento = ComponentFactory.createLabel("Vento: ...");
		lblPrecipitacao = ComponentFactory.createLabel("Precipitação: ...");
		lblUltimaAtualizacao = ComponentFactory.createLabel("Última atualização: ...", new Font("Segoe UI", Font.ITALIC, 11));
		lblLocalizacao = ComponentFactory.createLabel("...", new Font("Segoe UI", Font.BOLD, 18));
		lblLatitude = ComponentFactory.createLabel("Latitude: ...");
		lblLongitude = ComponentFactory.createLabel("Longitude: ...");
		lblAltitude = ComponentFactory.createLabel("Altitude: ...");
		lblFusoHorario = ComponentFactory.createLabel("Fuso Horário: ...");
		tabelaPrevisao = ComponentFactory.createTable(new String[] { "Dia", "Condição", "Temp. Máx.", "Temp. Mín.", "Chuva", "Vento" });
		panelPrevisaoHoraria = ComponentFactory.createPanel();
		
		ImageIcon iconePlaceholder = IconUtils.carregarIconeRedimensionado(IconUtils.CLOUD, 48, 48);
		cardTempMax = new CardClima("Temp. Máxima", "...", "...", iconePlaceholder);
		cardTempMin = new CardClima("Temp. Mínima", "...", "...", iconePlaceholder);
		cardVento = new CardClima("Vento", "...", "...", iconePlaceholder);
		cardPrecipitacao = new CardClima("Precipitação", "...", "...", iconePlaceholder);
	}
	
	private void initLayout() {
		DashboardBuilder builder = new DashboardBuilder(this);
		builder.build();
	}
	
	public JButton getBtnBuscar() {
		return btnBuscar;
	}

	public JTextField getCampoBusca() {
		return campoBusca;
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

	public DashboardController getController() {
		return controller;
	}
}