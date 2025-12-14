package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;
import java.awt.Color;

public class Entity {

    GamePanel gp;

    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    public int actionLockCounter = 0;

    // ===== ENTITY TYPE =====
    // 0 = NPC / Player
    // 1 = Monster
    public int type = 0;

    // ===== MONSTER DIALOGUE =====
    public String[] dialogue = {
        "HOY!! HAIN TIM ID??",
        "PREPARE 1 WHOLE WE HAVE A QUIZ!!",
        "BALIK NALA NEXT ISKOL YER!!"
    };
    public String currentDialogue = "";
    public int dialogueCounter = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {}

    public void update() {

        setAction();

        // ===== MONSTER DIALOGUE TIMER =====
        if (type == 1) {
            dialogueCounter++;
            if (dialogueCounter >= 180) { // 3 seconds
                int i = (int)(Math.random() * dialogue.length);
                currentDialogue = dialogue[i];
                dialogueCounter = 0;
            }
        }

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);

        // ===== ONLY MONSTERS KILL PLAYER =====
        if (type == 1 && gp.cChecker.checkPlayer(this)) {
            gp.gameState = gp.gameOverState;
            return;
        }

        if (!collisionOn) {

            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }

        } else if (type == 1) {
            // ===== MONSTER BOUNCE =====
            switch (direction) {
                case "up": direction = "down"; break;
                case "down": direction = "up"; break;
                case "left": direction = "right"; break;
                case "right": direction = "left"; break;
            }
        }




        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up": image = (spriteNum == 1) ? up1 : up2; break;
                case "down": image = (spriteNum == 1) ? down1 : down2; break;
                case "left": image = (spriteNum == 1) ? left1 : left2; break;
                case "right": image = (spriteNum == 1) ? right1 : right2; break;
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            // ===== DRAW MONSTER DIALOGUE =====
            if (type == 1 && !currentDialogue.isEmpty()) {

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(
                    screenX - 4,
                    screenY - 28,
                    g2.getFontMetrics().stringWidth(currentDialogue) + 8,
                    20,
                    8,
                    8
                );

                g2.setColor(Color.BLACK);
                g2.drawString(currentDialogue, screenX, screenY - 14);
            }
        }
    }

    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void speak() {
        // unused
    }
}
