package com.unisc.projeto.clima_app.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;

public class DashboardBuilder {

    private final DashboardFrm view;

    public DashboardBuilder(DashboardFrm view) {
        this.view = view;
    }

    //junta todos os panels
    public void build() {
        view.setLayout(new GridBagLayout());
        view.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);

        gbc.gridy = 0;
        view.add(buildHeaderPanel(), gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0;
        view.add(buildTopInfoPanel(), gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 180;
        view.add(buildPrevisaoHorariaPanel(), gbc);

        gbc.ipady = 0; 
        gbc.gridy = 3;
        view.add(buildResumoPanel(), gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        view.add(buildPrevisao7diasPanel(), gbc);
    }

   //monta o panel do topo com o campo de busca e o botao
    private JPanel buildHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));

        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.add(view.getCampoBusca(), BorderLayout.CENTER);
        searchPanel.add(view.getBtnBuscar(), BorderLayout.EAST);
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    //junta os condições atuais e a previsão por hora
    private JPanel buildTopInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.65;
        panel.add(buildCondicoesAtuaisPanel(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        panel.add(buildLocalizacaoAtualPanel(), gbc);

        return panel;
    }
    
    //monta o panel de condiçoes atuais
    private JPanel buildCondicoesAtuaisPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Condições Atuais"));
        panel.add(view.getLblUltimaAtualizacao(), BorderLayout.SOUTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 5, 0, 15); 
        contentPanel.add(view.getLblIconeCondicaoAtual(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1; 
        gbc.anchor = GridBagConstraints.LINE_START; 
        gbc.insets = new Insets(0, 0, 5, 10); 
        contentPanel.add(view.getLblTemperaturaAtual(), gbc);

      
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS)); 

        detailsPanel.add(view.getLblVento());
        detailsPanel.add(view.getLblUmidade());
        detailsPanel.add(view.getLblPrecipitacao());
        detailsPanel.add(view.getLblSensacaoTermica());
        detailsPanel.add(view.getLblCondicaoAtualDescricao());


        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0); 
        contentPanel.add(detailsPanel, gbc);

      

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    //monta o panel de localizaçao atual
    private JPanel buildLocalizacaoAtualPanel() {
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

        panel.add(infoPanel, BorderLayout.NORTH);

        return panel;
    }
    
    //monta o panel de previsao hora a hora
    private JScrollPane buildPrevisaoHorariaPanel() {
        JPanel previsaoContainer = view.getPanelPrevisaoHoraria();

        previsaoContainer.setLayout(new GridBagLayout());
        previsaoContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(previsaoContainer);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Previsão para hoje"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UIManager.getColor("Panel.background"));

        return scrollPane;
    }
    
    //monta o panel resumo do dia
    private JPanel buildResumoPanel() {
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

    //monta o panel com a tabela de previsao pra 7 dias
    private JPanel buildPrevisao7diasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Previsão para 7 dias"));

        JScrollPane scrollPane = new JScrollPane(view.getTabelaPrevisao());
        scrollPane.getViewport().setBackground(UIManager.getColor("Table.background"));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
