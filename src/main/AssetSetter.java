package main;

import entity.NPC_Guard;
import entity.Monster;
import entity.MonsterGirl;
import entity.MonsterOrc;
import object.*;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = 29 * gp.tileSize;
        gp.obj[0].worldY = 43 * gp.tileSize;

        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 34 * gp.tileSize;
        gp.obj[1].worldY = 28 * gp.tileSize;

        gp.obj[2] = new OBJ_Key(gp);
        gp.obj[2].worldX = 51 * gp.tileSize;
        gp.obj[2].worldY = 36 * gp.tileSize;

        gp.obj[3] = new OBJ_Chest(gp);
        gp.obj[3].worldX = 41 * gp.tileSize;
        gp.obj[3].worldY = 26 * gp.tileSize;

        gp.obj[4] = new OBJ_Key(gp);
        gp.obj[4].worldX = 60 * gp.tileSize;
        gp.obj[4].worldY = 26 * gp.tileSize;

        gp.obj[5] = new OBJ_Door(gp);
        gp.obj[5].worldX = 39 * gp.tileSize;
        gp.obj[5].worldY = 36 * gp.tileSize;

        gp.obj[6] = new OBJ_Steeldoor(gp);
        gp.obj[6].worldX = 71 * gp.tileSize;
        gp.obj[6].worldY = 34 * gp.tileSize;

        gp.obj[7] = new OBJ_Key(gp);
        gp.obj[7].worldX = 43 * gp.tileSize;
        gp.obj[7].worldY = 45 * gp.tileSize;

        gp.obj[9] = new OBJ_DecoyKey(gp);
        gp.obj[9].worldX = 39 * gp.tileSize;
        gp.obj[9].worldY = 28 * gp.tileSize;

        gp.obj[10] = new OBJ_NormalDoor(gp);
        gp.obj[10].worldX = 59 * gp.tileSize;
        gp.obj[10].worldY = 44 * gp.tileSize;

        gp.obj[11] = new OBJ_NormalDoor(gp);
        gp.obj[11].worldX = 70 * gp.tileSize;
        gp.obj[11].worldY = 32 * gp.tileSize;

        gp.obj[12] = new OBJ_NormalDoor(gp);
        gp.obj[12].worldX = 35 * gp.tileSize;
        gp.obj[12].worldY = 51 * gp.tileSize;

        gp.obj[13] = new OBJ_NormalDoor(gp);
        gp.obj[13].worldX = 36 * gp.tileSize;
        gp.obj[13].worldY = 41 * gp.tileSize;

        gp.obj[14] = new OBJ_NormalDoor(gp);
        gp.obj[14].worldX = 47 * gp.tileSize;
        gp.obj[14].worldY = 35 * gp.tileSize;
    }

    public void setNPC() {

        // GUARD (friendly / interactive)
       /* gp.npc[0] = new NPC_Guard(gp);
        gp.npc[0].worldX = gp.tileSize * 63;
        gp.npc[0].worldY = gp.tileSize * 51;

        // MONSTER (touch = GAME OVER)
        gp.npc[1] = new Monster(gp);
        gp.npc[1].worldX = gp.tileSize * 23;
        gp.npc[1].worldY = gp.tileSize * 47;

        gp.npc[2] = new MonsterGirl(gp);
        gp.npc[2].worldX = gp.tileSize * 32;
        gp.npc[2].worldY = gp.tileSize * 42;
        
        gp.npc[3] = new Monster(gp);
        gp.npc[3].worldX = gp.tileSize * 33;
        gp.npc[3].worldY = gp.tileSize * 40;
        
        gp.npc[4] = new MonsterGirl(gp);
        gp.npc[4].worldX = gp.tileSize * 44;
        gp.npc[4].worldY = gp.tileSize * 44;

        gp.npc[5] = new Monster(gp);
        gp.npc[5].worldX = gp.tileSize * 54;
        gp.npc[5].worldY = gp.tileSize * 32;
        
        gp.npc[6] = new MonsterGirl(gp);
        gp.npc[6].worldX = gp.tileSize * 62;
        gp.npc[6].worldY = gp.tileSize * 31;
        
        gp.npc[7] = new MonsterGirl(gp);
        gp.npc[7].worldX = gp.tileSize * 60;
        gp.npc[7].worldY = gp.tileSize * 27;
        
        gp.npc[8] = new MonsterGirl(gp);
        gp.npc[8].worldX = gp.tileSize * 33;
        gp.npc[8].worldY = gp.tileSize * 28;
        
        gp.npc[9] = new Monster(gp);
        gp.npc[9].worldX = gp.tileSize * 57;
        gp.npc[9].worldY = gp.tileSize * 41;
        
        gp.npc[10] = new Monster(gp);
        gp.npc[10].worldX = gp.tileSize * 43;
        gp.npc[10].worldY = gp.tileSize * 45; */
        
    	
        
    }
    
    
    //map 2 monster

    public void setMap2NPC() {
        // Clear any leftover NPCs just in case
        for(int i = 0; i < gp.npc.length; i++) {
            gp.npc[i] = null;
        }

        gp.npc[0] = new MonsterOrc(gp);
        gp.npc[0].worldX = gp.tileSize * 48; // Place him near the end or center
        gp.npc[0].worldY = gp.tileSize * 30;
        
        gp.npc[1] = new MonsterOrc(gp);
        gp.npc[1].worldX = gp.tileSize * 48; 
        gp.npc[1].worldY = gp.tileSize * 62;
    }
    
    
 // Inside AssetSetter.java

    public void setMap2Object() {
        
        // 1. CLEAR Map 1 objects
        for(int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }

        // 2. PLACE Map 2 objects
        // Example: A Key at Column 15, Row 20
        gp.obj[0] = new OBJ_Card(gp);
        gp.obj[0].worldX = 56 * gp.tileSize;
        gp.obj[0].worldY = 35 * gp.tileSize;
        
        gp.obj[1] = new OBJ_Card(gp);
        gp.obj[1].worldX = 42 * gp.tileSize;
        gp.obj[1].worldY = 53 * gp.tileSize;
        
        gp.obj[2] = new OBJ_Card(gp);
        gp.obj[2].worldX = 36 * gp.tileSize;
        gp.obj[2].worldY = 37 * gp.tileSize;

        gp.obj[3] = new OBJ_Gate(gp);
        gp.obj[3].worldX = 48 * gp.tileSize;
        gp.obj[3].worldY = 29 * gp.tileSize;
        
        gp.obj[4] = new OBJ_Gate(gp);
        gp.obj[4].worldX = 49 * gp.tileSize;
        gp.obj[4].worldY = 29 * gp.tileSize;
        
        gp.obj[5] = new OBJ_Gate(gp);
        gp.obj[5].worldX = 49 * gp.tileSize;
        gp.obj[5].worldY = 63 * gp.tileSize;
        
        gp.obj[6] = new OBJ_Gate(gp);
        gp.obj[6].worldX = 48 * gp.tileSize;
        gp.obj[6].worldY = 63 * gp.tileSize;
     
    }
    
    
    
}
