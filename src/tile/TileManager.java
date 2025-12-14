package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	ArrayList<String> fileNames = new ArrayList<>();
	ArrayList<String> collisionStatus = new ArrayList<>();
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		
		//READ TILE DATA FILE
		InputStream is = getClass().getResourceAsStream("/maps/datatile.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// GETTING TILE NAMES AND COLLISION INFO FROM THE FILE
		String line;

		try {
		    while ((line = br.readLine()) != null) {

		        fileNames.add(line);  // tile image filename

		        String collisionLine = br.readLine(); // read next line
		        collisionStatus.add(collisionLine);   // “true” or “false”
		    }
		    br.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}

		
		
		//INITIALIZE THE TILE ARRAY BASED ON THE FILENAMES SIZE
		tile = new Tile[fileNames.size()];
		getTileImage();
		
		//GET THE maxWorldCol & Row
		is = getClass().getResourceAsStream("/maps/map_101.txt");
		br = new BufferedReader(new InputStreamReader(is));
		
		try {
			String line2 = br.readLine();
			String maxTile[] = line2.split(" ");
			
			
			gp.maxWorldCol = maxTile.length;
			gp.maxWorldRow = maxTile.length;
			mapTileNum = new int[gp.maxWorldCol] [gp.maxWorldRow];
			
			br.close();
			
		}catch(IOException e) {
			System.out.println("Exception");
		}
		
		loadMap("/maps/map_101.txt");
		
		

		
		////getTileImage();
		///loadMap("/maps/worldmap101.txt");
		
	}
	
	public void loadMap(String filePath) {

	    try {
	        InputStream is = getClass().getResourceAsStream(filePath);
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));

	        int col = 0;
	        int row = 0;

	        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

	            String line = br.readLine();

	            while (col < gp.maxWorldCol) {

	                String numbers[] = line.split(" ");

	                int num = Integer.parseInt(numbers[col]);

	                mapTileNum[col][row] = num;
	                col++;
	            }

	            if (col == gp.maxWorldCol) {
	                col = 0;
	                row++;
	            }
	        }

	        br.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	//GOES THROUGH WALL OR NOT
	public void getTileImage() {
		
		
		for(int i = 0; i< fileNames.size(); i++) {
			String fileName;
			boolean collision;
			
			//GET FILE NAME
			fileName = fileNames.get(i);
			
			//get a collsion status
			if(collisionStatus.get(i).equals("true")) {
				collision = true;
				
			}
			else {
				collision = false;
			}
			setup(i,fileName, collision);
		}
			
			//setup(0, "wall_1", true);
			//setup(1, "cream_tile",false);
			//setup(3,"pillar",false);
			//setup(5, "maroon_left", false);
			//setup(0, "maroon_right", false);
			
			
		
					
	}	
	public void setup(int index, String imageName, boolean collision) {
		
		UtilityTool uTool = new UtilityTool();
		
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+ imageName));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
			
		}catch(IOException e) {
			e.printStackTrace();
			
		}
		
	}
	public void draw(Graphics2D g2) {
		
	int worldCol = 0;
	int worldRow = 0;
	
	
	while(worldCol< gp.maxWorldCol &&  worldRow < gp.maxWorldRow) {
		
		int tileNum = mapTileNum[worldCol][ worldRow];
		
		int worldX = worldCol * gp.tileSize;
		int worldY = worldRow * gp.tileSize;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
		
			g2.drawImage(tile[tileNum].image,screenX, screenY, null);
		}		
		
	
		worldCol++;
		
		if(worldCol == gp.maxWorldCol) {
			worldCol = 0;	
			 worldRow++;
			
		}
						
      }
	
	}
		
  }
	


	
































