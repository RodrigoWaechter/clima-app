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
		setSize(700, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initComponents();
		initLayout();
	}

	private void initComponents() {
		txtAreaSobre = ComponentFactory.createTextArea();

		txtAreaSobre.setText(
				"Aplicativo de Previsão do Tempo\n\n" +
				"Este aplicativo foi desenvolvido como parte do projeto da disciplina de Programação Avançada, " +
				"visando o consumo de uma API pública para apresentar previsões meteorológicas de forma interativa. \n\n" +
				"Desenvolvedores do time:\n" +
				"- Lucas Souza da Silva\n" +
				"- Rodrigo Hammes Waechter\n" +
				"- Vinicius Grahl Copetti\n\n" +
				"Curso: Ciência da Computação - Universidade de Santa Cruz do Sul\n\n" +
				"Repositório: https://github.com/RodrigoWaechter/clima-app"
		);

		btnFechar = ComponentFactory.createButton("Fechar");
		btnFechar.addActionListener(e -> dispose());

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
