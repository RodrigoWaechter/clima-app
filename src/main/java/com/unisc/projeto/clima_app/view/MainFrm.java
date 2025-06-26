package com.unisc.projeto.clima_app.view;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.unisc.projeto.clima_app.util.ComponentFactory;

@SuppressWarnings("serial")
public class MainFrm extends JFrame {

	private CardLayout cardLayout;
	private JPanel mainPanel;
	private DashboardFrm dashboardPanel;
	private HistoricoFrm historicoPanel;

	private JMenuItem menuItemDashboard;
	private JMenuItem menuItemSair;
	private JMenuItem menuItemAtualizar;
	private JMenuItem menuItemHistorico;
	private JMenuItem menuItemPreferencias;
	private JMenuItem menuItemSobre;

	public MainFrm() {
		setTitle("Clima App");
		setSize(1200, 850);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		initComponents();
		initLayout();
		initListeners();
	}

	private void initComponents() {
		dashboardPanel = new DashboardFrm();
		historicoPanel = new HistoricoFrm();

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
		menuItemDashboard.addActionListener((ActionEvent e) -> cardLayout.show(mainPanel, "DASHBOARD"));
		menuItemSair.addActionListener(e -> System.exit(0));
		menuItemAtualizar.addActionListener((ActionEvent e) -> dashboardPanel.getController().actionAtualizarDados());
		menuItemHistorico.addActionListener((ActionEvent e) -> cardLayout.show(mainPanel, "HISTORICO"));

		menuItemPreferencias.addActionListener((ActionEvent e) -> JOptionPane.showMessageDialog(this,
				"A tela de preferências ainda será implementada.", "Em Desenvolvimento",
				JOptionPane.INFORMATION_MESSAGE));

		menuItemSobre.addActionListener((ActionEvent e) -> new SobreFrm().setVisible(true));
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (Exception ex) {
			System.err.println("Falha ao inicializar o Look and Feel FlatLaf.");
		}
		SwingUtilities.invokeLater(() -> new MainFrm().setVisible(true));
	}
}