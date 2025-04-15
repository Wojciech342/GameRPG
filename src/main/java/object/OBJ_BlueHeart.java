package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_BlueHeart extends Entity {

    GamePanel gp;
    public static final String objName = "Blue Heart";
    public OBJ_BlueHeart(GamePanel gp) {
        super(gp);
        
        this.gp = gp;

        type = type_pickUpOnly;
        name = objName;
        down1 = setup("/objects/blueheart", gp.tileSize, gp.tileSize);

        setDialogues();
    }
    public void setDialogues() {

        dialogues[0][0] = "You pick up a beautiful blue gem";
        dialogues[0][1] = "You find the legendary treasure";
    }
    public boolean use(Entity entity) {

        gp.gameState = gp.cutSceneState;
        gp.cutSceneManager.sceneNum = gp.cutSceneManager.ending;
        return true;
    }
    
}
