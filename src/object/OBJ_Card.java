package object;

import main.GamePanel;

public class OBJ_Card extends SuperObject {
    public OBJ_Card(GamePanel gp) {
        name = "Card";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/card.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}