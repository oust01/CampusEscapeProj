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
    
    public boolean visionNarrowed = false;
    GamePanel gp;
    Graphics2D g2;

    // ===== FONTS =====
    Font arial_28, arial_40, arial_80B;
    Font titleFont = new Font("Arial", Font.BOLD, 72);

    // ===== HUD IMAGES =====
    BufferedImage keyImage;
    public BufferedImage cardImage;

    // ===== MENU LOGIC =====
    public int commandNum = 0; // 0: Start, 1: Help, 2: About

    // ===== MESSAGE (NPC DIALOGUE) =====
    public boolean messageOn = false;   
    public String message = "";         
    public int messageCounter = 0;      

    // ===== GAME END FLAGS =====
    public boolean gameFinished = false; 
    public boolean timeUp = false;        

    // ===== START HINT =====
    public boolean showStartHint = true; 
    int startHintCounter = 0;            

    // ===== TIMER =====
    public double playTime;                     
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    // ===== TITLE SCREEN ANIMATION =====
    int floatCounter = 0;                
    int blinkCounter = 0;                
    boolean showPress = true;

    // ===== TIMER BOX SIZE SETTINGS =====
    private final int TIMER_PADDING_X = 12;
    private final int TIMER_PADDING_Y = 8;
    private final int TIMER_MIN_WIDTH = 140;

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_28 = new Font("Arial", Font.BOLD, 28);
        arial_40 = new Font("Arial", Font.BOLD, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;

        try {
            cardImage = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/card.png"));
            UtilityTool uTool = new UtilityTool();
            cardImage = uTool.scaleImage(cardImage, gp.tileSize, gp.tileSize);
        } catch (Exception e) {
            System.out.println("Card PNG not found!");
        }
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
        messageCounter = 0;
    }

    // ================= MAIN DRAW =================
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // 1. TITLE SCREEN
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
            return;
        }

        // 2. HELP SCREEN (Mechanics)
        if (gp.gameState == gp.helpState) {
            drawHelpScreen();
            return;
        }

        // 3. ABOUT SCREEN (Lore)
        if (gp.gameState == gp.aboutState) {
            drawAboutScreen();
            return;
        }

        // 4. VISION DEBUFF
        if (visionNarrowed) {
            g2.setColor(new Color(0, 0, 0, 220)); 
            int screenX = gp.screenWidth / 2;
            int screenY = gp.screenHeight / 2;
            int size = gp.tileSize * 4;
            g2.fillRect(0, 0, gp.screenWidth, screenY - size/2); 
            g2.fillRect(0, screenY + size/2, gp.screenWidth, gp.screenHeight); 
            g2.fillRect(0, screenY - size/2, screenX - size/2, size); 
            g2.fillRect(screenX + size/2, screenY - size/2, screenX, size); 
        }

    

        // 6. PAUSE SCREEN
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        // 7. HUD (Keys, Cards, Timer)
        if (gp.gameState == gp.playState || gp.gameState == gp.pauseState) {
            drawHUD();
        }

        // 8. WIN SCREEN
        if (gameFinished) {
            drawCenteredDialog(new String[]{"CONGRATS! YOU JUST GRADUATED!", "NAKA CHAMBA!", "RETRY (R)   QUIT (Q)"}, arial_40);
            drawSmallTopRightDialog("Time: " + dFormat.format(playTime));
            return; 
        }

        // 9. GAME OVER SCREEN (Timer Check)
        if (playTime >= 180.0 || timeUp) {
            timeUp = true;
            drawCenteredDialog(new String[]{"GAME OVER!", "BALIK FIRST YEAR!", "RETRY (R)   QUIT (Q)"}, arial_40);
            drawSmallTopRightDialog("Time: " + dFormat.format(playTime));
            return;
        }

        // 10. NPC MESSAGE WINDOW
        if (messageOn) {
            drawDialogueWindow();
            messageCounter++;
            if (messageCounter > 60) messageOn = false;
        }
    }

    private void drawHUD() {
        g2.setFont(arial_28);
        g2.setColor(Color.white);

        if (gp.currentMap == 1) {
            if(keyImage != null) g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, gp.tileSize/2 + gp.tileSize + 8, gp.tileSize/2 + 30);
        } else if (gp.currentMap == 2) {
            if(cardImage != null) g2.drawImage(cardImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            g2.drawString("Cards: " + gp.player.cardCount + "/3", gp.tileSize/2 + gp.tileSize + 8, gp.tileSize/2 + 30);
        }

        if (gp.gameState == gp.playState) {
            playTime += 1.0 / 60.0;
        }
        double remaining = Math.max(0, 180.0 - playTime);
        drawSmallTopRightDialog(String.format("%d:%02d", (int) remaining / 60, (int) remaining % 60));
    }

    public void drawHelpScreen() {
        // Dark semi-transparent background
        g2.setColor(new Color(0, 0, 0, 240));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Header
        g2.setColor(Color.white);
        g2.setFont(arial_40);
        String title = "HOW TO PLAY";
        g2.drawString(title, getXforCenteredText(title), gp.tileSize * 2);

        // Subtitle/Instructions
        g2.setFont(arial_28.deriveFont(Font.PLAIN, 22F));
        
        String[] lines = {
            "WASD: Move Character",
            "E: Interact with NPCs",
            "P: Pause the Game",
            "MAP 1: Collect 5 Keys to unlock the exit",
            "MAP 2: Collect 3 Cards to reach the Gate",
            "TRAPS: Avoid Cream tiles (Slow / Blur)",
            "TIME LIMIT: Escape before 3:00 runs out!"
        };
        
        // Calculate vertical center
        int lineSpacing = 50;
        int y = gp.tileSize * 3 + 20;

        for (String line : lines) {
            // Draw a subtle dark box behind each line for better readability
            int textWidth = g2.getFontMetrics().stringWidth(line);
            int x = gp.screenWidth/2 - textWidth/2;
            
            g2.setColor(new Color(255, 255, 255, 30)); // Faint white highlight
            g2.fillRoundRect(x - 10, y - 25, textWidth + 20, 35, 10, 10);
            
            g2.setColor(Color.white);
            g2.drawString(line, x, y);
            y += lineSpacing;
        }

        // Return instruction at the bottom
        g2.setFont(arial_28.deriveFont(Font.BOLD, 20F));
        g2.setColor(Color.yellow);
        String backText = "[ Press ESC to Return to Menu ]";
        g2.drawString(backText, getXforCenteredText(backText), gp.screenHeight - gp.tileSize);
    }

    public void drawAboutScreen() {
        g2.setColor(new Color(30, 0, 0, 240));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setColor(Color.white);
        g2.setFont(arial_40);
        g2.drawString("THE LORE", gp.tileSize, gp.tileSize * 2);

        g2.setFont(arial_28.deriveFont(Font.ITALIC, 22F));
        String lore = "Welcome to the mysterious Chinese campus.\n" +
                      "Your mom enrolled you here for one reason: Discipline.\n" +
                      "The only way to graduate is to ESCAPE.\n\n" +
                      "Navigate the maze, avoid the Monsters, & Twin Orcs,\n" +
                      "and prove you have what it takes to finish school.";
        int y = gp.tileSize * 4;
        for (String line : lore.split("\n")) {
            g2.drawString(line, getXforCenteredText(line), y);
            y += 45;
        }
        g2.drawString("Press ESC to return to Menu", gp.tileSize, gp.screenHeight - gp.tileSize);
    }

    private void drawTitleScreen() {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        floatCounter++;
        int yOffset = (int) (Math.sin(floatCounter * 0.05) * 12);
        g2.setFont(titleFont);
        g2.setColor(Color.white);
        String text = "CAMPUS ESCAPE";
        g2.drawString(text, getXforCenteredText(text), gp.tileSize * 3 + yOffset);

        g2.setFont(arial_28);
        text = "START GAME";
        int y = gp.tileSize * 7;
        g2.drawString(text, getXforCenteredText(text), y);
        if(commandNum == 0) g2.drawString(">", getXforCenteredText(text) - gp.tileSize, y);

        text = "HELP"; y += gp.tileSize;
        g2.drawString(text, getXforCenteredText(text), y);
        if(commandNum == 1) g2.drawString(">", getXforCenteredText(text) - gp.tileSize, y);

        text = "ABOUT"; y += gp.tileSize;
        g2.drawString(text, getXforCenteredText(text), y);
        if(commandNum == 2) g2.drawString(">", getXforCenteredText(text) - gp.tileSize, y);
    }

    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().stringWidth(text);
        return gp.screenWidth/2 - length/2;
    }

    private void drawCenteredDialog(String[] lines, Font font) {
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int padding = 30;
        int boxWidth = 0;
        for (String s : lines) boxWidth = Math.max(boxWidth, fm.stringWidth(s));
        boxWidth += padding * 2;
        int boxHeight = fm.getHeight() * lines.length + padding * 2;
        int x = gp.screenWidth / 2 - boxWidth / 2;
        int y = gp.screenHeight / 2 - boxHeight / 2;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 30, 30);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        int textY = y + padding + fm.getAscent();
        for (String line : lines) {
            g2.drawString(line, gp.screenWidth / 2 - fm.stringWidth(line) / 2, textY);
            textY += fm.getHeight();
        }
    }

    private void drawSmallTopRightDialog(String text) {
        g2.setFont(arial_28);
        FontMetrics fm = g2.getFontMetrics();
        int width = Math.max(TIMER_MIN_WIDTH, fm.stringWidth(text) + TIMER_PADDING_X * 2);
        int height = fm.getHeight() + TIMER_PADDING_Y * 2;
        int x = gp.screenWidth - width - gp.tileSize / 2;
        int y = gp.tileSize / 2;
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRoundRect(x, y, width, height, 20, 20);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 20, 20);
        g2.drawString(text, x + (width - fm.stringWidth(text)) / 2, y + (height + fm.getAscent()) / 2 - 3);
    }

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
        int textX = x + gp.tileSize / 2;
        int textY = y + gp.tileSize;
        for (String line : message.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 35;
        }
    }

    private void drawPauseScreen() {
        g2.setFont(arial_80B);
        g2.setColor(Color.white);
        String text = "PAUSED";
        g2.drawString(text, getXforCenteredText(text), gp.screenHeight / 2);
    }

   
}