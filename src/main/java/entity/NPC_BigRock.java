package entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import main.GamePanel;
import object.OBJ_Door_Iron;
import tile_interactive.IT_MetalPlate;
import tile_interactive.InteractiveTile;

public class NPC_BigRock extends Entity {

    public static final String npcName = "Big Rock";

    public NPC_BigRock(GamePanel gp) {
        super(gp);

        name = npcName;
        direction = "down";
        speed = 6;

        solidArea = new Rectangle();
        solidArea.x = 2;
        solidArea.y = 6;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 44;
        solidArea.height = 40;

        dialogueSet = -1; // Cause we add one before the method is called, so this is so that we start from index 0

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1 = setup("/npc/bigrock", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/bigrock", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/bigrock", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/bigrock", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/bigrock", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/bigrock", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/bigrock", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/bigrock", gp.tileSize, gp.tileSize);
    }
    public void setDialogue() {
        dialogues[0][0] = "It's a giant rock.";

    }
    public void update() {}
    public void speak() {
        //Do this character specific stuff
        facePlayer();
        startDialogue(this, dialogueSet);

        dialogueSet++;

        if(dialogues[dialogueSet][0] == null) {
            dialogueSet = 0;
        }
    }
    public void move(String d) {
        this.direction = d;

        checkCollision();

        if(collisionOn == false) {

            switch(direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
            }
        }
        
        detectPlate();
    }
    public void detectPlate() {
        ArrayList<InteractiveTile> plateList = new ArrayList<>();
        ArrayList<Entity> rockList = new ArrayList<>();

        // Create a plate list
        for(int i = 0; i < gp.interactiveTiles[1].length; i++) {
            if(gp.interactiveTiles[gp.currentMap][i] != null &&
               gp.interactiveTiles[gp.currentMap][i]instanceof IT_MetalPlate) {
                plateList.add(gp.interactiveTiles[gp.currentMap][i]);
            }
        }

        // Create a rock list
        for(int i = 0; i < gp.npcs[1].length; i++) {
            if(gp.npcs[gp.currentMap][i] != null && gp.npcs[gp.currentMap][i]instanceof NPC_BigRock) {
                rockList.add(gp.npcs[gp.currentMap][i]);
            }
        }

        int count = 0;

        // Scan the plate list
        for (InteractiveTile interactiveTile : plateList) {
            int xDistance = Math.abs(worldX - interactiveTile.worldX);
            int yDistance = Math.abs(worldY - interactiveTile.worldY);
            int distance = Math.max(xDistance, yDistance);

            if (distance < 8) { // distance allowed between rock and the metal plate to set it as covered
                if (linkedEntity == null) {
                    linkedEntity = interactiveTile;
                    gp.playSE(3);
                }
            } else {
                if (linkedEntity == interactiveTile) {
                    // So that the plate does not lose its connection while checking other plates (other plates will obviously have more distance)
                    linkedEntity = null;
                }
            }
        }

        // Scan the rock list
        for (Entity entity : rockList) {
            // Count the rock on the plate
            if (entity.linkedEntity != null) {
                count++;
            }
        }

        // If all the rocks are on the plates, the iron door opens
        if(count == rockList.size()) {
            for(int i = 0; i < gp.objects[1].length; i++) {
                if(gp.objects[gp.currentMap][i] != null && gp.objects[gp.currentMap][i].name.equals(OBJ_Door_Iron.objName)) {
                    gp.objects[gp.currentMap][i] = null;
                    gp.playSE(21);
                }
            }
        }
    }
}
