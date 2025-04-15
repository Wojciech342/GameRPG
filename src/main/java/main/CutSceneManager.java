package main;

import data.Progress;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;

import entity.PlayerDummy;
import monster.MON_SkeletonLord;
import object.OBJ_BlueHeart;
import object.OBJ_Door_Iron;

public class CutSceneManager {
    
    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredit;

    // Scene Number
    public final int NA = 0; // Not Applicable
    public final int skeletonLord = 1;
    public final int ending = 2;

    public CutSceneManager(GamePanel gp) {
        this.gp = gp;

        endCredit = "End credit text to be changed\n" 
            + "End credit text to be changed\n"
            + "\n\n\n\n\n\n\n\n\n\n\n\n\n"
            + "End credit text to be changed\n"
            + "End credit text to be changed\n"
            + "End credit text to be changed\n"
            + "End credit text to be changed\n"
            + "End credit text to be changed\n"
            + "End credit text to be changed\n";
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        switch(sceneNum) {
        case skeletonLord: scene_skeletonLord(); break;
        case ending: scene_ending(); break;
        }
    }
    public void scene_skeletonLord() {

        if(scenePhase == 0) {

            gp.bossBattleOn = true;

            // Shut the iron door
            for(int i = 0; i < gp.objects[1].length; i++) {

                if(gp.objects[gp.currentMap][i] == null) {
                    gp.objects[gp.currentMap][i] = new OBJ_Door_Iron(gp);
                    gp.objects[gp.currentMap][i].worldX = gp.tileSize*25;
                    gp.objects[gp.currentMap][i].worldY = gp.tileSize*28;
                    gp.objects[gp.currentMap][i].temp = true; // delete after the boss fight
                    gp.playSE(21);
                    break;
                }
            }
            // Search a vacant slot for the dummy
            for(int i = 0; i < gp.npcs[1].length; i++) {

                if(gp.npcs[gp.currentMap][i] == null) {
                    gp.npcs[gp.currentMap][i] = new PlayerDummy(gp);
                    gp.npcs[gp.currentMap][i].worldX = gp.player.worldX;
                    gp.npcs[gp.currentMap][i].worldY = gp.player.worldY;
                    gp.npcs[gp.currentMap][i].direction= gp.player.direction;
                    break;
                }
            }

            gp.player.drawing = false;
            scenePhase++;
        }
        else if(scenePhase == 1) {

            gp.player.worldY -= 2;

            if(gp.player.worldY < gp.tileSize*16) {
                scenePhase++;
            }
        }
        else if(scenePhase == 2) {
            // Search the boss
            for(int i = 0; i < gp.npcs[1].length; i++) {

                if(gp.monsters[gp.currentMap][i] != null && gp.monsters[gp.currentMap][i]instanceof MON_SkeletonLord) {
                    gp.monsters[gp.currentMap][i].sleep = false;
                    gp.ui.npc = gp.monsters[gp.currentMap][i];
                    scenePhase++;
                    break;
                }
            }
        }
        else if(scenePhase == 3) {
            // The boss speaks
            gp.ui.drawDialogueScreen();
        }
        else if(scenePhase == 4) {
            // Search the dummy
            for(int i = 0; i < gp.npcs[1].length; i++) {
                if(gp.npcs[gp.currentMap][i] != null && gp.npcs[gp.currentMap][i]instanceof PlayerDummy) {
                    // Restore the player position
                    gp.player.worldX = gp.npcs[gp.currentMap][i].worldX;
                    gp.player.worldY = gp.npcs[gp.currentMap][i].worldY;
                    // Delete the dummy
                    gp.npcs[gp.currentMap][i] = null;
                    break;
                }
            }

            // Start drawing the player
            gp.player.drawing = true;

            // Reset
            sceneNum = NA;
            scenePhase = 0;
            gp.gameState = gp.playState;

            // Change the music
            gp.stopMusic();
            gp.playMusic(22);
        }
    }
    public void scene_ending() {
        if(scenePhase == 0) {

            gp.stopMusic();
            gp.ui.npc = new OBJ_BlueHeart(gp);
            scenePhase++;
        }
        else if(scenePhase == 1) {
            // Display dialogues
            gp.ui.drawDialogueScreen();
        }
        else if(scenePhase == 2) {
            // Play the fanfare
            gp.playSE(4);
            scenePhase++;
        }
        else if(scenePhase == 3) {
            // Wait until the sound effect ends
            if(counterReached(300)) {
                scenePhase++;
            }
        }
        else if(scenePhase == 4) {
            // The screen gets darker
            alpha += 0.005;
            if(alpha > 1f) {
                alpha = 1f;
            }
            drawBlackBackground(alpha);

            if(alpha == 1) {
                alpha = 0;
                scenePhase++;
            }
        }
        else if(scenePhase == 5) {
            drawBlackBackground(1f);
            
            alpha += 0.005;
            if(alpha > 1f) {
                alpha = 1f;
            }

            String text = "After the fierce battle with the Skeleton Lord \n"
                + "The boy finally won.";
            
            drawString(alpha, 30f, 200, text, 70);

            if(counterReached(300)){
                gp.playMusic(0);
                scenePhase++;
            }
        }
        else if(scenePhase == 6) {
            drawBlackBackground(1f);

            drawString(1f, 100f, gp.screenHeight/2 - 100, "Blue Boy\n Adventure", 150);

            if(counterReached(300)){
                scenePhase++;
            }
        }
        else if(scenePhase == 7) {
            drawBlackBackground(1f);

            y = gp.screenHeight/2;
            drawString(1f, 30f, y, endCredit, 40);

            if(counterReached(300)) {
                scenePhase++;
            }
        }
        else if(scenePhase == 8) {
            drawBlackBackground(1f);
            // Scrolling the credit
            y--;
            drawString(1f, 30f, y, endCredit, 40);
            if(y < -500) {
                scenePhase = 0;
                sceneNum = 0;
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum = 0;
                gp.gameState = gp.titleState;
                Progress.skeletonLordDefeated = false;
                gp.resetGame(true);
                gp.stopMusic();
            }
        }
        
    }
    public boolean counterReached(int target) {
        boolean counterReached = false;

        counter++;
        if(counter > target) {
            counterReached = true;
            counter = 0;
        }

        return counterReached;
    }
    public void drawBlackBackground(float alpha) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    } 
    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(fontSize));

        for(String line : text.split("\n")) {
            int x = gp.ui.getXForCenteredText(line);
            g2.drawString(line, x, y);
            y += lineHeight;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
