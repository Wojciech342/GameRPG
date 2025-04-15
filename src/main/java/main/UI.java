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

        for(int i = 0; i < gp.monsters[1].length; i++) {

            Entity monster = gp.monsters[gp.currentMap][i];
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
    public void drawTitleScreen() {

        if(titleScreenState == 0) {
            g2.setColor(new Color(0,0,0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            // TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 65F));
            String text = "Blue Boy Adventure";
            int x = getXForCenteredText(text);
            int y = gp.tileSize*3;

            // SHADOW
            g2.setColor(Color.gray);
            g2.drawString(text, x+3, y+3);
            // MAIN COLOR
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            // BLUE BOY IMAGE
            x = gp.screenWidth/2 - gp.tileSize*3/2;
            y += gp.tileSize;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize*3, gp.tileSize*3, null);

            // MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));

            text = "NEW GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize*4;
            g2.drawString(text, x, y);
            if(commandNum == 0) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "QUIT";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2) {
                g2.drawString(">", x-gp.tileSize, y);
            }
        }
        else if(titleScreenState == 1) {

            // CLASS SELECTION SCREEN
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Select your class!";
            int x = getXForCenteredText(text);
            int y = gp.tileSize *3;
            g2.drawString(text, x, y);

            text = "Fighter";
            x = getXForCenteredText(text);
            y += gp.tileSize *3;
            g2.drawString(text, x, y);
            if(commandNum == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Thief";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Sorcerer";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Back";
            x = getXForCenteredText(text);
            y += gp.tileSize *2;
            g2.drawString(text, x, y);
            if(commandNum == 3) {
                g2.drawString(">", x - gp.tileSize, y);
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

            if(gp.keyHandler.enterPressed) {
                charIndex = 0;
                combinedText = "";

                if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutSceneState) {
                    npc.dialogueIndex++;
                    gp.keyHandler.enterPressed = false;
                }
            }
        }
        else { // If no text is in the array
            npc.dialogueIndex = 0;
            if(gp.gameState == gp.dialogueState) { // Needed because of the trade state
                gp.gameState = gp.playState;
            }
            if(gp.gameState == gp.cutSceneState) { // after everything is said go to the next scene
                gp.cutSceneManager.scenePhase++;
            }
        }

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y); // g2.drawString ignores '\n' by default
            y += 40;
        }
    }
    public void drawCharacterScreen() {
        // CREATE A FRAME
        final int frameX = gp.tileSize*2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(24F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35; // font size + (my choice)

        // NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);

        // VALUES
        int tailX = (frameX + frameWidth) - 30;
        // Reset textY
        textY = frameY + gp.tileSize;
        String value;

        g2.setFont(g2.getFont().deriveFont(28F));

        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize + 3, textY-25, null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize + 3, textY-25, null);
    }
    public void drawInventory(Entity entity, boolean cursor) {
        int frameX;
        int frameY;
        int frameWidth;
        int frameHeight;
        int slotCol;
        int slotRow;
        if(entity == gp.player) {
            frameX = gp.tileSize*12;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize*6;
            frameHeight = gp.tileSize*5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        }
        else {
            frameX = gp.tileSize*2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize*6;
            frameHeight = gp.tileSize*5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }
        // FRAME
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // SLOT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // DRAW ENTITIE'S ITEMS
        for(int i = 0; i < entity.inventory.size(); i++) {

            // EQUIP CURSOR
            if(entity.inventory.get(i) == entity.currentWeapon ||
               entity.inventory.get(i) == entity.currentShield ||
               entity.inventory.get(i) == entity.currentLight) {
                g2.setColor(new Color(240,190,90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
               }

            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY,null);

            // DISPLAY AMOUNT
            if(entity == gp.player && entity.inventory.get(i).amount > 1) {

                g2.setFont(g2.getFont().deriveFont(26f));
                int amountX;
                int amountY;

                String s = "" + entity.inventory.get(i).amount;
                amountX = getXForAlignToRightText(s, slotX + 44);
                amountY = slotY + gp.tileSize;

                // SHADOW
                g2.setColor(new Color(60,60,60));
                g2.drawString(s, amountX, amountY);
                // NUMBER
                g2.setColor(Color.white);
                g2.drawString(s, amountX-3, amountY-3);
            }

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // CURSOR
        if(cursor == true) {
            int cursorX = slotXStart + (slotSize * slotCol);
            int cursorY = slotYStart + (slotSize * slotRow);
            int cursorWidth = gp.tileSize;
            int cursorHeight = gp.tileSize;

            // DRAW CURSOR
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            // DESCRIPTION PLAYER
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize*3;

            // DRAW DESCRIPTION TEXT
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(21F));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            if(itemIndex < entity.inventory.size()) {

                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

                for(String line : entity.inventory.get(itemIndex).description.split("\n")) {

                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }

    }
    public void drawGameOverScreen() {

        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 90f));

        text = "Game Over";
        // Shadow
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text, x, y);
        // Main
        g2.setColor(Color.white);
        g2.drawString(text, x-3, y-3);

        // Retry
        g2.setFont(g2.getFont().deriveFont(36f));
        text = "Retry";
        x = getXForCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-40, y);
        }

        // Back to the title screen
        text = "Quit";
        x = getXForCenteredText(text);
        y += 60;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-40, y);
        }
    }
    public void drawTransition() {
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if(counter == 50) { // The transition is done
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eventHandler.tempMap;
            gp.player.worldX = gp.eventHandler.tempCol * gp.tileSize;
            gp.player.worldY = gp.eventHandler.tempRow * gp.tileSize;
            gp.eventHandler.previousEventX = gp.player.worldX;
            gp.eventHandler.previousEventY = gp.player.worldY;
            gp.changeArea();
        }

    }
    public void drawTradeScreen() {

        switch(subState) {
            case 0: tradeSelect(); break;
            case 1: tradeBuy(); break;
            case 2: tradeSell(); break;
        }
        gp.keyHandler.enterPressed = false;
    }
    public void tradeSelect() {

        npc.dialogueSet = 0;
        drawDialogueScreen();

        // DRAW WINDOW
        int x = gp.tileSize*15;
        int y = gp.tileSize*4;
        int width = gp.tileSize*3;
        int height = (int)(gp.tileSize*3.5);
        drawSubWindow(x, y, width, height);

        // DRAW TEXTS
        x += gp.tileSize - 10;
        y += gp.tileSize;
        g2.drawString("Buy", x, y);
        if(commandNum == 0) {
            g2.drawString(">", x - 24, y);
            if(gp.keyHandler.enterPressed == true) {
                subState = 1;
            }
        }
        y += gp.tileSize;

        g2.drawString("Sell", x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - 24, y);
            if(gp.keyHandler.enterPressed == true) {
                subState = 2;
            }
        }
        y += gp.tileSize;
        g2.drawString("Leave", x, y);
        if(commandNum == 2) {
            g2.drawString(">", x - 24, y);
            if(gp.keyHandler.enterPressed == true) {
                commandNum = 0;
                npc.startDialogue(npc, 1);
            }
        }
        y += gp.tileSize;
    }
    public void tradeBuy() {
        // DRAW PLAYER'S INVENTORY
        drawInventory(gp.player, false);
        // DRAW NPC INVENTORY
        drawInventory(npc, true);

        // DRAW HINT WINDOW
        int x = gp.tileSize*2;
        int y = gp.tileSize*9;
        int width = gp.tileSize*6;
        int height = gp.tileSize*2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Back", x+24, y+60);

        // DRAW PLAYER COIN WINDOW
        x = gp.tileSize*12;
        y = gp.tileSize*9;
        width = gp.tileSize*6;
        height = gp.tileSize*2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your coin " + gp.player.coin, x+24, y+60);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if(itemIndex < npc.inventory.size()) {

            x = (int)(gp.tileSize*5.5);
            y = (int)(gp.tileSize*5.5);
            width = (int)(gp.tileSize*2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x+10, y+5, 32, 32, null);

            int price = npc.inventory.get(itemIndex).price;
            String text = "" + price;
            x = getXForAlignToRightText(text, gp.tileSize*8-20);
            g2.setFont(g2.getFont().deriveFont(28f));
            g2.drawString(text, x, y+31);
            // BUY AN ITEM
            if(gp.keyHandler.enterPressed == true) {
                if(npc.inventory.get(itemIndex).price > gp.player.coin) {
                    subState = 0;
                    npc.startDialogue(npc, 2);
                }
                else {
                    if(gp.player.canObtainItem(npc.inventory.get(itemIndex)) == true) {
                        gp.player.coin -= npc.inventory.get(itemIndex).price;
                    }
                    else {
                        subState = 0;
                        npc.startDialogue(npc, 3);
                    }
                }
            }
        }
    }
    public void tradeSell() {
        // DRAW PLAYER INVENTORY
        drawInventory(gp.player, true);

        // DRAW HINT WINDOW
        int x = gp.tileSize*2;
        int y = gp.tileSize*9;
        int width = gp.tileSize*6;
        int height = gp.tileSize*2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Back", x+24, y+60);

        // DRAW PLAYER COIN WINDOW
        x = gp.tileSize*12;
        y = gp.tileSize*9;
        width = gp.tileSize*6;
        height = gp.tileSize*2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your coin: " + gp.player.coin, x+24, y+60);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if(itemIndex < gp.player.inventory.size()) {

            x = (int)(gp.tileSize*15.5);
            y = (int)(gp.tileSize*5.5);
            width = (int)(gp.tileSize*2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x+10, y+5, 32, 32, null);

            int price = gp.player.inventory.get(itemIndex).price/2;
            String text = "" + price;
            x = getXForAlignToRightText(text, gp.tileSize*18-20);
            g2.setFont(g2.getFont().deriveFont(28f));
            g2.drawString(text, x, y+31);

            // SELL AN ITEM
            if(gp.keyHandler.enterPressed == true) {

                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon ||
                        gp.player.inventory.get(itemIndex) == gp.player.currentShield) {
                    commandNum = 0;
                    subState = 0;
                    npc.startDialogue(npc, 4);
                }
                else {
                    if(gp.player.inventory.get(itemIndex).amount > 1) {
                        gp.player.inventory.get(itemIndex).amount--;
                    }
                    else {
                        gp.player.inventory.remove(itemIndex);
                    }
                    gp.player.coin += price;
                }
            }
        }
    }
    public void drawSleepScreen() {

        counter++;
        gp.stopMusic();

        if(counter < 120) {
            gp.environmentManager.lighting.filterAlpha += 0.01f;
            if(gp.environmentManager.lighting.filterAlpha > 1f) {
                gp.environmentManager.lighting.filterAlpha = 1f;
            }
        }
        if(counter >= 120) {
            gp.environmentManager.lighting.filterAlpha -= 0.01f;
            if(gp.environmentManager.lighting.filterAlpha <= 0f) {
                gp.playMusic(0);
                gp.environmentManager.lighting.filterAlpha = 0f;
                counter = 0;
                gp.environmentManager.lighting.dayState = gp.environmentManager.lighting.day;
                gp.environmentManager.lighting.dayCounter = 0;
                gp.gameState = gp.playState;
                gp.player.getImage();
            }
        }
    }
    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(22F));

        // SUB WINDOW
        int frameX = gp.tileSize*6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState) {
        case 0: options_top(frameX, frameY); break;
        case 1: options_fullScreenNotification(frameX, frameY); break;
        case 2: options_control(frameX, frameY); break;
        case 3: options_endGameConfirmation(frameX, frameY); break;
        }

        gp.keyHandler.enterPressed = false;
    }
    public void options_top(int frameX, int frameY) {

        int textX;
        int textY;

        // TITLE
        String text = "Options";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        // FULL SCREEN ON/OFF
        textX = frameX + gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("Full screen", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyHandler.enterPressed == true) {
                if(gp.fullScreenOn == false) {
                    gp.fullScreenOn = true;
                }
                else if(gp.fullScreenOn == true) {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }

        }

        // MUSIC
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX-25, textY);
        }

        // SE
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if(commandNum == 2) {
            g2.drawString(">", textX-25, textY);
        }

        // CONTROL
        textY += gp.tileSize;
        g2.drawString("Control", textX, textY);
        if(commandNum == 3) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyHandler.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }

        }

        // END GAME
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if(commandNum == 4) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyHandler.enterPressed == true) {
                subState = 3;
                commandNum = 0;
            }
        }

        // BACK
        textY += gp.tileSize*2;
        g2.drawString("Back", textX, textY);
        if(commandNum == 5) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyHandler.enterPressed == true) {
                commandNum = 0;
                gp.gameState = gp.playState;
            }
        }

        // FULL SCREEN CHECK BOX
        textX = (int)(frameX + gp.tileSize*4.7);
        textY = frameY + gp.tileSize*2 + gp.tileSize/2;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, gp.tileSize/2, gp.tileSize/2);
        if(gp.fullScreenOn == true) {
            g2.fillRect(textX, textY, gp.tileSize/2, gp.tileSize/2);
        }

        // MUSIC VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, gp.tileSize/2); // 120/5 = 24, scale 120 - width, 5 - volume scale number
        int volumeWidth = 24*gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, gp.tileSize/2);

        // SE VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, gp.tileSize/2);
        volumeWidth = 24*gp.se.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, gp.tileSize/2);

        gp.config.saveConfig();
    }
    public void options_fullScreenNotification(int frameX, int frameY) {

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentDialogue = "The change will take \neffect after restarting \nthe game.";

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // BACK
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyHandler.enterPressed == true) {
                subState = 0;
            }
        }
    }
    public void options_control(int frameX, int frameY) {
        int textX;
        int textY;

        // TITLE
        String text = "Control";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY); textY += gp.tileSize;
        g2.drawString("Confirm/Attack", textX, textY); textY += gp.tileSize;
        g2.drawString("Shoot/Cast", textX, textY); textY += gp.tileSize;
        g2.drawString("Character Screen", textX, textY); textY += gp.tileSize;
        g2.drawString("Pause", textX, textY); textY += gp.tileSize;
        g2.drawString("Options", textX, textY); textY += gp.tileSize;

        textX = frameX + gp.tileSize*6;
        textY = frameY + gp.tileSize*2;
        g2.drawString("WASD", textX, textY); textY += gp.tileSize;
        g2.drawString("ENTER", textX, textY); textY += gp.tileSize;
        g2.drawString("F", textX, textY); textY += gp.tileSize;
        g2.drawString("C", textX, textY); textY += gp.tileSize;
        g2.drawString("P", textX, textY); textY += gp.tileSize;
        g2.drawString("ESC", textX, textY); textY += gp.tileSize;

        // BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyHandler.enterPressed == true) {
                subState = 0;
                commandNum = 3;
            }
        }
    }

    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
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