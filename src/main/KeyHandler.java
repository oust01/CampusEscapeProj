package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean rPressed, qPressed, ePressed, enterPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

    
        // ===== TITLE STATE =====
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) gp.setupGame();
                if (gp.ui.commandNum == 1) gp.gameState = gp.helpState;
                if (gp.ui.commandNum == 2) gp.gameState = gp.aboutState;
            }
        }

        // ===== HELP / ABOUT =====
        else if (gp.gameState == gp.helpState || gp.gameState == gp.aboutState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.titleState;
            }
        }

        // ===== PLAY STATE =====
        else if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_E) ePressed = true;

            if (code == KeyEvent.VK_P) {
                gp.gameState = gp.pauseState;
            }
        }

        // ===== PAUSE =====
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_P) {
                gp.gameState = gp.playState;
            }
        }

        // ===== GAME OVER =====
        else if (gp.gameState == gp.gameOverState) {
            if (code == KeyEvent.VK_R) gp.setupGame();
            if (code == KeyEvent.VK_Q) System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_E) ePressed = false;
    }
}
