package main;

import java.awt.event.*;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed, guardUp;
    //DEBUG
    boolean showDebugText = false;
    public boolean godModeOn = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            titleState(code);
        }
        // PLAY STATE
        else if(gp.gameState == gp.playState) {
            playState(code);
        }
        // PAUSE STATE
        else if(gp.gameState == gp.pauseState) {
            pauseState(code);
        }
        // DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutSceneState) {
            dialogueState(code);
        }
        // CHARACTER STATE
        else if(gp.gameState == gp.characterState) {
            characterState(code);
        }
        // OPTIONS STATE
        else if(gp.gameState == gp.optionsState) {
            optionsState(code);
        }
        // GAME OVER STATE
        else if(gp.gameState == gp.gameOverState) {
            gameOverState(code);
        }
        // TRADE STATE
        else if(gp.gameState == gp.tradeState) {
            tradeState(code);
        }
        // MAP STATE
        else if(gp.gameState == gp.mapState) {
            mapState(code);
        }
    }
    public void titleState(int code) {
        if(gp.ui.titleScreenState == 0) {
            if(code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if(code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER) {
                if(gp.ui.commandNum == 0) {
                    gp.ui.titleScreenState = 1;
                }
                if(gp.ui.commandNum == 1) {
                    gp.saveLoad.load();
                    gp.gameState = gp.playState;
                    
                }
                if(gp.ui.commandNum == 2) {
                    System.exit(0);
                }   
            }
        }
        else if(gp.ui.titleScreenState == 1) {
            if(code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 3;
                }
            }
            if(code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 3) {
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER) {
                if(gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                    
                }
                if(gp.ui.commandNum == 1) {
                    gp.gameState = gp.playState;
                }
                if(gp.ui.commandNum == 2) {
                    gp.gameState = gp.playState;
                }
                if(gp.ui.commandNum == 3) {
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0;
                }   
            }
        }
    }
    public void playState(int code) {
        if(code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }
        if(code == KeyEvent.VK_SPACE) {
            guardUp = true;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionsState;
        }
        if(code == KeyEvent.VK_X) {
            gp.map.miniMapOn = !gp.map.miniMapOn;
        }
        if(code == KeyEvent.VK_M) {
            gp.gameState = gp.mapState;
        }

        
        // DEBUG
        if(code == KeyEvent.VK_T) {
            showDebugText = !showDebugText;
        }
        if(code == KeyEvent.VK_R) { // DEBUG, loading changed map when you are already in game
            switch(gp.currentMap) {
            case 0: gp.tileManager.loadMap("/maps/worldV3.txt", 0);
            case 1: gp.tileManager.loadMap("/maps/interior.txt", 1);
            }  
        }
        if(code == KeyEvent.VK_G) {
            if(!godModeOn) {
                godModeOn = true;
            }
            else if(godModeOn) {
                godModeOn = false;
            }
        }
    }
    public void pauseState(int code) {
        
        if(code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
        
    }
    public void dialogueState(int code) {
        
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }
    public void characterState(int code) {
        
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
        playerInventory(code);
    }
    public void gameOverState(int code) {
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
            gp.playSE(9);
        }
        else if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
            gp.playSE(9);
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.resetGame(false);
                gp.playMusic(0);
            }
            else if(gp.ui.commandNum == 1) {
                gp.ui.commandNum = 0;
                gp.gameState = gp.titleState;
                gp.resetGame(true);
            }
        }
    }
    public void optionsState(int code) {
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch(gp.ui.subState) {
        case 0: maxCommandNum = 5; break;
        case 3: maxCommandNum = 1; break;
        }

        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSE(9);
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        else if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSE(9);
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume(); // we need to call it because the music is already being played
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;
                    gp.playSE(9);
                }
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;
                    gp.playSE(9);
                }
            }
        }
    }
    public void mapState(int code) {

        if(code == KeyEvent.VK_M) {
            gp.gameState = gp.playState;
        }
    }
    public void tradeState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        if(gp.ui.subState == 0) {
            if(code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
                gp.playSE(9);
            }
            else if(code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
                gp.playSE(9);
            }
        }
        if(gp.ui.subState == 1) {
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE) {
                gp.ui.subState = 0;
            }
        }
        else if(gp.ui.subState == 2) {
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE) {
                gp.ui.subState = 0;
            }
        }
    }
    public void playerInventory(int code) {

        if(code == KeyEvent.VK_W) {
            if(gp.ui.playerSlotRow != 0) {
                gp.ui.playerSlotRow--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gp.ui.playerSlotCol != 0) {
                gp.ui.playerSlotCol--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_S) {
            if(gp.ui.playerSlotRow != 3) {
                int slot = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow + 1);
                if(slot < gp.player.inventory.size()) {
                    gp.ui.playerSlotRow++;
                    gp.playSE(9);
                }
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gp.ui.playerSlotCol != 4) {
                int slot = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol + 1, gp.ui.playerSlotRow);
                if(slot < gp.player.inventory.size()) {
                    gp.ui.playerSlotCol++;
                    gp.playSE(9);
                }
            }
        }
    }
    public void npcInventory(int code) {
        if(code == KeyEvent.VK_W) {
            if(gp.ui.npcSlotRow != 0) {
                gp.ui.npcSlotRow--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gp.ui.npcSlotCol != 0) {
                gp.ui.npcSlotCol--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_S) {
            if(gp.ui.npcSlotRow != 3) {
                int slot = gp.ui.getItemIndexOnSlot(gp.ui.npcSlotCol, gp.ui.npcSlotRow + 1);
                if(slot < gp.ui.npc.inventory.size()) {
                    gp.ui.npcSlotRow++;
                    gp.playSE(9);
                }
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gp.ui.npcSlotCol != 4) {
                int slot = gp.ui.getItemIndexOnSlot(gp.ui.npcSlotCol + 1, gp.ui.npcSlotRow);
                if(slot < gp.ui.npc.inventory.size()) {
                    gp.ui.npcSlotCol++;
                    gp.playSE(9);
                }
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if(code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if(code == KeyEvent.VK_SPACE) {
            guardUp = false;
        }
    }
    
}
