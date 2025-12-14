package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    // ===== SCREEN =====
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // ===== WORLD =====
    public int maxWorldCol;
    public int maxWorldRow;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;

    // ===== SYSTEM =====
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    // ===== ENTITIES =====
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[50];
    public Entity npc[] = new Entity[20];

    // ===== GAME STATES =====
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 3;

    public int gameState;

    // ===== CONSTRUCTOR =====
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    // ===== SETUP =====
    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();

        ui.showStartHint = true;
        ui.startHintCounter = 0;
        ui.playTime = 0;
        ui.messageOn = false;
        ui.message = "";
        ui.messageCounter = 0;
        ui.gameFinished = false;
        ui.timeUp = false;

        player.hasKey = 0;
        player.setDefaultValues();

        playMusic(0);
        gameState = playState;
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    // ===== GAME LOOP =====
    @Override
    public void run() {

        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    // ===== UPDATE =====
    public void update() {

        // ===== TITLE INPUT =====
        if (gameState == titleState) {
            if (keyH.enterPressed) {
                keyH.enterPressed = false;
                setupGame();
            }
            return;
        }

        // ===== GAME OVER INPUT =====
        if (gameState == gameOverState) {

            if (keyH.rPressed) {
                keyH.rPressed = false;
                setupGame();
            }
            if (keyH.qPressed) {
                System.exit(0);
            }
            return;
        }

        // ===== PLAY =====
        if (gameState == playState) {
        	
        	// ===== TIME UP =====
        	if (ui.timeUp) {
        	    gameState = gameOverState;
        	    stopMusic();
        	    playSE(5); // gameover.wav
        	    return;
        	}
       	

            player.update();

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) npc[i].update();
            }

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();

                    //  MONSTER TOUCH PLAYER = GAME OVER
                    if (npc[i].type == 1 && cChecker.checkPlayer(npc[i])) {
                        gameState = gameOverState;
                        ui.timeUp = true;

                        stopMusic();
                        playSE(5); //  gameover.wav
                        return;
                    }
                }
            }


        }
    }

    // ===== DRAW =====
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == titleState) {
            ui.draw(g2);
            g2.dispose();
            return;
        }

        tileM.draw(g2);

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) obj[i].draw(g2, this);
        }

        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) npc[i].draw(g2);
        }

        player.draw(g2);
        ui.draw(g2);

        g2.dispose();
    }

    // ===== SOUND =====
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
