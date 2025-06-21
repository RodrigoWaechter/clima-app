package com.unisc.projeto.clima_app.controller;

import java.awt.Cursor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.unisc.projeto.clima_app.api.GeocodingAPI;
import com.unisc.projeto.clima_app.api.OpenMeteoAPI;
import com.unisc.projeto.clima_app.dao.DadoDiarioDAO;
import com.unisc.projeto.clima_app.dao.DadoHorarioDAO;
import com.unisc.projeto.clima_app.dao.LocalizacaoDAO;
import com.unisc.projeto.clima_app.domain.DadoDiario;
import com.unisc.projeto.clima_app.domain.DadoHorario;
import com.unisc.projeto.clima_app.domain.Localizacao;
import com.unisc.projeto.clima_app.util.IconUtils;
import com.unisc.projeto.clima_app.util.WeatherCodeUtil;
import com.unisc.projeto.clima_app.view.DashboardFrm;
import com.unisc.projeto.clima_app.view.ItemPrevisao;

public class DashboardController {

    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    private final DashboardFrm view;
    private final LocalizacaoDAO localizacaoDAO;
    private final DadoHorarioDAO dadoHorarioDAO;
    private final DadoDiarioDAO dadoDiarioDAO;
    private final GeocodingAPI geocodingAPI;
    private final OpenMeteoAPI openMeteoAPI;

    private Localizacao localizacaoAtual;
    private DadoHorario dadoHorarioAtual;
    private List<DadoDiario> previsaoDiaria;
    private List<DadoHorario> previsaoHoraria;

    public DashboardController(DashboardFrm view) {
        this.view = view;
        this.previsaoDiaria = new ArrayList<>();
        this.previsaoHoraria = new ArrayList<>();
        this.localizacaoDAO = new LocalizacaoDAO();
        this.dadoHorarioDAO = new DadoHorarioDAO();
        this.dadoDiarioDAO = new DadoDiarioDAO();
        this.geocodingAPI = new GeocodingAPI();
        this.openMeteoAPI = new OpenMeteoAPI();
    }

    public void carregarDadosIniciais() {
        Optional<Localizacao> ultimaLocalizacao = localizacaoDAO.findLast();
        String cidadeInicial = ultimaLocalizacao.map(Localizacao::getNomeCidade).orElse("Santa Cruz do Sul, RS");
        buscarDadosParaLocalizacao(cidadeInicial);
    }

