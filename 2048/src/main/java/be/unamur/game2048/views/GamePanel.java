package be.unamur.game2048.views;

import be.unamur.game2048.controllers.GameController;
import be.unamur.game2048.models.GameParams;
import be.unamur.game2048.models.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /!\ NE PAS TESTER CETTE CLASSE /!\
 * Cette classe s'occupe de l'affichage de la grille.
 * Inspiré de http://www.rosettacode.org/wiki/2048#Java
 */

public class GamePanel extends JPanel {

    private GameController controller;

    public void addController(GameController controller) {
        this.controller = controller;
    }

    public GamePanel() {
        setPreferredSize(new Dimension(900, 700));
        setBackground(ColorPalette.backgroundColor);
        setFont(new Font("SansSerif", Font.BOLD, 48));
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (controller == null)
                    return;
                controller.startGame();
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (controller == null)
                    return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        controller.moveUp(false);
                        break;
                    case KeyEvent.VK_DOWN:
                        controller.moveDown(false);
                        break;
                    case KeyEvent.VK_LEFT:
                        controller.moveLeft(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        controller.moveRight(false);
                        break;
                }
                repaint();
            }
        });

    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g);
    }

    void drawGrid(Graphics2D g) {
        g.setColor(ColorPalette.gridColor);
        g.fillRoundRect(200, 100, 499, 499, 15, 15);
        if (controller.getGamestate() == GameState.running)
            drawGridRunning(g);
        else
            drawGridNotRunning(g);
    }

    private void drawGridNotRunning(Graphics2D g) {
        g.setColor(ColorPalette.startColor);
        g.fillRoundRect(215, 115, 469, 469, 7, 7);

        g.setColor(ColorPalette.gridColor.darker());
        g.setFont(new Font("SansSerif", Font.BOLD, 128));
        g.drawString("2048", 310, 270);

        g.setFont(new Font("SansSerif", Font.BOLD, 20));

        if (controller.getGamestate() == GameState.won)
            g.drawString("You win !", 390, 350);
        else if (controller.getGamestate() == GameState.over)
            g.drawString("You loose !", 400, 350);

        g.setColor(ColorPalette.gridColor);
        g.drawString("Click to start a new game", 330, 470);
        g.drawString("(Use arrow keys to move tiles)", 310, 530);
    }

    private void drawGridRunning(Graphics2D g) {
        for (int row = 0; row < GameParams.sideLength; row++) {
            for (int col = 0; col < GameParams.sideLength; col++) {
                if (controller.getGrid().getTile(row, col) == null) {
                    g.setColor(ColorPalette.emptyColor);
                    g.fillRoundRect(215 + col * 121, 115 + row * 121, 106, 106, 7, 7);
                } else {
                    drawTile(g, row, col);
                }
            }
        }
    }

    void drawTile(Graphics2D g, int r, int c) {
        int value = controller.getGrid().getTile(r, c).getValue();
        int colorPaletteIndex = (int) (Math.log(value) / Math.log(2));
        g.setColor(ColorPalette.colorPalette[colorPaletteIndex]);
        g.fillRoundRect(215 + c * 121, 115 + r * 121, 106, 106, 7, 7);
        String s = String.valueOf(value);

        g.setColor(value < 128 ? ColorPalette.colorPalette[0] : ColorPalette.colorPalette[1]);

        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int dec = fm.getDescent();

        int x = 215 + c * 121 + (106 - fm.stringWidth(s)) / 2;
        int y = 115 + r * 121 + (asc + (106 - (asc + dec)) / 2);

        g.drawString(s, x, y);
    }
}
