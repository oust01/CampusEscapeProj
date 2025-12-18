package entity;

import java.util.Random;
import main.GamePanel;

public class MonsterOrc extends Entity {

    public MonsterOrc(GamePanel gp) {
        super(gp);

        type = 1; // 1 = Monster (Game Over on touch)
  
        speed = 4; // Adjust speed (make it slightly slower or faster than player)
        direction = "down";

        // Set collision area
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        // Assuming your images are in /monsters/orc_up_1.png, etc.
        up1 = setup("/monsters/orc_up_1");
        up2 = setup("/monsters/orc_up_2");
        down1 = setup("/monsters/orc_down_1");
        down2 = setup("/monsters/orc_down_2");
        left1 = setup("/monsters/orc_left_1");
        left2 = setup("/monsters/orc_left_2");
        right1 = setup("/monsters/orc_right_1");
        right2 = setup("/monsters/orc_right_2");
    }

    @Override
    public void setAction() {
        actionLockCounter++;

        // Recalculate if time is up OR if we just hit a wall (forced by Entity class)
        if (actionLockCounter >= 30) {
            
            int xDistance = Math.abs(worldX - gp.player.worldX);
            int yDistance = Math.abs(worldY - gp.player.worldY);

            // BOSS SKILL: If blocked, try the opposite axis to "slide" around walls
            if (collisionOn) {
                if (direction.equals("up") || direction.equals("down")) {
                    direction = (gp.player.worldX < worldX) ? "left" : "right";
                } else {
                    direction = (gp.player.worldY < worldY) ? "up" : "down";
                }
            } else {
                // Standard chase: follow the largest distance
                if (xDistance > yDistance) {
                    direction = (gp.player.worldX < worldX) ? "left" : "right";
                } else {
                    direction = (gp.player.worldY < worldY) ? "up" : "down";
                }
            }
            actionLockCounter = 0;
        }
    }
}