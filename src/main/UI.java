package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.OBJ_Key;

public class UI {

    // Reference to GamePanel (access states, screen size, player, etc.)
    GamePanel gp;
    Graphics2D g2;

    // ===== FONTS =====
    Font arial_28, arial_40, arial_80B;
    Font titleFont = new Font("Arial", Font.BOLD, 72);

    // ===== HUD IMAGES =====
    BufferedImage keyImage;

    // ===== MESSAGE (NPC DIALOGUE) =====
    public boolean messageOn = false;   // show dialogue window
    public String message = "";         // dialogue text
    public int messageCounter = 0;      // timer for dialogue auto-hide

    // ===== GAME END FLAGS =====
    public boolean gameFinished = false; // win condition
    public boolean timeUp = false;        // lose condition (timer)

    // ===== START HINT =====
    public boolean showStartHint = true; // show hint at game start
    int startHintCounter = 0;            // how long hint stays on screen

    // ===== TIMER =====
    double playTime;                     // elapsed time in seconds
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    // ===== TITLE SCREEN ANIMATION =====
    int floatCounter = 0;                // floating title animation
    int blinkCounter = 0;                // blinking "press enter"
    boolean showPress = true;

    // ===== TIMER BOX SIZE SETTINGS =====
    private final int TIMER_PADDING_X = 12;
    private final int TIMER_PADDING_Y = 8;
    private final int TIMER_MIN_WIDTH = 140;

    public UI(GamePanel gp) {
        this.gp = gp;

        // Initialize fonts
        arial_28 = new Font("Arial", Font.BOLD, 28);
        arial_40 = new Font("Arial", Font.BOLD, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        // Load key image for HUD
        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;
    }

    // Show NPC message
    public void showMessage(String text) {
        message = text;
        messageOn = true;
        messageCounter = 0;
    }

    // ================= MAIN DRAW =================
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // ===== START GAME HINT =====
        if (gp.gameState == gp.playState && showStartHint) {
            drawHintBox(
                "Press E to talk to the guard",
                gp.screenWidth / 2 - gp.tileSize * 3,
                gp.screenHeight - gp.tileSize * 3,
                gp.tileSize * 6,
                gp.tileSize + 10
            );

            // Hide hint after ~4 seconds (240 frames @60fps)
            startHintCounter++;
            if (startHintCounter > 240) showStartHint = false;
        }

        // ===== TITLE SCREEN =====
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
            return;
        }

        // ===== PAUSE SCREEN =====
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        // ===== WIN SCREEN =====
        if (gameFinished) {
            drawCenteredDialog(
                new String[]{
                    "YOU WON! IM PROUD OF YOU <3",
                    "NAKA CHAMBA!",
                    "RETRY (R)     QUIT (Q)"
                },
                arial_40
            );
            drawSmallTopRightDialog("Time: " + dFormat.format(playTime));
            return;
        }

        // ===== GAME OVER SCREEN =====
        if (timeUp) {
            drawCenteredDialog(
                new String[]{
                    "GAME OVER!",
                    "WALA KANG BITAW MAN!",
                    "RETRY (R)     QUIT (Q)"
                },
                arial_40
            );
            drawSmallTopRightDialog("Time: " + dFormat.format(playTime));
            return;
        }

        // ===== HUD (KEY COUNT) =====
        g2.setFont(arial_28);
        g2.setColor(Color.white);
        g2.drawImage(keyImage,
                gp.tileSize / 2,
                gp.tileSize / 2,
                gp.tileSize,
                gp.tileSize,
                null);

        g2.drawString("x " + gp.player.hasKey,
                gp.tileSize / 2 + gp.tileSize + 8,
                gp.tileSize / 2 + 30);

        // ===== TIMER UPDATE =====
        if (gp.gameState == gp.playState) {
            playTime += 1.0 / 60.0; // increase time every frame
        }

        // 🔧 CHANGE THIS VALUE TO ADJUST TIME LIMIT (seconds)
        double remaining = Math.max(0, 180.0 - playTime);

        drawSmallTopRightDialog(String.format(
                "%d:%02d",
                (int) remaining / 60,
                (int) remaining % 60
        ));

        // Trigger game over when time runs out
        if (playTime >= 180.0 && !timeUp) {
            timeUp = true;
        }

