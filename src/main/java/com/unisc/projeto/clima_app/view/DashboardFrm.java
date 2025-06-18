package com.unisc.projeto.clima_app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DashboardFrm extends JFrame {

	public DashboardFrm( ) {
		initComponents();
	}
	
	private void initComponents() {
		// Configs da janela
		setTitle("Clima App");
		setSize(800, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		// MENU SUPERIOR
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuArquivo = new JMenu("Arquivo");
		menuArquivo.add(new JMenuItem("Dashboard"));
		menuArquivo.addSeparator();
		menuArquivo.add(new JMenuItem("Sair"));
		
		JMenu menuDados = new JMenu("Dados");
		menuDados.add(new JMenuItem("Atualizar Dados"));
		menuDados.add(new JMenuItem("Histórico"));
		
		JMenu menuConfiguracoes = new JMenu("Configurações");
		menuConfiguracoes.add(new JMenuItem("Preferências"));
		
		JMenu menuAjuda = new JMenu("Ajuda");
		menuAjuda.add(new JMenuItem("Sobre"));
		
		menuBar.add(menuArquivo);
		menuBar.add(menuDados);
	    menuBar.add(menuConfiguracoes);
	    menuBar.add(menuAjuda);

	    setJMenuBar(menuBar);
	    
	    // PAINEL SUPERIOR - BUSCA
	    JPanel panelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JTextField txtBusca = new JTextField(30);
	   // JButton btnBuscar = new JButton(new ImageIcon(getClass().getResource("/resources/search.png")));
	    JButton btnBuscar = new JButton(carregarIconeRedimensionado("/resources/search.png", 20, 20));
	    panelBusca.add(new JLabel("Pesquisa uma cidade:"));
	    panelBusca.add(txtBusca);
	    panelBusca.add(btnBuscar);
	    
	    // PAINEL CENTRAL - DASHBOARD
	    JPanel panelCentral = new JPanel();
	    panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
	    
	    JLabel lblCidade = new JLabel("Santa Cruz do Sul - RS", SwingConstants.CENTER);
	    lblCidade.setFont(new Font("SansSerif", Font.BOLD, 24));
	    lblCidade.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JLabel lblIconeClima = new JLabel(carregarIconeRedimensionado("/resources/cloud.png", 100, 100));
        lblIconeClima.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JLabel lblTemperatura = new JLabel("30°C", SwingConstants.CENTER);
        lblTemperatura.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTemperatura.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDescricao = new JLabel("Nublado", SwingConstants.CENTER);
        lblDescricao.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblDescricao.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentral.add(lblCidade);
        panelCentral.add(lblIconeClima);
        panelCentral.add(lblTemperatura);
        panelCentral.add(lblDescricao);
        
        // PAINEL INFERIOR - DADOS DETALHADOS
        JPanel panelDetalhes = new JPanel(new GridLayout(1, 3, 10, 10));
        panelDetalhes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelDetalhes.add(criarCard("Umidade", "70%", "/resources/humidity.png"));
        panelDetalhes.add(criarCard("Vento", "4.1 km/h", "/resources/wind.png"));
        panelDetalhes.add(criarCard("Chuva", "2%", "/resources/rain.png"));

        // Adicionar aos painéis principais
        add(panelBusca, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelDetalhes, BorderLayout.SOUTH);
		
	}
	
	//Método auxiliar para criar os cards do painel inferior
	private JPanel criarCard(String titulo, String valor, String caminhoIcone) {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBackground(new Color(240, 240, 240));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    JLabel lblIcon = new JLabel(carregarIconeRedimensionado(caminhoIcone, 40, 40));
	    lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel lblTitulo = new JLabel(titulo);
	    lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
	    lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel lblValor = new JLabel(valor);
	    lblValor.setFont(new Font("SansSerif", Font.PLAIN, 14));
	    lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

	    panel.add(lblIcon);
	    panel.add(lblTitulo);
	    panel.add(lblValor);

	    return panel;
	}
	
	private ImageIcon carregarIconeRedimensionado(String caminho, int largura, int altura) {
	    ImageIcon icon = new ImageIcon(getClass().getResource(caminho));
	    Image imagem = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
	    return new ImageIcon(imagem);
	}

}


