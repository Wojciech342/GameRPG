package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {

    //In the class OBJ_Boots, GamePanel gp is not declared as a field within OBJ_Boots because you don't need to use it directly. Instead, you pass the gp parameter 
    //to the constructor of the superclass Entity, which stores it as a field there. In OBJ_Boots, you rely on the gp field from the parent class (Entity) to access it 
    //when needed. This means you don’t need to explicitly declare a gp field in the OBJ_Boots class because you're not using it directly in that class.

    public static final String objName = "Boots";

    public OBJ_Boots(GamePanel gp) {

        super(gp);

        name = objName;
        down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);
        price = 10;
        description = "[Boots]\nMake you faster";
    }
}
