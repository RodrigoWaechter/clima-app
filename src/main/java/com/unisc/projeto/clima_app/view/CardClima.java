package com.unisc.projeto.clima_app.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class CardClima extends JPanel {
    private final Color BG_COLOR = new Color(255, 255, 255);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color SECONDARY_COLOR = new Color(120, 120, 120);
    private final Color HIGHLIGHT_COLOR = new Color(70, 130, 180);
    private final int CORNER_RADIUS = 20;
    private final int SHADOW_SIZE = 5;
    private boolean isHovered = false;
    
    private JLabel lblTitulo;
    private JLabel lblIcone;
    private JLabel lblTemperatura;
    private JLabel lblHorario;

    public CardClima(String titulo, String temperatura, String horario, Icon icone) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        configureComponents(titulo, temperatura, horario, icone);
        setupLayout();
        addHoverEffect();
    }

    private void configureComponents(String titulo, String temperatura, String horario, Icon icone) {
        lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        lblTitulo.setForeground(TEXT_COLOR);
        
        lblIcone = new JLabel(icone);

        lblTemperatura = new JLabel(temperatura) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Gradiente para a temperatura
                GradientPaint gp = new GradientPaint(
                    0, 0, isHovered ? HIGHLIGHT_COLOR : new Color(40, 40, 40),
                    getWidth(), 0, isHovered ? new Color(100, 149, 237) : new Color(60, 60, 60));
                g2.setPaint(gp);
                
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        lblTemperatura.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTemperatura.setForeground(new Color(0, 0, 0, 0));
        
        lblHorario = new JLabel(horario);
        lblHorario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHorario.setForeground(SECONDARY_COLOR);
    }

    private void setupLayout() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(lblIcone, BorderLayout.EAST);
        topPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(lblTemperatura, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.add(lblHorario);
        lblHorario.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                isHovered = true;
                repaint();
            }

            public void mouseExited(MouseEvent evt) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Importante para limpar a área antes de desenhar
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Desenha sombra se o mouse estiver sobre o card
        if (isHovered) {
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(SHADOW_SIZE, SHADOW_SIZE, 
                          getWidth() - SHADOW_SIZE, getHeight() - SHADOW_SIZE, 
                          CORNER_RADIUS, CORNER_RADIUS);
        }
        
        // Desenha o fundo do card
        Paint oldPaint = g2.getPaint();
        if (isHovered) {
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(245, 248, 255),
                getWidth(), getHeight(), new Color(235, 242, 252));
            g2.setPaint(gp);
        } else {
            g2.setColor(BG_COLOR);
        }
        g2.fillRoundRect(0, 0, getWidth() - SHADOW_SIZE, getHeight() - SHADOW_SIZE, 
                       CORNER_RADIUS, CORNER_RADIUS);
        g2.setPaint(oldPaint);
        
        // Desenha a borda
        g2.setColor(isHovered ? new Color(200, 220, 255) : new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - SHADOW_SIZE - 1, getHeight() - SHADOW_SIZE - 1, 
                        CORNER_RADIUS, CORNER_RADIUS);
        
        g2.dispose();
    }
    public void updateContent(String temperatura, String horario, Icon icone) {
        this.lblTemperatura.setText(temperatura);
        this.lblHorario.setText(horario);
        this.lblIcone.setIcon(icone);
        this.repaint();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 160);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}
