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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class CardClima extends JPanel {
    // --- MUDANÇA: Cores fixas removidas ---
    // As cores agora serão lidas do UIManager

    private final int CORNER_RADIUS = 20;
    private final int SHADOW_SIZE = 5;
    private boolean isHovered = false;
    
    private JLabel lblTitulo;
    private JLabel lblIcone;
    private JLabel lblTemperatura;
    private JLabel lblHorario;
    
    // --- MUDANÇA: Variáveis para armazenar as cores do tema ---
    private Color bgColor;
    private Color textColor;
    private Color secondaryColor;
    private Color highlightColor;
    private Color borderColor;
    private Color shadowColor;

    public CardClima(String titulo, String temperatura, String horario, Icon icone) {
        // --- MUDANÇA: updateUI() é chamado para carregar as cores iniciais ---
        updateUI(); 
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        configureComponents(titulo, temperatura, horario, icone);
        setupLayout();
        addHoverEffect();
    }
    
    // --- MUDANÇA: Novo método para carregar cores do Look and Feel ---
    private void updateColorsFromUIManager() {
        bgColor = UIManager.getColor("Panel.background");
        textColor = UIManager.getColor("Label.foreground");
        secondaryColor = UIManager.getColor("Label.disabledForeground");
        highlightColor = UIManager.getColor("Component.focusColor");
        if (highlightColor == null) {
             highlightColor = new Color(70, 130, 180); // Fallback
        }
        borderColor = UIManager.getColor("Button.borderColor");
        if (borderColor == null) {
            borderColor = new Color(220, 220, 220); // Fallback
        }
        shadowColor = UIManager.getColor("Separator.shadow");
         if (shadowColor == null) {
            shadowColor = new Color(0, 0, 0, 20); // Fallback
        }
    }
    
    // --- MUDANÇA: Sobrescrevendo updateUI para reagir a mudanças de tema ---
    @Override
    public void updateUI() {
        super.updateUI();
        updateColorsFromUIManager();
        // Garante que os componentes filhos também sejam atualizados se necessário
        if (lblTitulo != null) {
            configureComponentColors();
            repaint();
        }
    }
    
    private void configureComponentColors() {
        lblTitulo.setForeground(textColor);
        lblHorario.setForeground(secondaryColor);
    }

    private void configureComponents(String titulo, String temperatura, String horario, Icon icone) {
        lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        
        lblIcone = new JLabel(icone);

        lblTemperatura = new JLabel(temperatura) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(
                    0, 0, isHovered ? highlightColor : textColor,
                    getWidth(), 0, isHovered ? highlightColor.brighter() : textColor.brighter());
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
        lblTemperatura.setForeground(new Color(0, 0, 0, 0)); // A cor é controlada pelo paintComponent
        
        lblHorario = new JLabel(horario);
        lblHorario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        configureComponentColors();
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
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (isHovered) {
            g2.setColor(shadowColor);
            g2.fillRoundRect(SHADOW_SIZE, SHADOW_SIZE, 
                          getWidth() - SHADOW_SIZE, getHeight() - SHADOW_SIZE, 
                          CORNER_RADIUS, CORNER_RADIUS);
        }
        
        Paint oldPaint = g2.getPaint();
        if (isHovered) {
            GradientPaint gp = new GradientPaint(
                0, 0, bgColor.brighter(),
                getWidth(), getHeight(), bgColor);
            g2.setPaint(gp);
        } else {
            g2.setColor(bgColor);
        }
        g2.fillRoundRect(0, 0, getWidth() - SHADOW_SIZE, getHeight() - SHADOW_SIZE, 
                       CORNER_RADIUS, CORNER_RADIUS);
        g2.setPaint(oldPaint);
        
        g2.setColor(isHovered ? highlightColor : borderColor);
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
