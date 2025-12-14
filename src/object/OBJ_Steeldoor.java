package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Steeldoor extends SuperObject{
	
	GamePanel gp;
	 public OBJ_Steeldoor(GamePanel gp){
		
		 this.gp = gp;
		name = "Steeldoor";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/steeldoor.png"));
			uTool.scaleImage(image, gp.tileSize, gp.tileSize);
			
		}catch(IOException e) {
			e.printStackTrace();
			
		}
	}

}
