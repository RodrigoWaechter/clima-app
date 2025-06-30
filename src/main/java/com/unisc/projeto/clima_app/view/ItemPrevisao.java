package com.unisc.projeto.clima_app.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ItemPrevisao extends JPanel {
    
    private final int ARC_WIDTH = 20;
    private final int ARC_HEIGHT = 20;
    private boolean selected = false;
    
    private Color textColor;
    private Color highlightColor;
    private Color highlightTextColor;
    private Color bgColor;
    private Color borderColor;
    
    private JLabel lblHorario;
    private JLabel lblTemperatura;

    public ItemPrevisao(String horario, String temperatura, ImageIcon icone) {
        updateUI();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblHorario = new JLabel(horario);
        lblHorario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblHorario.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblIcone = new JLabel(icone);
        lblIcone.setAlignmentX(CENTER_ALIGNMENT);

        lblTemperatura = new JLabel(temperatura);
        lblTemperatura.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTemperatura.setAlignmentX(CENTER_ALIGNMENT);
        lblTemperatura.setPreferredSize(new Dimension(60, 25));
        
        configureComponentColors();

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setSelected(true);
            }
            public void mouseExited(MouseEvent evt) {
                setSelected(false);
            }
        });

        add(Box.createVerticalStrut(5));
        add(lblHorario);
        add(Box.createVerticalStrut(10));
        add(lblIcone);
        add(Box.createVerticalStrut(10));
        add(lblTemperatura);
        add(Box.createVerticalStrut(5));
    }
    
    // metodo pra carregar as cores do tema escolhido nas preferencias
    private void updateColorsFromUIManager() {
        textColor = UIManager.getColor("Label.foreground");
        highlightColor = UIManager.getColor("List.selectionBackground");
        highlightTextColor = UIManager.getColor("List.selectionForeground");
        bgColor = UIManager.getColor("Panel.background");
        borderColor = UIManager.getColor("Button.borderColor");

        // Fallbacks para caso o tema não defina alguma cor
        if (highlightColor == null) highlightColor = new Color(70, 130, 180);
        if (highlightTextColor == null) highlightTextColor = Color.WHITE;
        if (borderColor == null) borderColor = new Color(200, 200, 200, 100);
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        updateColorsFromUIManager();
        if(lblHorario != null) {
            configureComponentColors();
            repaint();
        }
    }
    
    private void configureComponentColors() {
        if (selected) {
            lblHorario.setForeground(highlightTextColor);
            lblTemperatura.setForeground(highlightTextColor);
        } else {
            lblHorario.setForeground(textColor);
            lblTemperatura.setForeground(textColor);
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        configureComponentColors();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (selected) {
            GradientPaint gp = new GradientPaint(0, 0, highlightColor, getWidth(), getHeight(), highlightColor.brighter());
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
        } else {
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
            
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
        }
        
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 180);
    }
}
