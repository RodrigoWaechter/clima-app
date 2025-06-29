package com.unisc.projeto.clima_app.view;

import com.unisc.projeto.clima_app.util.ComponentFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SuppressWarnings("serial")
public class SobreFrm extends JFrame {

	private JTextArea txtAreaSobre;
	private JButton btnFechar;

	public SobreFrm() {
		setTitle("Clima App");
		setSize(500, 490);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initComponents();
		initLayout();
	}

	private void initComponents() {
		txtAreaSobre = new JTextArea(10, 40);
		txtAreaSobre.setEditable(false);
		txtAreaSobre.setLineWrap(true);
		txtAreaSobre.setWrapStyleWord(true);

		txtAreaSobre.setText(
				"Aplicativo de Previsão do Tempo\n\n" +
				"Este aplicativo foi desenvolvido como parte do projeto da disciplina de Programação Avançada, " +
				"visando o consumo de uma API pública para apresentar previsões meteorológicas de forma interativa. \n\n" +
				"Desenvolvedores do time:\n" +
				"- Lucas Souza da Silva\n" +
				"- Rodrigo Hammes Waechter\n" +
				"- Vinicius Grahl Copetti\n\n" +
				"Curso: Ciência da Computação - Universidade de Santa Cruz do Sul\n\n" +
				"Repositório: /https://github.com/RodrigoWaechter/clima-app"
		);

		// botão de fechar com destaque
		btnFechar = ComponentFactory.createButton("Fechar");
		btnFechar.addActionListener(e -> dispose());

		estilizarComponentes();
	}

	private void estilizarComponentes() {
		// estilização do texto
		txtAreaSobre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtAreaSobre.setBackground(new Color(245, 245, 245));
		txtAreaSobre.setBorder(new EmptyBorder(10, 10, 10, 10));

		// sstilização do botão
		btnFechar.setBackground(new Color(220, 53, 69));
		btnFechar.setForeground(Color.WHITE);
		btnFechar.setFocusPainted(false);
		btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 12));
	}

	private void initLayout() {
		JPanel mainPnl = ComponentFactory.createPanel(new BorderLayout(10, 10));
		mainPnl.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel pnlTexto = ComponentFactory.createPanel(new GridBagLayout());
		pnlTexto.setBorder(new TitledBorder("Sobre o Projeto"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;

		JScrollPane scroll = new JScrollPane(txtAreaSobre);
		pnlTexto.add(scroll, gbc);

		JPanel btnPanel = ComponentFactory.createPanel(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		btnPanel.add(btnFechar);

		mainPnl.add(pnlTexto, BorderLayout.CENTER);
		mainPnl.add(btnPanel, BorderLayout.SOUTH);

		this.add(mainPnl);

	}

	public JButton getBtnFechar() {
		return btnFechar;
	}

}
