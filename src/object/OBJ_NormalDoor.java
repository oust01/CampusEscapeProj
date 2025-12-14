package object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_NormalDoor extends SuperObject {

    GamePanel gp;

    private BufferedImage closedImage;
    private BufferedImage openImage;

    public boolean isOpen = false;

    public OBJ_NormalDoor(GamePanel gp) {
        this.gp = gp;
        name = "NormalDoor";
        collision = true;

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        try {
            closedImage = ImageIO.read(getClass().getResourceAsStream("/objects/normaldoor.png"));
            openImage   = ImageIO.read(getClass().getResourceAsStream("/objects/normaldoor_open.png"));

            image = closedImage; // default
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open() {
        if (!isOpen) {
            isOpen = true;
            image = openImage;
            collision = false; // allow player to pass
            gp.playSE(4);
        }
    }
}