    public void buscarLocalizacao() {
        String nomeCidade = view.getCampoBusca().getText();
        if (nomeCidade != null && !nomeCidade.trim().isEmpty()) {
            buscarDadosParaLocalizacao(nomeCidade);
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, digite o nome de uma cidade.", "Campo Vazio", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void atualizarDados() {
        if (localizacaoAtual != null) {
            buscarDadosParaLocalizacao(localizacaoAtual.getNomeCidade());
        } else {
            JOptionPane.showMessageDialog(view, "Nenhuma localização selecionada para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarDadosParaLocalizacao(String nomeCidade) {
        setLoading(true);

        new Thread(() -> {
            try {
                Optional<Localizacao> locOpt = localizacaoDAO.findByName(nomeCidade);
                if (locOpt.isEmpty()) {
                    LOGGER.info("Localização não encontrada no banco, buscando na API Geocoding...");
                    locOpt = geocodingAPI.queryLocalizacaoPorNome(nomeCidade);
                    locOpt.ifPresent(localizacaoDAO::save);
                }
                this.localizacaoAtual = locOpt.orElseThrow(() -> new RuntimeException("Cidade não encontrada: " + nomeCidade));

                Optional<DadoHorario> dadoHorarioApi_Atual = openMeteoAPI.queryClimaAtualFromJSON(localizacaoAtual);
                List<DadoDiario> previsaoDiariaApi = openMeteoAPI.queryPrevisaoDiariaFromJSON(localizacaoAtual);
                List<DadoHorario> previsaoHorariaCompletaApi = openMeteoAPI.queryPrevisaoHorariaFromJSON(localizacaoAtual);

                final LocalDate hoje = LocalDate.now();
                final LocalDate amanha = hoje.plusDays(1);
                final Integer horaAtual = LocalDateTime.now().getHour();

                List<DadoHorario> previsaoFiltrada = previsaoHorariaCompletaApi.stream().filter(p -> {
                    LocalDate dataPrevisao = p.getHorario().toLocalDate();
                    if (dataPrevisao.isEqual(hoje)) {
                        return true;
                    }
                
                    return dataPrevisao.isEqual(amanha) && p.getHorario().getHour() <= horaAtual;
                }).toList();

                if (dadoHorarioApi_Atual.isPresent() && !previsaoDiariaApi.isEmpty()) {
                    this.dadoHorarioAtual = dadoHorarioApi_Atual.get();
                    this.previsaoDiaria = previsaoDiariaApi;
                    this.previsaoHoraria = previsaoFiltrada;
                    salvarPrevisoesDaApiNoBanco(previsaoDiariaApi, previsaoFiltrada);
                } else {
                    LOGGER.warning("API falhou ou não retornou dados. Carregando dados do banco.");
                    this.dadoHorarioAtual = dadoHorarioDAO.queryHoraAutal(localizacaoAtual.getIdLocalizacao()).orElse(null);
                    this.previsaoDiaria = dadoDiarioDAO.queryProximos7Dias(localizacaoAtual.getIdLocalizacao());
                    this.previsaoHoraria = dadoHorarioDAO.queryProximas24Horas(localizacaoAtual.getIdLocalizacao());
                }

                if (dadoHorarioAtual == null) {
                    throw new Exception("Não foi possível obter os dados de clima para " + nomeCidade);
                }
                SwingUtilities.invokeLater(this::atualizarView);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro fatal no fluxo de busca de dados para localização.", e);
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE));
            } finally {
                SwingUtilities.invokeLater(() -> setLoading(false));
            }
        }).start();
    }

    private void salvarPrevisoesDaApiNoBanco(List<DadoDiario> previsaoDiaria, List<DadoHorario> previsaoHoraria) {
        try {
            dadoDiarioDAO.salvarPrevisoes(previsaoDiaria);
            dadoHorarioDAO.salvarLista(previsaoHoraria);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar previsões da API no banco.", e);
        }
    }

    // ALTERADO: boolean -> Boolean
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

    private void atualizarView() {
        if (dadoHorarioAtual == null || localizacaoAtual == null) {
            LOGGER.warning("Tentativa de atualizar a view com dados nulos.");
            return;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        Integer weatherCode = dadoHorarioAtual.getCdClima();

        // --- Atualiza painel de Condições Atuais ---
        view.getLblIconeCondicaoAtual().setIcon(IconUtils.carregarIconeRedimensionado(WeatherCodeUtil.getIconPath(weatherCode), 80, 80));
        view.getLblCondicaoAtualDescricao().setText(WeatherCodeUtil.getWeatherDescription(weatherCode));
        view.getLblTemperaturaAtual().setText(String.format("%.0f°C", dadoHorarioAtual.getTemperatura()));
        view.getLblSensacaoTermica().setText(String.format("Sensação térmica: %.0f°C", dadoHorarioAtual.getSensacaoTermica()));
        view.getLblUmidade().setText(String.format("Umidade: %.0f%%", dadoHorarioAtual.getUmidadeRelativa()));
        view.getLblVento().setText(String.format("Vento: %.1f km/h", dadoHorarioAtual.getVelocidadeVento()));
        view.getLblPrecipitacao().setText(String.format("Precipitação: %.1f mm", dadoHorarioAtual.getPrecipitacao()));
        view.getLblUltimaAtualizacao().setText("Última atualização: " + dadoHorarioAtual.getHorario().format(timeFormatter) + "h");

        // --- Atualiza painel de Localização ---
        view.getLblLocalizacao().setText(localizacaoAtual.getNomeCidade());
        view.getLblLatitude().setText(String.format("Latitude: %.4f", localizacaoAtual.getLatitude()));
        view.getLblLongitude().setText(String.format("Longitude: %.4f", localizacaoAtual.getLongitude()));
        view.getLblAltitude().setText("Altitude: N/A");
        view.getLblFusoHorario().setText("Fuso Horário: N/A");

        // --- Atualiza Previsão Horária ---
        atualizarPrevisaoHoraria();

        // --- Atualiza Cards de Resumo ---
        if (!previsaoDiaria.isEmpty()) {
            DadoDiario hoje = previsaoDiaria.get(0);
            view.getCardTempMax().updateContent(String.format("%.0f°C", hoje.getTemperaturaMax()), "Máx. hoje", IconUtils.carregarIconeRedimensionado("/icons/sun.png", 48, 48));
            view.getCardTempMin().updateContent(String.format("%.0f°C", hoje.getTemperaturaMin()), "Mín. hoje", IconUtils.carregarIconeRedimensionado("/icons/cloud.png", 48, 48));
            view.getCardVento().updateContent(String.format("%.0f km/h", hoje.getVelocidadeVentoMax()), "Vento máx.", IconUtils.carregarIconeRedimensionado("/icons/wind.png", 48, 48));
            view.getCardPrecipitacao().updateContent(String.format("%.1f mm", hoje.getPrecipitacaoTotal()), "Chuva hoje", IconUtils.carregarIconeRedimensionado("/icons/rain.png", 48, 48));
        }

        // --- Atualiza a tabela de 7 dias ---
        DefaultTableModel model = (DefaultTableModel) view.getTabelaPrevisao().getModel();
        model.setRowCount(0);

        Locale localeBrasil = new Locale("pt", "BR");
        for (DadoDiario dia : previsaoDiaria) {
            String nomeDia = dia.getData().getDayOfWeek().getDisplayName(TextStyle.FULL, localeBrasil);
            nomeDia = nomeDia.substring(0, 1).toUpperCase() + nomeDia.substring(1).split("-")[0];
            String condicao = WeatherCodeUtil.getWeatherDescription(dia.getCdClima());

            model.addRow(new Object[]{nomeDia, condicao, String.format("%.0f°C", dia.getTemperaturaMax()),
                    String.format("%.0f°C", dia.getTemperaturaMin()),
                    String.format("%.1f mm", dia.getPrecipitacaoTotal()),
                    String.format("%.0f km/h", dia.getVelocidadeVentoMax())});
        }
        view.revalidate();
        view.repaint();
    }

    private void atualizarPrevisaoHoraria() {
        JPanel painel = view.getPanelPrevisaoHoraria();
        painel.removeAll();
        if (previsaoHoraria == null || previsaoHoraria.isEmpty()) {
            painel.revalidate();
            painel.repaint();
            return;
        }

        previsaoHoraria.forEach(hora -> {
            String strHora = hora.getHorario().format(DateTimeFormatter.ofPattern("HH'h'"));
            String strTemp = String.format("%.0f°C", hora.getTemperatura());
            Integer cdClima = hora.getCdClima();
            String iconPath = WeatherCodeUtil.getIconPath(cdClima);

            ItemPrevisao item = new ItemPrevisao(strHora, strTemp, IconUtils.carregarIconeRedimensionado(iconPath, 32, 32));
            painel.add(item);
        });

        painel.revalidate();
        painel.repaint();
    }
}