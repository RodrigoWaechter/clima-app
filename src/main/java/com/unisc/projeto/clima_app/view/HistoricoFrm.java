package com.unisc.projeto.clima_app.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.github.lgooddatepicker.components.DatePicker;
import com.unisc.projeto.clima_app.controller.HistoricoController;
import com.unisc.projeto.clima_app.util.ComponentFactory;
import com.unisc.projeto.clima_app.util.IconUtils;

@SuppressWarnings("serial")
public class HistoricoFrm extends JPanel {

	private DatePicker datePickerInicio;
	private DatePicker datePickerFim;
	private JButton btnFiltrar;
	private JTable tabelaHistorico;
	private JTextField txtNomeCidade; 
	private HistoricoController controller;

	public HistoricoFrm() {
		initComponents();
		initLayout();
		controller = new HistoricoController(this);
	}

	private void initComponents() {
		datePickerInicio = ComponentFactory.createDatePicker(LocalDate.now());
		datePickerFim = ComponentFactory.createDatePicker(LocalDate.now());
		btnFiltrar = ComponentFactory.createButton(IconUtils.carregarIconeRedimensionado(IconUtils.SEARCH, 30, 30));
		tabelaHistorico = ComponentFactory.createTable( new String[] { "Cidade", "Data", "Temp. Mín/Máx", "Precipitação (mm)", "Vento Máx (km/h)" });
		txtNomeCidade = ComponentFactory.createTextField(25);
	}

	private void initLayout() {
		this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel headerPanel = new JPanel(new BorderLayout());
		JLabel lblTitle = ComponentFactory.createLabel("Histórico");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		headerPanel.add(lblTitle, BorderLayout.WEST);
		JPanel filterPanel = new JPanel();
		filterPanel.add(ComponentFactory.createLabel("Localização:"));
		filterPanel.add(txtNomeCidade); 
		filterPanel.add(ComponentFactory.createLabel("De:"));
		filterPanel.add(datePickerInicio);
		filterPanel.add(ComponentFactory.createLabel("Até:"));
		filterPanel.add(datePickerFim);
		filterPanel.add(btnFiltrar);
		headerPanel.add(filterPanel);
		this.add(headerPanel, BorderLayout.NORTH);
		this.add(new JScrollPane(tabelaHistorico), BorderLayout.CENTER);
	}

	// Getters
	public DatePicker getDatePickerInicio() {
		return datePickerInicio;
	}

	public DatePicker getDatePickerFim() {
		return datePickerFim;
	}

	public JButton getBtnFiltrar() {
		return btnFiltrar;
	}

	public JTable getTabelaHistorico() {
		return tabelaHistorico;
	}

	public HistoricoController getController() {
		return controller;
	}
	
	public JTextField getTxtNomeCidade() { 
		return txtNomeCidade;
	}
}