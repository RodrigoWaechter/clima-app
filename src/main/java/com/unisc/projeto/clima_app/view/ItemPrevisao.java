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
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ItemPrevisao extends JPanel {
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color HIGHLIGHT_COLOR = new Color(70, 130, 180);
    private final int ARC_WIDTH = 20;
    private final int ARC_HEIGHT = 20;
    private boolean selected = false;

    public ItemPrevisao(String horario, String temperatura, ImageIcon icone) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblHorario = new JLabel(horario);
        lblHorario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblHorario.setForeground(TEXT_COLOR);
        lblHorario.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblIcone = new JLabel(icone) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                // Sombra suave
                if (!selected) {
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillOval(getWidth()/2 - icone.getIconWidth()/2 + 2, 
                               getHeight()/2 - icone.getIconHeight()/2 + 2, 
                               icone.getIconWidth(), icone.getIconHeight());
                }
                
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        lblIcone.setAlignmentX(CENTER_ALIGNMENT);

        // Label de temperatura com gradiente
        JLabel lblTemperatura = new JLabel(temperatura) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (selected) {
                    // Gradiente quando selecionado
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(100, 149, 237),
                        getWidth(), 0, new Color(65, 105, 225));
                    g2.setPaint(gp);
                } else {
                    g2.setColor(TEXT_COLOR);
                }
                
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        lblTemperatura.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTemperatura.setAlignmentX(CENTER_ALIGNMENT);
        lblTemperatura.setPreferredSize(new Dimension(60, 25));

        // Adiciona efeito de hover
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

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fundo arredondado com borda sutil
        if (selected) {
            g2.setColor(HIGHLIGHT_COLOR);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
            
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, ARC_WIDTH-4, ARC_HEIGHT-4);
        } else {
            // Efeito de vidro fosco
            g2.setColor(new Color(255, 255, 255, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
            
            // Borda sutil
            g2.setColor(new Color(200, 200, 200, 100));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, ARC_WIDTH, ARC_HEIGHT);
        }
        
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 180);
    }
}