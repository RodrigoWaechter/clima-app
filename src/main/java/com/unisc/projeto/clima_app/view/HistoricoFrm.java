package com.unisc.projeto.clima_app.view;

import java.awt.BorderLayout;
import java.awt.Font; // Importação necessária para a fonte
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants; // Importação para alinhamento
import javax.swing.table.DefaultTableCellRenderer; // Importação para renderização
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.unisc.projeto.clima_app.controller.HistoricoController;
import com.unisc.projeto.clima_app.util.ComponentFactory;

@SuppressWarnings("serial")
public class HistoricoFrm extends JPanel {

	private DatePicker datePickerInicio;
	private DatePicker datePickerFim;
	private JButton btnFiltrar;
	private JTable tabelaHistorico;

	public HistoricoFrm() {
		initComponents();
		initLayout();
		new HistoricoController(this);
	}

	private void initComponents() {
		datePickerInicio = ComponentFactory.createDatePicker(LocalDate.now());
		datePickerFim = ComponentFactory.createDatePicker(LocalDate.now());
		btnFiltrar = ComponentFactory.createButton("Filtrar");
		tabelaHistorico = ComponentFactory.createTable( new String[] { "Cidade", "Data", "Temp. Mín/Máx", "Precipitação (mm)", "Vento Máx (km/h)" });
	}

	private void initLayout() {
		this.setLayout(new BorderLayout());
		JPanel filterPanel = new JPanel();
		filterPanel.add(ComponentFactory.createLabel("De:"));
		filterPanel.add(datePickerInicio);
		filterPanel.add(ComponentFactory.createLabel("Até:"));
		filterPanel.add(datePickerFim);
		filterPanel.add(btnFiltrar);
		this.add(filterPanel, BorderLayout.NORTH);
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
}