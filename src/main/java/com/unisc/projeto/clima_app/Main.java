package com.unisc.projeto.clima_app;

import com.formdev.flatlaf.FlatLightLaf;
import com.unisc.projeto.clima_app.view.DashboardFrm;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
         try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar o Look and Feel FlatLaf.");
        }

        SwingUtilities.invokeLater(() -> {
            new DashboardFrm().setVisible(true);
        });
    }
}
