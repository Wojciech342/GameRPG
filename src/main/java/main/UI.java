package main;

import java.awt.*;
import java.util.ArrayList;

import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import entity.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;

public class UI { // UI - User interface

    GamePanel gp;
    Graphics2D g2;
    public Font purisaB;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; //  0: the first screen, 1: the second screen
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int counter = 0;
    public Entity npc;
    int charIndex = 0;
    String combinedText = "";

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
        Entity bronzeCoin = new OBJ_Coin_Bronze(gp);
        coin = bronzeCoin.down1;
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(purisaB);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.white);

        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        // PLAY STATE
        else if(gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMonsterLife();
            drawMessage();
        }
        // PAUSE STATE
        else if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        // DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }
        // CHARACTER STATE
        if(gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        // OPTIONS STATE
        if(gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }
        // GAME OVER STATE
        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        // TRANSITION STATE
        if(gp.gameState == gp.transitionState) {
            drawTransition();
        }
        // TRADE STATE
        if(gp.gameState == gp.tradeState) {
            drawTradeScreen();
        }
        // SLEEP STATE
        if(gp.gameState == gp.sleepState) {
            drawSleepScreen();
        }
    }
    public void drawPlayerLife() {

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;
        int iconSize = 32;
        int manaStartX = (gp.tileSize/2) - 5;
        int manaStartY = 0;

        // DRAW MAX LIFE
        while(i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, iconSize, iconSize, null);
            i++;
            x += iconSize;
            manaStartY = y + 32;

            if(i % 8 == 0) {
                x = gp.tileSize/2;
                y += iconSize;
            }
        }

        // RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        // DRAW CURRENT HEART
        while(i < gp.player.life) {
            // So here the left half of the heart will be always drawn, and the full heart will be drawn on top if the life is there
            g2.drawImage(heart_half, x, y, iconSize, iconSize, null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_full, x, y, iconSize, iconSize, null);
            }
            i++;
            x += iconSize;

            if(i % 16 == 0) {
                x = gp.tileSize/2;
                y += iconSize;
            }
        }

        // DRAW MAX MANA
        x = manaStartX;
        y = manaStartY;
        i = 0;
        while(i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, iconSize, iconSize, null);
            i++;
            x += iconSize - 5;

            if(i % 9 == 0) {
                x = manaStartX;
                y += iconSize;
            }
        }

        // DRAW MANA
        x = manaStartX;
        y = manaStartY;
        i = 0;
        while(i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, iconSize, iconSize, null);
            i++;
            x += iconSize - 5;

            if(i % 9 == 0) {
                x = manaStartX;
                y += iconSize;
            }
        }
    }
    public void drawMonsterLife() {

        for(int i = 0; i < gp.monster[1].length; i++) {

            Entity monster = gp.monster[gp.currentMap][i];
            if(monster != null && monster.inCamera() == true) {
                if(monster.hpBarOn == true && monster.boss == false) {

                    double oneScale = (double)gp.tileSize/monster.maxLife;
                    double hpBarValue = oneScale*monster.life;

                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(monster.getScreenX()-1, monster.getScreenY() - 16, gp.tileSize+2, 12);

                    g2.setColor(new Color(255,0,30));
                    g2.fillRect(monster.getScreenX(), monster.getScreenY() - 15, (int)hpBarValue, 10);

                    monster.hpBarCounter++;

                    if(monster.hpBarCounter > 300) {
                        monster.hpBarOn = false;
                        monster.hpBarCounter = 0;
                    }
                }
                else if(monster.boss == true) {
                    double oneScale = (double)gp.tileSize*8/monster.maxLife;
                    double hpBarValue = oneScale*monster.life;

                    int x = gp.screenWidth/2 - gp.tileSize*4;
                    int y = gp.tileSize*10; // Constant hp bar coordinates for boss

                    g2.setColor(new Color(35,35,35)); // hp container for red bar
                    g2.fillRect(x-1, y-1, gp.tileSize*8 + 2, 22);

                    g2.setColor(new Color(255,0,30)); // hp red bar
                    g2.fillRect(x, y, (int)hpBarValue, 20);

                    g2.setFont(g2.getFont().deriveFont(Font.BOLD,24f));
                    g2.setColor(Color.white);
                    g2.drawString(monster.name, x + 4, y - 10);
                }
            }
        }
    }
    public void drawMessage() {
        int messageX = gp.tileSize/2 - 5;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));

        for(int i = 0; i < message.size(); i++) {

            if(message.get(i) != null) {

                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; // messageCounter++
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 40;

                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }


    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen() {
        // WINDOW
        int x = gp.tileSize*3;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*6);
        int height = gp.tileSize*4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 23F));
        x += gp.tileSize;
        y += gp.tileSize;

        if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {
            //currentDialogue = npc.dialogues[npc.dialogueSet][npc.dialogueIndex];

            char[] characters = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();

            if(charIndex < characters.length) {

                gp.playSE(17);
                String s  = String.valueOf(characters[charIndex]);
                combinedText = combinedText + s;
                currentDialogue = combinedText;

                charIndex++;
            }

            if(gp.keyH.enterPressed) {
                charIndex = 0;
                combinedText = "";

                if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutSceneState) {
                    npc.dialogueIndex++;
                    gp.keyH.enterPressed = false;
                }
            }
        }
        else { // If no text is in the array
            npc.dialogueIndex = 0;
            if(gp.gameState == gp.dialogueState) { // Needed because of the trade state
                gp.gameState = gp.playState;
            }
            if(gp.gameState == gp.cutSceneState) { // after everything is said go to the next scene
                gp.csManager.scenePhase++;
            }
        }

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y); // g2.drawString ignores '\n' by default
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0,0,0,210); // BLACK, transparency
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255); // WHITE
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width-10, height-10, 25, 25);
    }
    public int getXForCenteredText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
    public int getXForAlignToRightText(String text, int tailX) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }

}