        // ===== NPC MESSAGE WINDOW =====
        if (messageOn) {
            drawDialogueWindow();
            messageCounter++;

            // Auto-hide dialogue after ~3 seconds
            if (messageCounter > 180) messageOn = false;
        }
    }

    // ================= CENTERED DIALOG (WIN / LOSE) =================
    private void drawCenteredDialog(String[] lines, Font font) {

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int padding = 30;
        int lineHeight = fm.getHeight();

        // Auto-size box based on longest line
        int boxWidth = 0;
        for (String s : lines) {
            boxWidth = Math.max(boxWidth, fm.stringWidth(s));
        }
        boxWidth += padding * 2;

        int boxHeight = lineHeight * lines.length + padding * 2;

        int x = gp.screenWidth / 2 - boxWidth / 2;
        int y = gp.screenHeight / 2 - boxHeight / 2;

        // Background
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        // Border
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        // Text
        int textY = y + padding + fm.getAscent();
        for (String line : lines) {
            int textX = gp.screenWidth / 2 - fm.stringWidth(line) / 2;
            g2.drawString(line, textX, textY);
            textY += lineHeight;
        }
    }

    // ================= TIMER BOX (TOP RIGHT) =================
    private void drawSmallTopRightDialog(String text) {
        g2.setFont(arial_28);
        FontMetrics fm = g2.getFontMetrics();

        int width = Math.max(
                TIMER_MIN_WIDTH,
                fm.stringWidth(text) + TIMER_PADDING_X * 2
        );
        int height = fm.getHeight() + TIMER_PADDING_Y * 2;

        int x = gp.screenWidth - width - gp.tileSize / 2;
        int y = gp.tileSize / 2;

        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRoundRect(x, y, width, height, 20, 20);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 20, 20);

        g2.drawString(text,
                x + (width - fm.stringWidth(text)) / 2,
                y + (height + fm.getAscent()) / 2 - 3);
    }

    // ================= TITLE SCREEN =================
    private void drawTitleScreen() {

        // Solid background (change to transparent if needed)
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        floatCounter++;
        int yOffset = (int) (Math.sin(floatCounter * 0.05) * 12);

        g2.setFont(titleFont);
        g2.setColor(Color.white);
        g2.drawString(
                "CAMPUS ESCAPE",
                centerX("CAMPUS ESCAPE"),
                gp.screenHeight / 2 - 60 + yOffset
        );

        blinkCounter++;
        if (blinkCounter > 30) {
            showPress = !showPress;
            blinkCounter = 0;
        }

        if (showPress) {
            g2.setFont(arial_28);
            g2.drawString(
                    "PRESS ENTER TO START",
                    centerX("PRESS ENTER TO START"),
                    gp.screenHeight / 2 + 40
            );
        }
    }

    // Center text horizontally
    private int centerX(String text) {
        return gp.screenWidth / 2
                - g2.getFontMetrics().stringWidth(text) / 2;
    }

    // ================= NPC DIALOGUE WINDOW =================
    private void drawDialogueWindow() {

        int width = gp.screenWidth - gp.tileSize * 2;
        int height = gp.tileSize * 3;
        int x = gp.tileSize;
        int y = gp.screenHeight - height - gp.tileSize;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x, y, width, height, 30, 30);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x, y, width, height, 30, 30);

        g2.setFont(arial_28);
        int textX = x + gp.tileSize / 2;
        int textY = y + gp.tileSize;

        for (String line : message.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 35;
        }
    }

    // ================= PAUSE SCREEN =================
    private void drawPauseScreen() {
        g2.setFont(arial_80B);
        g2.setColor(Color.white);
        g2.drawString("PAUSED", centerX("PAUSED"), gp.screenHeight / 2);
    }

    // ================= SMALL HINT BOX =================
    private void drawHintBox(String text, int x, int y, int w, int h) {

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x, y, w, h, 25, 25);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, w, h, 25, 25);

        g2.setFont(new Font("Arial", Font.PLAIN, 22));
        FontMetrics fm = g2.getFontMetrics();

        g2.drawString(
                text,
                x + (w - fm.stringWidth(text)) / 2,
                y + (h + fm.getAscent()) / 2 - 4
        );
    }
}
