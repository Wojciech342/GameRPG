package main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import ai.PathFinder;
import config.Config;
import data.SaveLoad;

import java.util.ArrayList;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import java.util.Collections;
import java.util.Comparator;


public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;
    public final int maxMap = 10;
    public int currentMap = 0;

    // FOR FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;

    // FPS
    final int FPS = 60;

    // SYSTEM
    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public Sound music = new Sound();
    public Sound se = new Sound(); // se - sound effect
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);
    Config config = new Config(this);
    public PathFinder pathFinder = new PathFinder(this);
    public EnvironmentManager environmentManager = new EnvironmentManager(this);
    Map map = new Map(this);
    SaveLoad saveLoad = new SaveLoad(this);
    public EntityGenerator entityGenerator = new EntityGenerator(this);
    public CutSceneManager cutSceneManager = new CutSceneManager(this);
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyHandler);
    public Entity[][] objects = new Entity[maxMap][20]; // That means we can display up to 20 objects at the same time
    public Entity[][] npcs = new Entity[maxMap][10];
    public Entity[][] monsters = new Entity[maxMap][20];
    public InteractiveTile[][] interactiveTiles = new InteractiveTile[maxMap][50];
    public Entity[][] projectiles = new Entity[maxMap][50];
    public ArrayList<Entity> particles = new ArrayList<>();
    public ArrayList<Entity> entities = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int sleepState = 9;
    public final int mapState = 10;
    public final int cutSceneState = 11;

    // OTHERS
    public boolean bossBattleOn = false;

    // AREA
    public int currentArea;
    public int nextArea;
    public final int outside = 50;
    public final int indoor = 51;
    public final int dungeon = 52;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // Improve game rendering performance
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // With this GamePanel can be "focused" to receive key input
    }

    public void setupGame() {
        assetSetter.placeObjectsOnMap();
        assetSetter.placeNPCsOnMap();
        assetSetter.placeMonstersOnMap();
        assetSetter.setInteractiveTile();
        environmentManager.setup();

        gameState = titleState;
        currentArea = outside;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        if(fullScreenOn) {
            setFullScreen();
        }
        
    }
    public void resetGame(boolean restart) {
        stopMusic();
        removeTempEntity();
        bossBattleOn = false;
        player.setDefaultPosition();
        player.restoreStatus();
        player.resetCounter();
        assetSetter.placeNPCsOnMap();
        assetSetter.placeMonstersOnMap();

        if(restart) {
            player.setDefaultValues();
            assetSetter.placeObjectsOnMap();
            assetSetter.setInteractiveTile();
            environmentManager.lighting.resetDay();
        }
       
    }
    public void setFullScreen() {
        // GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        // GET FULL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
    
            if(delta >= 1) {
                update();
                //repaint();
                drawToTempScreen(); // draw everything to the buffered image
                drawToScreen(); // draw the buffered image to the screen
                delta--;
            }
        }
    }
    public void update() {
        if(gameState == playState){
            // PLAYER
            player.update();
            // NPC
            for(int i = 0; i < npcs[1].length; i++) {
                if(npcs[currentMap][i] != null) {
                    npcs[currentMap][i].update();
                }
            }
            // MONSTER
            for(int i = 0; i < monsters[1].length; i++) {
                if(monsters[currentMap][i] != null) {
                    if(monsters[currentMap][i].alive && !monsters[currentMap][i].dying) {
                        monsters[currentMap][i].update();
                    }
                    if(!monsters[currentMap][i].alive) {
                        monsters[currentMap][i].checkDrop();
                        monsters[currentMap][i] = null;
                    }
                }
            }
            // PROJECTILE
            for(int i = 0; i < projectiles[1].length; i++) {
                if(projectiles[currentMap][i] != null) {
                    if(projectiles[currentMap][i].alive) {
                        projectiles[currentMap][i].update();
                    }
                    else if(!projectiles[currentMap][i].alive) {
                        projectiles[currentMap][i] = null;
                    }
                }
            }
            // PARTICLE
            for(int i = 0; i < particles.size(); i++) {
                if(particles.get(i) != null) {
                    if(particles.get(i).alive) {
                        particles.get(i).update();
                    }
                    else if(!particles.get(i).alive) {
                        particles.remove(i);
                    }
                }
            }
            // INTERACTIVE TILE
            for(int i = 0; i < interactiveTiles[1].length; i++) { // length of the second dimension
                if(interactiveTiles[currentMap][i] != null) {
                    interactiveTiles[currentMap][i].update();
                }
            }
            environmentManager.update();
        }
        if(gameState == pauseState){

        } 
    }
    public void drawToTempScreen() {
        // DEBUG
        long drawStart = 0;
        if(keyHandler.showDebugText) {
            drawStart = System.nanoTime();
        }

        // Clear the screen from the previous frame by filling it with a background color
        g2.setColor(Color.black);  // Or any other background color
        g2.fillRect(0, 0, screenWidth2, screenHeight2);  // Clear the entire screen
            
        // TITLE SCREEN
        if(gameState == titleState) {
            ui.draw(g2);
        }
        // MAP SCREEN
        else if(gameState == mapState) {
            map.drawFullMapScreen(g2);
        }
        // OTHERS
        else {
            // TILE
            tileManager.draw(g2);

            // INTERACTIVE TILE
            for(int i = 0; i < interactiveTiles[1].length; i++) {
                if(interactiveTiles[currentMap][i] != null) {
                    interactiveTiles[currentMap][i].draw(g2);
                }
            }

            // ADD ENTITIES TO THE LIST
            entities.add(player);

            for(int i = 0; i < npcs[1].length; i++) {
                if(npcs[currentMap][i] != null) {
                    entities.add(npcs[currentMap][i]);
                }
            }
            for(int i = 0; i < objects[1].length; i++) {
                if(objects[currentMap][i] != null) {
                    entities.add(objects[currentMap][i]);
                }
            }
            for(int i = 0; i < monsters[1].length; i++) {
                if(monsters[currentMap][i] != null) {
                    entities.add(monsters[currentMap][i]);
                }
            }
            for(int i = 0; i < projectiles[1].length; i++) {
                if(projectiles[currentMap][i] != null) {
                    entities.add(projectiles[currentMap][i]);
                }
            }
            for(int i = 0; i < particles.size(); i++) {
                if(particles.get(i) != null) {
                    entities.add(particles.get(i));
                }
            }
            

            // SORT - we sort it so that it displays the correct way, lower entity is above the upper one (in layers)
            Collections.sort(entities, new Comparator<Entity>() {

                @Override
                public int compare(Entity e1, Entity e2) {
                    
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
                
            });

            // DRAW ENTITIES
            for(int i = 0; i < entities.size(); i++) {
                entities.get(i).draw(g2);
            }
            // EMPTY ENTITY LIST
            entities.clear();

            // ENVIRONMENT
            environmentManager.draw(g2);

            // MINI MAP
            map.drawMinimap(g2);

            // CUTSCENE
            cutSceneManager.draw(g2);
            
            // UI - User interface
            ui.draw(g2);
        }

        //DEBUG
        if(keyHandler.showDebugText){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial",Font.PLAIN,20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX" + player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY" + player.worldY, x, y); y += lineHeight;
            g2.drawString("Col" + (player.worldX + player.solidArea.x) /tileSize, x, y); y += lineHeight;
            g2.drawString("Row" + (player.worldY + player.solidArea.y) /tileSize, x, y); y += lineHeight;
            g2.drawString("God Mode:" + keyHandler.godModeOn, x, y); y += lineHeight;

            g2.drawString("Draw Time: " + passed, x, y);
        }
    }
    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSE(int i) { // SE - sound effect
        se.setFile(i);
        se.play();
    }
    public void changeArea() {
        if(nextArea != currentArea) {
            stopMusic();

            if(nextArea == outside) {
                playMusic(0);
            }
            else if(nextArea == indoor) {
                playMusic(18);
            }
            else if(nextArea == dungeon) {
                playMusic(19);
            }

            assetSetter.placeNPCsOnMap(); // It resets all NPCS (we do it mostly for rocks so it can be changed)
        }

        currentArea = nextArea;
        //aSetter.setMonster();
    }
    public void removeTempEntity() {
        for(int mapNum = 0; mapNum < maxMap; mapNum++) {
            for(int i = 0; i < objects[1].length; i++) {
                if(objects[mapNum][i] != null && objects[mapNum][i].temp) {
                    objects[mapNum][i] = null;
                }
            }
        }
    }
}