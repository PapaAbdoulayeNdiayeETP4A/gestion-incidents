package com.gestionincidents;

import javax.swing.SwingUtilities;

import com.gestionincidents.view.FenetreConnexion;

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