package main;

import java.awt.Rectangle;

import entity.Entity;
import object.OBJ_Key;
import object.OBJ_NormalDoor;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
        
    }
    
 // Check if player is near an entity (without moving)
    public boolean checkPlayerNearby(Entity entity) {
        Rectangle playerRect = new Rectangle(
            gp.player.worldX + gp.player.solidArea.x,
            gp.player.worldY + gp.player.solidArea.y,
            gp.player.solidArea.width,
            gp.player.solidArea.height
        );

        Rectangle entityRect = new Rectangle(
            entity.worldX + entity.solidArea.x,
            entity.worldY + entity.solidArea.y,
            entity.solidArea.width,
            entity.solidArea.height
        );

        return playerRect.intersects(entityRect);
    }

    
    // ================= TILE COLLISION =================
    public void checkTile(Entity entity) {

        int leftX   = entity.worldX + entity.solidArea.x;
        int rightX  = leftX + entity.solidArea.width;
        int topY    = entity.worldY + entity.solidArea.y;
        int bottomY = topY + entity.solidArea.height;

        int leftCol   = leftX / gp.tileSize;
        int rightCol  = rightX / gp.tileSize;
        int topRow    = topY / gp.tileSize;
        int bottomRow = bottomY / gp.tileSize;

        int tile1, tile2;

        switch (entity.direction) {

            case "up":
                topRow = (topY - entity.speed) / gp.tileSize;
                tile1 = gp.tileM.mapTileNum[leftCol][topRow];
                tile2 = gp.tileM.mapTileNum[rightCol][topRow];
                if (gp.tileM.tile[tile1].collision || gp.tileM.tile[tile2].collision)
                    entity.collisionOn = true;
                break;

            case "down":
                bottomRow = (bottomY + entity.speed) / gp.tileSize;
                tile1 = gp.tileM.mapTileNum[leftCol][bottomRow];
                tile2 = gp.tileM.mapTileNum[rightCol][bottomRow];
                if (gp.tileM.tile[tile1].collision || gp.tileM.tile[tile2].collision)
                    entity.collisionOn = true;
                break;

            case "left":
                leftCol = (leftX - entity.speed) / gp.tileSize;
                tile1 = gp.tileM.mapTileNum[leftCol][topRow];
                tile2 = gp.tileM.mapTileNum[leftCol][bottomRow];
                if (gp.tileM.tile[tile1].collision || gp.tileM.tile[tile2].collision)
                    entity.collisionOn = true;
                break;

            case "right":
                rightCol = (rightX + entity.speed) / gp.tileSize;
                tile1 = gp.tileM.mapTileNum[rightCol][topRow];
                tile2 = gp.tileM.mapTileNum[rightCol][bottomRow];
                if (gp.tileM.tile[tile1].collision || gp.tileM.tile[tile2].collision)
                    entity.collisionOn = true;
                break;
        }
    }

    // ================= OBJECT COLLISION =================
    public int checkObject(Entity entity, boolean player) {

        int index = 999;
        Rectangle entityRect = getNextRect(entity);

        for (int i = 0; i < gp.obj.length; i++) {

            if (gp.obj[i] == null) continue;

            Rectangle objRect = new Rectangle(
                    gp.obj[i].worldX + gp.obj[i].solidArea.x,
                    gp.obj[i].worldY + gp.obj[i].solidArea.y,
                    gp.obj[i].solidArea.width,
                    gp.obj[i].solidArea.height
            );

            if (entityRect.intersects(objRect)) {

                // ✅ PLAYER opens NormalDoor automatically
                if (player && gp.obj[i] instanceof OBJ_NormalDoor) {
                    ((OBJ_NormalDoor) gp.obj[i]).open();
                }

                // ✅ COLLISION RULES
                if (gp.obj[i].collision) {

                    // Player collides with all solid objects
                    if (player) {
                        entity.collisionOn = true;
                    }
                    // Monster collides with everything EXCEPT keys
                    else if (!(gp.obj[i] instanceof OBJ_Key)) {
                        entity.collisionOn = true;
                    }
                }

                if (player) {
                    index = i;
                }
            }
        }
        return index;
    }

    // ================= ENTITY COLLISION =================
    public int checkEntity(Entity entity, Entity[] target) {

        int index = 999;
        Rectangle entityRect = getNextRect(entity);

        for (int i = 0; i < target.length; i++) {

            if (target[i] == null) continue;

            Rectangle targetRect = new Rectangle(
                    target[i].worldX + target[i].solidArea.x,
                    target[i].worldY + target[i].solidArea.y,
                    target[i].solidArea.width,
                    target[i].solidArea.height
            );

            if (entityRect.intersects(targetRect)) {
                entity.collisionOn = true;
                index = i;
                break;
            }
        }
        return index;
    }

    // ================= PLAYER TOUCH (MONSTER) =================
    public boolean checkPlayer(Entity entity) {

        Rectangle entityRect = getNextRect(entity);
        Rectangle playerRect = new Rectangle(
                gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );

        return entityRect.intersects(playerRect);
    }

    // ================= HELPER =================
    private Rectangle getNextRect(Entity entity) {

        int x = entity.worldX + entity.solidArea.x;
        int y = entity.worldY + entity.solidArea.y;

        switch (entity.direction) {
            case "up":    y -= entity.speed; break;
            case "down":  y += entity.speed; break;
            case "left":  x -= entity.speed; break;
            case "right": x += entity.speed; break;
        }

        return new Rectangle(x, y, entity.solidArea.width, entity.solidArea.height);
    }
}
