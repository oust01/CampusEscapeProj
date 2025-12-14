package entity;

import java.util.Random;
import main.GamePanel;

public class NPC_Guard extends Entity {

    String[] dialogues = new String[10];
    int dialogueIndex = 0;

    public NPC_Guard(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 1;

        getImage();
        setDialogue1();
    }

    public void setDialogue1() {
        dialogues[0] = "NOT ALL DOORS ARE MEANT TO BE OPEN.";
        dialogues[1] = "WATCH OUT FOR THE MONSTERS!!";
        dialogues[2] = "FIND THE STEEL DOOR TO GET OUT";
    }

    @Override
    public void speak() {
        if (dialogueIndex >= dialogues.length || dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }

        gp.ui.showMessage(dialogues[dialogueIndex]);
        dialogueIndex++;
    }


    public void getImage() {
        up1 = setup("/npc/npc_up_1");
        up2 = setup("/npc/npc_up_2");
        down1 = setup("/npc/npc_down_1");
        down2 = setup("/npc/npc_down_2");
        left1 = setup("/npc/npc_left_1");
        left2 = setup("/npc/npc_left_2");
        right1 = setup("/npc/npc_right_1");
        right2 = setup("/npc/npc_right_2");
    }

    public void setAction() {

        actionLockCounter++;

        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) direction = "up";
            else if (i <= 50) direction = "down";
            else if (i <= 75) direction = "left";
            else direction = "right";

            actionLockCounter = 0;
        }
    }
}
