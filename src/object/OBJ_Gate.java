package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Gate extends SuperObject {

    public OBJ_Gate(GamePanel gp) {
        name = "Gate";
        collision = true;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/gate.png"));
            // uTool.scaleImage handles the 16x16 to 48x48 conversion
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Solid area matches the 16x16 base (scaled to 48x48)
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}