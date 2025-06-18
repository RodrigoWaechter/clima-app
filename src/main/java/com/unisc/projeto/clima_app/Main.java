package com.unisc.projeto.clima_app;

import javax.swing.SwingUtilities;

import com.unisc.projeto.clima_app.view.DashboardFrm;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) {
    	  SwingUtilities.invokeLater(() -> {
              new DashboardFrm().setVisible(true);
          });
    }
}
