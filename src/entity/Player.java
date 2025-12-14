package entity;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Boots;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 67;
        worldY = gp.tileSize * 53;
        speed = 6;
        direction = "down";
    }

    public void getPlayerImage() {
        up1 = setup("/player/man_up_1");
        up2 = setup("/player/man_up_2");
        down1 = setup("/player/man_down_1");
        down2 = setup("/player/man_down_2");
        left1 = setup("/player/man_left_1");
        left2 = setup("/player/man_left_2");
        right1 = setup("/player/man_right_1");
        right2 = setup("/player/man_right_2");
    }

    

    public void update() {

        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            // SET DIRECTION
            if(keyH.upPressed) direction = "up";
            else if(keyH.downPressed) direction = "down";
            else if(keyH.leftPressed) direction = "left";
            else if(keyH.rightPressed) direction = "right";

            // RESET COLLISION
            collisionOn = false;

            // TILE COLLISION
            gp.cChecker.checkTile(this);

            // OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

                   
            // MOVE ONLY IF NO COLLISION
            if(!collisionOn) {
                switch(direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            // ANIMATION
            spriteCounter++;
            if(spriteCounter > 12) {
                spriteNum = (spriteNum == 1 ? 2 : 1);
                spriteCounter = 0;
            }
        }
        
        
        
    }


    

    public void pickUpObject(int i) {

        if (i == 999) return;

        String objectName = gp.obj[i].name;

        switch (objectName) {

            case "Key":
                gp.playSE(1);
                hasKey++;
                gp.obj[i] = null;
                gp.ui.showMessage("YOU GOT A KEY! ;)");
                break;

            case "Door":
                gp.playSE(4);
                if (hasKey > 0) {
                    gp.obj[i] = null;
                    collisionOn = false;
                    hasKey--;
                    gp.ui.showMessage("AIGHT YOUR CHOICE....");
                } else {
                    gp.ui.showMessage("YOU SURE?");
                }
                break;

            case "Boots":
                gp.playSE(3);
                speed += 6;
                gp.obj[i] = null;
                break;

            case "Chest":
                gp.playSE(3);

                int chestX = gp.obj[i].worldX;
                int chestY = gp.obj[i].worldY;

                gp.obj[i] = null;       // Remove chest

                gp.obj[i] = new OBJ_Boots(gp); // Replace chest with boots
                gp.obj[i].worldX = chestX;
                gp.obj[i].worldY = chestY;

                gp.ui.showMessage("The chest contained Boots!");
                break;

            case "Steeldoor":
                gp.playSE(4);
                if (hasKey >= 5) {
                    gp.obj[i] = null;
                    collisionOn = false;
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(2);
                    gp.gameState = gp.gameOverState;
                } else {
                    gp.ui.showMessage("YOW! GIVE ME 5 KEYS AND I'LL LET YOU OUT;>");
                }
                break;
              

            case "DecoyKey":  // Handle the DecoyKey
                gp.playSE(1);  // Play sound for pickup (optional)
                // You can add any custom logic for the DecoyKey, such as:
                // - Displaying a message
                // - Triggering a special event, etc.
                gp.obj[i] = null;  // Remove the DecoyKey from the world
                gp.ui.showMessage("GET SCAMMED YOU GREEDY HUMAN (' A ')Ψ");  // Show message
                
                break;
                
                
        }
    }

    public void interactNPC(int i) {
        if (i != 999 && keyH.ePressed) {
            gp.npc[i].speak();
            keyH.ePressed = false;
        }
    }

    public void draw(java.awt.Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up": image = (spriteNum == 1 ? up1 : up2); break;
            case "down": image = (spriteNum == 1 ? down1 : down2); break;
            case "left": image = (spriteNum == 1 ? left1 : left2); break;
            case "right": image = (spriteNum == 1 ? right1 : right2); break;
        }

        g2.drawImage(image, screenX, screenY, null);
    }
    
    
    
}
