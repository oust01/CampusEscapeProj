package object;

import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Key extends SuperObject{
	
	GamePanel gp;
	 public OBJ_Key(GamePanel gp){
		
		 this.gp = gp;
		name = "Key";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
			 solidArea = new Rectangle(8, 16, 32, 32); // Set size of collision area.
		        solidAreaDefaultX = solidArea.x;
		        solidAreaDefaultY = solidArea.y;
			uTool.scaleImage(image, gp.tileSize, gp.tileSize);
			
		}catch(IOException e) {
			e.printStackTrace();
			
		}
	}

}
