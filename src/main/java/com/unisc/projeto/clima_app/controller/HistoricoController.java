package com.unisc.projeto.clima_app.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.unisc.projeto.clima_app.dao.HistoricoDAO;
import com.unisc.projeto.clima_app.domain.DadoDiario;
import com.unisc.projeto.clima_app.view.HistoricoFrm;

public class HistoricoController {
    
    private final HistoricoFrm view;
    private final HistoricoDAO historicoDAO;
    private static final Logger LOGGER = Logger.getLogger(HistoricoController.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public HistoricoController(HistoricoFrm view) {
        this.view = view;
        this.historicoDAO = new HistoricoDAO();
        initListeners();
      //  carregaDados();
    }
    
    private void initListeners() {
        view.getBtnFiltrar().addActionListener(e -> filtrarDados());
    }

    public void carregaDados() {
    	List<DadoDiario> dados = historicoDAO.findDadosDiarios();
       // System.out.println("Quantidade da dados: " + dados.size());

        DefaultTableModel model = (DefaultTableModel) view.getTabelaHistorico().getModel();
        model.setRowCount(0);
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E, dd/MM", new Locale("pt","BR"));

        for (DadoDiario dadoDiario : dados) {
            Object[] rowData = {
                    dadoDiario.getLocalizacao().getNomeCidade(),
                    dadoDiario.getData().format(dayFormatter),
                    String.format("%.0f° / %.0f°", dadoDiario.getTemperaturaMin(), dadoDiario.getTemperaturaMax()),
                    String.format("%.1f mm", dadoDiario.getPrecipitacaoTotal()),
                    String.format("%.0f km/h", dadoDiario.getVelocidadeVentoMax())
            };
            model.addRow(rowData);
        }
    }

    public void filtrarDados() {
        LOGGER.log(Level.INFO, "Iniciando a filtragem de dados...");
        DatePicker pickerInicio = view.getDatePickerInicio();
        DatePicker pickerFim = view.getDatePickerFim();
        String nomeCidade = view.getTxtNomeCidade().getText().trim(); 

        LocalDate inicio = pickerInicio.getDate();
        LocalDate fim = pickerFim.getDate();

        List<DadoDiario> dadosDoBanco;

        if (inicio == null || fim == null) {
            if (nomeCidade.isEmpty()) {
                dadosDoBanco = historicoDAO.findDadosDiarios();
            } else {
                dadosDoBanco = historicoDAO.findDadosDiarios(nomeCidade);
            }
        } else {
            if (inicio.isAfter(fim)) {
                JOptionPane.showMessageDialog(null, "A data de início não pode ser posterior à data de fim.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nomeCidade.isEmpty()) {
                dadosDoBanco = historicoDAO.findDadosDiarios(inicio, fim);
            } else {
                dadosDoBanco = historicoDAO.findDadosDiarios(inicio, fim, nomeCidade); 
            }
        }

        LOGGER.log(Level.INFO, "Foram encontrados {0} registros no banco de dados.", dadosDoBanco.size());

        DefaultTableModel model = (DefaultTableModel) view.getTabelaHistorico().getModel();
        model.setRowCount(0);

        for (DadoDiario dado : dadosDoBanco) {
            Object[] linha = {
                    dado.getLocalizacao().getNomeCidade(),
                    dado.getData().format(DATE_FORMATTER),
                    String.format("%.1f°C / %.1f°C", dado.getTemperaturaMin(), dado.getTemperaturaMax()),
                    String.format("%.1f", dado.getPrecipitacaoTotal()),
                    String.format("%.1f", dado.getVelocidadeVentoMax())
            };
            model.addRow(linha);
        }
    }

}