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

    // ===== HOW CLOSE YOU NEED TO TALK =====
    private static final int TALK_RANGE = 64; // pixels

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

    @Override
    public void update() {

        // ===== MOVEMENT =====
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) direction = "up";
            else if (keyH.downPressed) direction = "down";
            else if (keyH.leftPressed) direction = "left";
            else if (keyH.rightPressed) direction = "right";

            collisionOn = false;

            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            if (!collisionOn) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        // ===== TALK TO NPC WHEN NEAR (NO COLLISION) =====
        if (keyH.ePressed) {
            interactNearbyNPC();
            keyH.ePressed = false;
        }
    }

    // ===== NEW PROXIMITY NPC INTERACTION =====
    private void interactNearbyNPC() {

        for (int i = 0; i < gp.npc.length; i++) {

            if (gp.npc[i] == null) continue;

            int dx = Math.abs(worldX - gp.npc[i].worldX);
            int dy = Math.abs(worldY - gp.npc[i].worldY);

            if (dx <= TALK_RANGE && dy <= TALK_RANGE) {
                gp.npc[i].speak();
                return; // talk to only ONE NPC
            }
        }
    }

    // ===== OBJECT PICKUP (UNCHANGED) =====
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

            case "Door
