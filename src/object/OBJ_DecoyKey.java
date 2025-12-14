package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_DecoyKey extends SuperObject{
	
	GamePanel gp;
	 public OBJ_DecoyKey(GamePanel gp){
		
		 this.gp = gp;
		name = "DecoyKey";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/decoy_key.png"));
			uTool.scaleImage(image, gp.tileSize, gp.tileSize);
			
		}catch(IOException e) {
			e.printStackTrace();
			
		}
	}

}
