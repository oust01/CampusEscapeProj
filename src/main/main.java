package main;

import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class main {

    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setUndecorated(true);
        window.setResizable(false);
        window.setTitle("Campus Escape");

        GamePanel gamePanel = new GamePanel();
        gamePanel.window = window;   // REQUIRED for toggle

        window.add(gamePanel);

        GraphicsDevice gd = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        gd.setFullScreenWindow(window); // START FULLSCREEN
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}
