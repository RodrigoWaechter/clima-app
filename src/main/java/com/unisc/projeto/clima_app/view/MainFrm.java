package com.unisc.projeto.clima_app.view;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.unisc.projeto.clima_app.controller.PreferenciaController;
import com.unisc.projeto.clima_app.database.SetupDatabase;
import com.unisc.projeto.clima_app.util.ComponentFactory;

@SuppressWarnings("serial")
public class MainFrm extends JFrame {
	private CardLayout cardLayout;
	private JPanel mainPanel;
	private DashboardFrm dashboardPanel;
	private HistoricoFrm historicoPanel;
	private PreferenciaFrm preferenciaPanel;

	private JMenuItem menuItemDashboard;
	private JMenuItem menuItemSair;
	private JMenuItem menuItemAtualizar;
	private JMenuItem menuItemHistorico;
	private JMenuItem menuItemPreferencias;
	private JMenuItem menuItemSobre;

	public MainFrm() {
		setTitle("Clima App");
		setSize(1400, 1050);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		initComponents();
		initLayout();
		initListeners();
		initShutdownHook();

	}

	private void initComponents() {
		dashboardPanel = new DashboardFrm();
		historicoPanel = new HistoricoFrm();
		preferenciaPanel = new PreferenciaFrm();

		menuItemDashboard = ComponentFactory.createMenuItem("Dashboard");
		menuItemSair = ComponentFactory.createMenuItem("Sair");
		menuItemAtualizar = ComponentFactory.createMenuItem("Atualizar dados");
		menuItemHistorico = ComponentFactory.createMenuItem("Histórico");
		menuItemPreferencias = ComponentFactory.createMenuItem("Preferências");
		menuItemSobre = ComponentFactory.createMenuItem("Sobre");
	}

	private void initLayout() {
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		mainPanel.add(dashboardPanel, "DASHBOARD");
		mainPanel.add(historicoPanel, "HISTORICO");
		mainPanel.add(preferenciaPanel, "PREFERENCIAS");

		this.add(mainPanel);

		JMenuBar menuBar = ComponentFactory.createMenuBar();
		JMenu menuArquivo = ComponentFactory.createMenu("Arquivo");
		menuArquivo.add(menuItemDashboard);
		menuArquivo.add(menuItemSair);

		JMenu menuDados = ComponentFactory.createMenu("Dados");
		menuDados.add(menuItemAtualizar);
		menuDados.add(menuItemHistorico);

		JMenu menuConfiguracoes = ComponentFactory.createMenu("Configurações");
		menuConfiguracoes.add(menuItemPreferencias);

		JMenu menuAjuda = ComponentFactory.createMenu("Ajuda");
		menuAjuda.add(menuItemSobre);

		menuBar.add(menuArquivo);
		menuBar.add(menuDados);
		menuBar.add(menuConfiguracoes);
		menuBar.add(menuAjuda);

		setJMenuBar(menuBar);
		cardLayout.show(mainPanel, "DASHBOARD");
	}

	private void initListeners() {
		menuItemDashboard.addActionListener((ActionEvent e) -> navigateTo("DASHBOARD"));
		menuItemSair.addActionListener(e -> System.exit(0));
		menuItemAtualizar.addActionListener((ActionEvent e) -> {
			dashboardPanel.getController().actionAtualizarDados();
			historicoPanel.getController().carregaDados();
		});
		menuItemHistorico.addActionListener((ActionEvent e) -> navigateTo("HISTORICO"));
		menuItemPreferencias.addActionListener((ActionEvent e) -> navigateTo("PREFERENCIAS"));
		menuItemSobre.addActionListener((ActionEvent e) -> new SobreFrm().setVisible(true));
	}

	public void navigateTo(String panelName) {
		cardLayout.show(mainPanel, panelName);
	}

	private void initShutdownHook() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.out.println("Janela fechando. Desligando o contêiner Docker...");

				try {
					ProcessBuilder processBuilder = new ProcessBuilder("/usr/bin/docker", "compose", "down");
					processBuilder.directory(new File(System.getProperty("user.dir")));
					processBuilder.inheritIO();
					Process process = processBuilder.start();
					process.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					System.exit(0);
				}
			}
		});
	}

	public static void main(String[] args) {
		SetupDatabase.runSetup();
		PreferenciaController.inicializarTemaAplicacao();

		SwingUtilities.invokeLater(() -> new MainFrm().setVisible(true));
	}
}