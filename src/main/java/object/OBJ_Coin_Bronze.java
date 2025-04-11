package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends Entity {
    
    GamePanel gp;
    public static final String objName = "Bronze coin";

    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickUpOnly;
        name = objName;
        value = 1;
        down1 = setup("/objects/coin_gold", gp.tileSize, gp.tileSize);
        price = 5;
    }
    public boolean use(Entity entity) {

        gp.playSE(1);
        gp.ui.addMessage("Coin +" + value);
        gp.player.coin += value;
        return true;
    }
}
