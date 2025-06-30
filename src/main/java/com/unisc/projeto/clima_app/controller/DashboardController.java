package com.unisc.projeto.clima_app.controller;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.unisc.projeto.clima_app.dao.LocalizacaoDAO;
import com.unisc.projeto.clima_app.dao.PreferenciaDAO;
import com.unisc.projeto.clima_app.domain.ClimaInfoDTO;
import com.unisc.projeto.clima_app.domain.DadoDiario;
import com.unisc.projeto.clima_app.domain.DadoHorario;
import com.unisc.projeto.clima_app.domain.Localizacao;
import com.unisc.projeto.clima_app.domain.Preferencia;
import com.unisc.projeto.clima_app.service.ClimaService;
import com.unisc.projeto.clima_app.util.IconUtils;
import com.unisc.projeto.clima_app.util.WeatherCodeUtil;
import com.unisc.projeto.clima_app.view.DashboardFrm;
import com.unisc.projeto.clima_app.view.ItemPrevisao;

public class DashboardController {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    private final DashboardFrm view;
    private final ClimaService climaService;
    private Localizacao localizacaoAtual;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DashboardController(DashboardFrm view) {
        this.view = view;
        this.climaService = new ClimaService();

        initListeners();
        actionCarregarDadosIniciais();
    }

    private void initListeners() {
        view.getBtnBuscar().addActionListener(e -> actionBuscarLocalizacao());
        view.getCampoBusca().addActionListener(e -> actionBuscarLocalizacao());
    }

    private void actionCarregarDadosIniciais() {
        String cidadeInicial = new PreferenciaDAO().findPreferencia()
                .map(Preferencia::getCidadePreferida)
                .orElseGet(() -> new LocalizacaoDAO().findLast()
                        .map(Localizacao::getNomeCidade)
                        .orElse("Santa Cruz do Sul"));
        
        actionBuscarDadosPorLocalizacao(cidadeInicial);
    }
    
