package com.unisc.projeto.clima_app.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


public class ComponentFactory {

    private static final Font FONT_BOLD_18 = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_PLAIN_14 = new Font("Segoe UI", Font.PLAIN, 14);

    public static JLabel createLabel(String text) {
        return new JLabel(text);
    }
    
    public static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    public static JLabel createTitleLabel(String text) {
        return createLabel(text, FONT_BOLD_18);
    }

    public static JLabel createDefaultLabel(String text) {
        return createLabel(text, FONT_PLAIN_14);
    }
    
    public static JPanel createTitledPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(""));
        return panel;
    }
    
    public static JButton createButton(String text) {
        return new JButton(text);
    }

    public static JTextField createTextField(String toolTipText) {
        JTextField textField = new JTextField();
        textField.setToolTipText(toolTipText);
        textField.setBorder(new LineBorder(new Color(153, 153, 153), 1, true));
        return textField;
    }
}