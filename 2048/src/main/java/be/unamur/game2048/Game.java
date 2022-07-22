package be.unamur.game2048;

import be.unamur.game2048.controllers.GameController;
import be.unamur.game2048.views.GamePanel;

import java.awt.*;
import javax.swing.*;

/**
 * /!\ NE PAS TESTER CETTE CLASSE /!\
 * Classe principale pour démarrer le jeu.
 * Inspiré de http://www.rosettacode.org/wiki/2048#Java
 */

public class Game {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("2048");
            f.setResizable(true);
            GamePanel panel = new GamePanel();
            GameController controller = new GameController();
            panel.addController(controller);
            f.add(panel, BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