    private void actionBuscarDadosPorLocalizacao(String nomeCidade) {
        setLoading(true); 
        
        //thread separada para não usar a mesma do swing na busca dos dados
        executor.submit(() -> {
            try {
                ClimaInfoDTO info = climaService.getDadosClimaticos(nomeCidade);
                this.localizacaoAtual = info.getLocalizacao();
                
                SwingUtilities.invokeLater(() -> atualizarView(info));

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro no fluxo de busca de dados para localização.", e);
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE));
            } finally {
                SwingUtilities.invokeLater(() -> setLoading(false));
            }
        });
    }
    
    public void actionBuscarLocalizacao() {
        String nomeCidade = view.getCampoBusca().getText();
        if (nomeCidade != null && !nomeCidade.trim().isEmpty()) {
            actionBuscarDadosPorLocalizacao(nomeCidade);
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, digite o nome de uma cidade.", "Campo Vazio", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void actionAtualizarDados() {
        if (localizacaoAtual != null) {
            actionBuscarDadosPorLocalizacao(localizacaoAtual.getNomeCidade());
        } else {
            JOptionPane.showMessageDialog(view, "Nenhuma localização selecionada para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    //so firula pra alterar o cursor e desabilitar os campos enquanto pesquisa.... 
    private void setLoading(Boolean loading) {
        view.setCursor(loading ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
        view.getBtnBuscar().setEnabled(!loading);
        view.getCampoBusca().setEnabled(!loading);
        if (loading) {
            view.getLblTemperaturaAtual().setText("...");
            ((DefaultTableModel) view.getTabelaPrevisao().getModel()).setRowCount(0);
            view.getPanelPrevisaoHoraria().removeAll();
            view.getPanelPrevisaoHoraria().repaint();
        }
    }

    //metodo principal pra atualizar a view que chama todos os demais
    private void atualizarView(ClimaInfoDTO info) {
        atualizarCondicoesAtuais(info.getDadoHorarioAtual());
        atualizarPainelLocalizacao(info.getLocalizacao());
        atualizarCardsResumo(info.getPrevisaoDiaria());
        atualizarTabelaPrevisao(info.getPrevisaoDiaria());
        atualizarPrevisaoHoraria(info.getPrevisaoHoraria());

        view.revalidate();
        view.repaint();
    }
    
    private void atualizarTabelaPrevisao(List<DadoDiario> previsaoDiaria) {
        DefaultTableModel model = (DefaultTableModel) view.getTabelaPrevisao().getModel();
        model.setRowCount(0);

        if (previsaoDiaria == null) return;

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E, dd/MM", new Locale("pt","BR"));

        for (DadoDiario dia : previsaoDiaria) {
            Object[] rowData = {
                    dia.getData().format(dayFormatter),
                    WeatherCodeUtil.getWeatherDescription(dia.getCdClima()),
                    String.format("%.0f°", dia.getTemperaturaMax()),
                    String.format("%.0f°", dia.getTemperaturaMin()),
                    String.format("%.1f mm", dia.getPrecipitacaoTotal()),
                    String.format("%.0f km/h", dia.getVelocidadeVentoMax())
            };
            model.addRow(rowData);
        }
    }
    
    private void atualizarCondicoesAtuais(DadoHorario dadoAtual) {
        Integer weatherCode = dadoAtual.getCdClima();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        view.getLblIconeCondicaoAtual().setIcon(IconUtils.carregarIconeRedimensionado(WeatherCodeUtil.getIconPath(weatherCode), 80, 80));
        view.getLblCondicaoAtualDescricao().setText(WeatherCodeUtil.getWeatherDescription(weatherCode));
        view.getLblTemperaturaAtual().setText(String.format("%.0f°C", dadoAtual.getTemperatura()));
        view.getLblSensacaoTermica().setText(String.format("Sensação térmica: %.0f°C", dadoAtual.getSensacaoTermica()));
        view.getLblUmidade().setText(String.format("Umidade: %.0f%%", dadoAtual.getUmidadeRelativa()));
        view.getLblVento().setText(String.format("Vento: %.1f km/h", dadoAtual.getVelocidadeVento()));
        view.getLblPrecipitacao().setText(String.format("Precipitação: %.1f mm", dadoAtual.getPrecipitacao()));
        view.getLblUltimaAtualizacao().setText("Última atualização: " + dadoAtual.getHorario().format(timeFormatter) + "h");
    }

    private void atualizarPainelLocalizacao(Localizacao localizacao) {
        view.getLblLocalizacao().setText(localizacao.getNomeCidade());
        view.getLblLatitude().setText(String.format("Latitude: %.4f", localizacao.getLatitude()));
        view.getLblLongitude().setText(String.format("Longitude: %.4f", localizacao.getLongitude()));
    }

    private void atualizarCardsResumo(List<DadoDiario> previsaoDiaria) {
        if (!previsaoDiaria.isEmpty()) {
            DadoDiario hoje = previsaoDiaria.get(0);
            view.getCardTempMax().updateContent(String.format("%.0f°C", hoje.getTemperaturaMax()), "Máx. hoje", IconUtils.carregarIconeRedimensionado(IconUtils.SUN, 48, 48));
            view.getCardTempMin().updateContent(String.format("%.0f°C", hoje.getTemperaturaMin()), "Mín. hoje", IconUtils.carregarIconeRedimensionado(IconUtils.CLOUD2, 48, 48));
            view.getCardVento().updateContent(String.format("%.0f km/h", hoje.getVelocidadeVentoMax()), "Vento máx.", IconUtils.carregarIconeRedimensionado(IconUtils.WIND, 48, 48));
            view.getCardPrecipitacao().updateContent(String.format("%.1f mm", hoje.getPrecipitacaoTotal()), "Chuva hoje", IconUtils.carregarIconeRedimensionado(IconUtils.RAINFALL, 48, 48));
        }
    }

    private void atualizarPrevisaoHoraria(List<DadoHorario> previsaoHoraria) {
        JPanel painel = view.getPanelPrevisaoHoraria();
        painel.removeAll();

        if (previsaoHoraria == null || previsaoHoraria.isEmpty()) {
            painel.revalidate();
            painel.repaint();
            return;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        
        Integer gridxCounter = 0;
        for (DadoHorario hora : previsaoHoraria) {
            String strHora = hora.getHorario().format(DateTimeFormatter.ofPattern("HH'h'"));
            String strTemp = String.format("%.0f°C", hora.getTemperatura());
            String iconPath = WeatherCodeUtil.getIconPath(hora.getCdClima());

            ItemPrevisao item = new ItemPrevisao(strHora, strTemp, IconUtils.carregarIconeRedimensionado(iconPath, 48, 48));
            
            gbc.gridx = gridxCounter++;
            painel.add(item, gbc);
        }
        
        gbc.gridx = gridxCounter;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(Box.createHorizontalGlue(), gbc);

        painel.revalidate();
        painel.repaint();
    }

    public Localizacao getLocalizacaoAtual() {
        return localizacaoAtual;
    }
}