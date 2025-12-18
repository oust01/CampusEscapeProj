package entity;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Boots;

public class Player extends Entity {
	
	
	public int speedBonus = 0; // For Boots or other boosts


    // ===== DEBUFF TIMERS =====
    public int slowTimer = 0;
    public int visionTimer = 0;
    public boolean effectTriggered = false; // Prevents spamming on same tile
    int lastTileCol = -1, lastTileRow = -1; // Tracks last tile position

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    public int cardCount = 0;

    // ===== TALK RANGE =====
    private static final int TALK_RANGE = 360; // pixels (~1.3 tiles)

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

    // ===== SPAWN POINT / DEFAULT VALUES =====
    public void setDefaultValues() {
        slowTimer = 0;
        visionTimer = 0;
        effectTriggered = false;
        speed = 6; // Default speed
        gp.ui.visionNarrowed = false;

        worldX = gp.tileSize * 67;
        worldY = gp.tileSize * 53;
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

    // ===== UPDATE DEBUFFS AND TILE EFFECTS =====
    public void updateDebuffs() {
        // ===== SLOW TIMER =====
    	if (slowTimer > 0) {
    	    slowTimer--;
    	    speed = 2 + speedBonus; // Slow effect plus any bonus
    	} else {
    	    speed = 6 + speedBonus; // Normal speed plus any bonus
    	}


        // ===== VISION TIMER =====
        if (visionTimer > 0) {
            visionTimer--;
            gp.ui.visionNarrowed = true;
        } else {
            gp.ui.visionNarrowed = false;
        }

        // ===== ONLY APPLY TILE EFFECTS ON MAP 2 =====
        if (gp.currentMap == 2) {
            int col = (worldX + solidArea.x) / gp.tileSize;
            int row = (worldY + solidArea.y) / gp.tileSize;

            // Reset effect when stepping to a new tile
            if (col != lastTileCol || row != lastTileRow) {
                lastTileCol = col;
                lastTileRow = row;
                effectTriggered = false;
            }

            int tileNum = gp.tileM.mapTileNum[col][row];

            if (tileNum == 2 && !effectTriggered) { // Cream tile
                double roll = Math.random() * 100;

                if (roll < 40) {
                    if (Math.random() < 0.5) {
                        slowTimer = 180; // 3 seconds
                        gp.ui.showMessage("STUCK IN CREAM!");
                    } else {
                        gp.ui.playTime += 10; // Time penalty
                        gp.ui.showMessage("TIME PENALTY! -10s");
                    }
                } else if (roll < 45) { // 5% speed boost
                    speed += 2; 
                    gp.ui.showMessage("CREAM BOOST!");
                } else if (roll < 120) { // 15% vision blur
                    visionTimer = 300; // 5 seconds
                    gp.ui.showMessage("EYES BLURRED!");
                }

                effectTriggered = true;
            }
        }
    }


    @Override
    public void update() {
        // ===== UPDATE DEBUFFS =====
        updateDebuffs();

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
                spriteNum = (spriteNum == 1 ? 2 : 1);
                spriteCounter = 0;
            }
        }

        // ===== TALK TO NPC =====
        if (keyH.ePressed) {
            talkToNearbyNPC();
            keyH.ePressed = false;
        }
    }

    // ===== TALK TO NPC =====
    private void talkToNearbyNPC() {
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] == null) continue;

            int dx = Math.abs(worldX - gp.npc[i].worldX);
            int dy = Math.abs(worldY - gp.npc[i].worldY);

            if (dx <= TALK_RANGE && dy <= TALK_RANGE) {
                gp.npc[i].speak();
                return;
            }
        }
    }

    // ===== OBJECT PICKUP =====
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
                speedBonus += 3;
                gp.obj[i] = null;
                break;
            case "Chest":
                gp.playSE(3);
                int chestX = gp.obj[i].worldX;
                int chestY = gp.obj[i].worldY;
                gp.obj[i] = new OBJ_Boots(gp);
                gp.obj[i].worldX = chestX;
                gp.obj[i].worldY = chestY;
                gp.ui.showMessage("The chest contained Boots!");
                break;
            case "Steeldoor":
                gp.playSE(4);
                if (hasKey >= 5) {
                    gp.obj[i] = null;
                    collisionOn = false;
                    gp.nextLevel();
                } else {
                    gp.ui.showMessage("YOW! GIVE ME 5 KEYS AND I'LL LET YOU OUT;>");
                }
                break;
            case "DecoyKey":
                gp.playSE(1);
                gp.obj[i] = null;
                gp.ui.showMessage("GET SCAMMED YOU GREEDY HUMAN (' A ')Ψ");
                break;
            case "Card":
                gp.playSE(1);
                cardCount++;
                gp.obj[i] = null;
                gp.ui.showMessage("Security Card collected! (" + cardCount + "/3)");
                break;
            case "Gate":
                if (cardCount >= 3) {
                    gp.gameState = gp.gameOverState;
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(2);
                    gp.obj[i] = null;
                } else {
                    gp.ui.showMessage("The gate is locked. You need " + (3 - cardCount) + " more cards!");
                }
                break;
            case "Goal":
                gp.gameState = gp.gameOverState;
                gp.ui.gameFinished = true;
                gp.stopMusic();
                gp.playSE(2);
                break;
        }
    }

    @Override
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
