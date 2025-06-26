package com.unisc.projeto.clima_app.controller;

import com.github.lgooddatepicker.components.DatePicker;
import com.unisc.projeto.clima_app.dao.HistoricoDAO;
import com.unisc.projeto.clima_app.domain.DadoDiario;
import com.unisc.projeto.clima_app.view.HistoricoFrm;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

public class HistoricoController {
    
    private final HistoricoFrm view;
    private final HistoricoDAO historicoDAO;
    private static final Logger LOGGER = Logger.getLogger(HistoricoController.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public HistoricoController(HistoricoFrm view) {
        this.view = view;
        this.historicoDAO = new HistoricoDAO();
        initListeners();
    }
    
    private void initListeners() {
        view.getBtnFiltrar().addActionListener(e -> filtrarDados());
    }
    
    public void filtrarDados() {
        LOGGER.log(Level.INFO, "Iniciando a filtragem de dados...");
        DatePicker pickerInicio = view.getDatePickerInicio();
        DatePicker pickerFim = view.getDatePickerFim();
        
        if (pickerInicio.getDate() == null || pickerFim.getDate() == null) {
            LOGGER.log(Level.WARNING, "Datas de início ou fim não selecionadas.");
            return;
        }
        
        LocalDate inicio = pickerInicio.getDate();
        LocalDate fim = pickerFim.getDate();
        
        if (inicio.isAfter(fim)) {
            LOGGER.log(Level.WARNING, "A data de início não pode ser posterior à data de fim.");
            return;
        }
        
        List<DadoDiario> dadosDoBanco = historicoDAO.findDadosDiariosFiltrados(null, inicio, fim);
        
        LOGGER.log(Level.INFO, "Foram encontrados {0} registros no banco de dados.", dadosDoBanco.size());
        
        DefaultTableModel model = (DefaultTableModel) view.getTabelaHistorico().getModel();
        model.setRowCount(0);
        
        // --- MUDANÇA: Preenchimento da linha da tabela com os novos dados ---
        for (DadoDiario dado : dadosDoBanco) {
            Object[] linha = {
                dado.getLocalizacao().getNomeCidade(), // Nova informação
                dado.getData().format(DATE_FORMATTER),
                String.format("%.1f°C / %.1f°C", dado.getTemperaturaMin(), dado.getTemperaturaMax()),
                String.format("%.1f", dado.getPrecipitacaoTotal()),
                String.format("%.1f", dado.getVelocidadeVentoMax()) // Nova informação
            };
            model.addRow(linha);
        }
        // -----------------------------------------------------------------
    }
}