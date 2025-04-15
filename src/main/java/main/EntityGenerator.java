package main;

import entity.Entity;
import object.OBJ_Axe;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Coin_Bronze;
import object.OBJ_Door;
import object.OBJ_Door_Iron;
import object.OBJ_Fireball;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_Lantern;
import object.OBJ_ManaCrystal;
import object.OBJ_Pickaxe;
import object.OBJ_Potion_Red;
import object.OBJ_Rock;
import object.OBJ_Shield_Blue;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
import object.OBJ_Tent;
import object.OBJ_BlueHeart;

public class EntityGenerator {
    
    GamePanel gp;

    public EntityGenerator(GamePanel gp) {
        this.gp = gp;
    }
    public Entity getObject(String itemName) {
        Entity obj = switch (itemName) {
            case OBJ_Axe.objName -> new OBJ_Axe(gp);
            case OBJ_BlueHeart.objName -> new OBJ_BlueHeart(gp);
            case OBJ_Boots.objName -> new OBJ_Boots(gp);
            case OBJ_Chest.objName -> new OBJ_Chest(gp);
            case OBJ_Coin_Bronze.objName -> new OBJ_Coin_Bronze(gp);
            case OBJ_Door_Iron.objName -> new OBJ_Door_Iron(gp);
            case OBJ_Door.objName -> new OBJ_Door(gp);
            case OBJ_Fireball.objName -> new OBJ_Fireball(gp);
            case OBJ_Heart.objName -> new OBJ_Heart(gp);
            case OBJ_Key.objName -> new OBJ_Key(gp);
            case OBJ_Lantern.objName -> new OBJ_Lantern(gp);
            case OBJ_ManaCrystal.objName -> new OBJ_ManaCrystal(gp);
            case OBJ_Pickaxe.objName -> new OBJ_Pickaxe(gp);
            case OBJ_Potion_Red.objName -> new OBJ_Potion_Red(gp);
            case OBJ_Rock.objName -> new OBJ_Rock(gp);
            case OBJ_Shield_Blue.objName -> new OBJ_Shield_Blue(gp);
            case OBJ_Shield_Wood.objName -> new OBJ_Shield_Wood(gp);
            case OBJ_Sword_Normal.objName -> new OBJ_Sword_Normal(gp);
            case OBJ_Tent.objName -> new OBJ_Tent(gp);
            default -> null;
        };

        return obj;
    }
}
