package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JPanel;
import javax.swing.JFrame;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    // ===== SCREEN =====
    final int originalTileSize = 16;
    final int scale = 3;
    
 // ===== DISPLAY MODE =====
 // ===== DISPLAY MODE =====
    private boolean stretchMode = true; // F10 = true, F11 = false

    public void setStretchMode(boolean value) {
        stretchMode = value;
        requestFocusInWindow(); // 🔑 GET FOCUS BACK
        repaint();
    }



    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int helpState = 4;
    public final int aboutState = 5;

    // ===== PIXEL PERFECT RENDERING =====
    private int scaleFactor;
    private int screenX;
    private int screenY;

    // ===== FULLSCREEN =====
    public JFrame window;
    public boolean fullscreenOn = true;

    // ===== WORLD =====
    public int maxWorldCol;
    public int maxWorldRow;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;

    // ===== SYSTEM =====
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public int currentMap = 1;
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

        currentMap = 1;
        tileM.loadNewTileData("/maps/datatile.txt", "/maps/map_101.txt");

        ui.gameFinished = false;
        ui.timeUp = false;
        ui.playTime = 0;
        ui.showStartHint = true;
        ui.startHintCounter = 0;

        player.hasKey = 0;
        player.cardCount = 0;
        player.setDefaultValues();

        for (int i = 0; i < obj.length; i++) obj[i] = null;
        for (int i = 0; i < npc.length; i++) npc[i] = null;

        aSetter.setObject();
        aSetter.setNPC();

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

        while (gameThread != null) {

            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void nextLevel() {

        currentMap = 2;
        tileM.loadNewTileData("/maps/datatile101.txt", "/maps/secondmap.txt");

        for (int i = 0; i < obj.length; i++) obj[i] = null;
        for (int i = 0; i < npc.length; i++) npc[i] = null;

        player.hasKey = 0;
        player.cardCount = 0;

        player.worldX = tileSize * 49;
        player.worldY = tileSize * 44;

        aSetter.setMap2NPC();
        aSetter.setMap2Object();
        ui.showMessage("FINAL MAZE LOADED");
    }

    // ===== UPDATE =====
    public void update() {

        // ===== TITLE STATE =====
        if (gameState == titleState) {
            if (keyH.enterPressed) {
                keyH.enterPressed = false;
                setupGame();
            }
            return;
        }

        // ===== GAME OVER STATE =====
        if (gameState == gameOverState) {
            if (keyH.rPressed) {
                keyH.rPressed = false;
                setupGame();
            }
            if (keyH.qPressed) System.exit(0);
            return;
        }

        // ===== PLAY STATE =====
        if (gameState == playState) {

            // Check if time is up
            if (ui.timeUp) {
                gameState = gameOverState;
                stopMusic();
                playSE(5);
                return;
            }

            // Update player
            player.update();

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();

                    // Check if player is near NPC
                    if (npc[i].type == 0 && cChecker.checkPlayerNearby(npc[i])) {
                        if (keyH.ePressed) {
                            npc[i].speak();
                            keyH.ePressed = false;
                        }
                    }


                    // Existing monster collision/game over
                    if (npc[i].type == 1 && cChecker.checkPlayer(npc[i])) {
                        gameState = gameOverState;
                        ui.timeUp = true;
                        stopMusic();
                        playSE(5);
                        return;
                    }
                }
            }

        }

        // ===== PAUSE STATE =====
        if (gameState == pauseState) {
            // Pause logic handled in KeyHandler
        }
    }


    // ===== PIXEL PERFECT SCALING =====
    private void calculateScaling() {

        int windowWidth = getWidth();
        int windowHeight = getHeight();

        scaleFactor = Math.min(
                windowWidth / screenWidth,
                windowHeight / screenHeight
        );

        if (scaleFactor < 1) scaleFactor = 1;

        screenX = (windowWidth - screenWidth * scaleFactor) / 2;
        screenY = (windowHeight - screenHeight * scaleFactor) / 2;
    }

    // ===== DRAW =====
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (stretchMode) {
            // ===== STRETCH MODE (F10) =====
            double scaleX = (double) getWidth() / screenWidth;
            double scaleY = (double) getHeight() / screenHeight;

            g2.scale(scaleX, scaleY);
            drawGame(g2);

        } else {
            // ===== PIXEL PERFECT MODE (F11) =====
            calculateScaling();

            g2.translate(screenX, screenY);
            g2.scale(scaleFactor, scaleFactor);
            drawGame(g2);
        }

        g2.dispose();
    }
    
    
    private void drawGame(Graphics2D g2) {

        if (gameState == titleState) {
            ui.draw(g2);
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
    }




    // ===== FULLSCREEN TOGGLE =====
    public void toggleFullscreen() {

        GraphicsDevice gd = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        fullscreenOn = !fullscreenOn;
        window.dispose();

        if (fullscreenOn) {
            window.setUndecorated(true);
            gd.setFullScreenWindow(window);
        } else {
            gd.setFullScreenWindow(null);
            window.setUndecorated(false);
            window.setSize(screenWidth, screenHeight);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        }
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
