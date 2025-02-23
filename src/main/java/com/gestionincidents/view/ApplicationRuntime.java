package com.gestionincidents.view;

import javax.swing.SwingUtilities;

public class ApplicationRuntime {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FenetreConnexion();
            }
        });
    }
}