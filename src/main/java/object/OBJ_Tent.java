package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Tent extends Entity {
    GamePanel gp;
    public static final String objName = "Tent";

    public OBJ_Tent(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = objName;
        down1 = setup("/objects/tent", gp.tileSize, gp.tileSize);
        description = "[Tent]\nYou can sleep until\nnext morning";
        price = 3;
        stackable = true;
        setDialogues();
    }
    public void setDialogues() {
        dialogues[0][0] = "You can only use " + name + " at dusk or at night.";
    }
    public boolean use(Entity entity) {
        if(gp.environmentManager.lighting.dayState == gp.environmentManager.lighting.dusk || gp.environmentManager.lighting.dayState == gp.environmentManager.lighting.night) {
            gp.gameState = gp.sleepState;
            gp.playSE(14);
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            gp.player.getSleepingImage(down1);
            //return true; // make the item dissapear
            return false; // the item is reusable, does not disappear
        }
        else {
            startDialogue(this, 0);
            return false;
        }

    }
    
}
