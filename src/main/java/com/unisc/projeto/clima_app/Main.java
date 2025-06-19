package com.unisc.projeto.clima_app;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.unisc.projeto.clima_app.view.DashboardFrm;

public class Main {

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            new DashboardFrm().setVisible(true);
        });
    }
}
