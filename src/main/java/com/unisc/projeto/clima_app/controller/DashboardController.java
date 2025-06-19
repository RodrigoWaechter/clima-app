package com.unisc.projeto.clima_app.controller;

import com.unisc.projeto.clima_app.api.GeocodingAPI;
import com.unisc.projeto.clima_app.api.OpenMeteoAPI;
import com.unisc.projeto.clima_app.dao.DadoHorarioDAO;
import com.unisc.projeto.clima_app.dao.LocalizacaoDAO;
import com.unisc.projeto.clima_app.domain.DadoHorario;
import com.unisc.projeto.clima_app.domain.Localizacao;
import com.unisc.projeto.clima_app.view.DashboardFrm;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Optional;

public class DashboardController {

    private final DashboardFrm view;
    private final LocalizacaoDAO localizacaoDAO;
    private final DadoHorarioDAO dadoHorarioDAO;
    private final OpenMeteoAPI openMeteoAPI;
    private final GeocodingAPI geocodingAPI;
    private final Timer autocompleteTimer; 

    public DashboardController(DashboardFrm view) {
        this.view = view;
        this.localizacaoDAO = new LocalizacaoDAO();
        this.dadoHorarioDAO = new DadoHorarioDAO();
        this.openMeteoAPI = new OpenMeteoAPI();
        this.geocodingAPI = new GeocodingAPI();
        
        // AJUSTADO: Aumenta o tempo de espera para 400ms para uma experiência mais estável.
        this.autocompleteTimer = new Timer(400, e -> performAutocompleteSearch());
        this.autocompleteTimer.setRepeats(false);
        
        this.initController();
    }

    private void initController() {
        view.getBtnBuscar().addActionListener(e -> handleSearchAction());
        
        view.addSearchDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { autocompleteTimer.restart(); }
            @Override
            public void removeUpdate(DocumentEvent e) { autocompleteTimer.restart(); }
            @Override
            public void changedUpdate(DocumentEvent e) { /* Não usado */ }
        });

        view.getTxtBusca().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // AJUSTADO: Pequeno aumento no delay para garantir que o clique no popup seja processado.
                Timer hideTimer = new Timer(250, ae -> view.hideSuggestions());
                hideTimer.setRepeats(false);
                hideTimer.start();
            }
        });
    }

    private void performAutocompleteSearch() {
        String query = view.getTxtBusca().getText().trim();
        if (query.length() < 3) {
            view.hideSuggestions();
            return;
        }
        
        SwingWorker<List<Localizacao>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Localizacao> doInBackground() throws Exception {
                return geocodingAPI.searchCitiesByName(query);
            }

            @Override
            protected void done() {
                try {
                    List<Localizacao> suggestions = get();
                    view.showSuggestions(suggestions, DashboardController.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    public void selectSuggestionAndSearch(Localizacao localizacao) {
        autocompleteTimer.stop();
        view.getTxtBusca().setText(localizacao.getNomeCidade());
        fetchAndDisplayData(localizacao.getNomeCidade());
    }

    private void handleSearchAction() {
        autocompleteTimer.stop();
        view.hideSuggestions();
        String cidade = view.getTxtBusca().getText().trim();
        if (cidade.isEmpty()) {
            view.showMessage("Por favor, digite o nome de uma cidade.", "Aviso");
            return;
        }
        fetchAndDisplayData(cidade);
    }
    
    private void fetchAndDisplayData(String nomeCidade) {
        Optional<Localizacao> optLocalizacao = localizacaoDAO.findByName(nomeCidade);

        if (optLocalizacao.isEmpty()) {
            view.showMessage("Cidade não encontrada localmente.\nA procurar nos nossos mapas...", "Aviso");
            optLocalizacao = geocodingAPI.findCoordinatesByCityName(nomeCidade)
                                        .map(localizacaoDAO::save); 
        }

        optLocalizacao.ifPresentOrElse(localizacao -> {
            openMeteoAPI.fetchCurrentWeather(localizacao).ifPresentOrElse(dadoApi -> {
                dadoHorarioDAO.save(dadoApi);
                view.updateUI(localizacao, dadoApi);
            }, () -> {
                view.showErrorMessage("Falha ao buscar dados em tempo real.\nA exibir último registo local.");
                dadoHorarioDAO.findLatestByLocalizacaoId(localizacao.getIdLocalizacao()).ifPresentOrElse(
                    dadoDb -> view.updateUI(localizacao, dadoDb),
                    () -> view.showMessage("Não há dados climáticos para " + nomeCidade, "Sem Dados")
                );
            });
        }, () -> {
            view.showErrorMessage("A cidade '" + nomeCidade + "' não foi encontrada nos nossos mapas.");
        });
    }
}
