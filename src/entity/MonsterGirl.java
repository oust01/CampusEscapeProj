package entity;

import java.util.Random;
import main.GamePanel;

public class MonsterGirl extends Entity {

    GamePanel gp;
    Random random = new Random();

    public MonsterGirl(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = 1;
        speed = 2;
        direction = "down";

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("/monsters/girl_up_1");
        up2 = setup("/monsters/girl_up_2");
        down1 = setup("/monsters/girl_down_1");
        down2 = setup("/monsters/girl_down_2");
        left1 = setup("/monsters/girl_left_1");
        left2 = setup("/monsters/girl_left_2");
        right1 = setup("/monsters/girl_right_1");
        right2 = setup("/monsters/girl_right_2");
    }

    @Override
    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter == 120) {
            int i = random.nextInt(4);

            if (i == 0) direction = "up";
            if (i == 1) direction = "down";
            if (i == 2) direction = "left";
            if (i == 3) direction = "right";

            actionLockCounter = 0;
        }
    }
    
    
    @Override
    public void update() {

        super.update(); // keeps movement, animation, collision

        // GAME OVER when monster touches player
        if (gp.cChecker.checkPlayer(this)) {
            gp.ui.timeUp = true;
            gp.gameState = gp.gameOverState;
            gp.stopMusic();
        }
    }

    
    
    
}
