package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.util.Random;

public class MON_GreenSlime extends Entity {

    //Here, you explicitly store the gp instance in the MON_GreenSlime class because you are using it directly within this class, for example, 
    //when loading images in the getImage() method. In this case, this.gp is necessary because you're referring to gp as a local field of MON_GreenSlime, 
    //rather than relying on the parent class's gp.
    // So i need it because i am using it in the getImage() method, different method then the class
    GamePanel gp;

    public MON_GreenSlime(GamePanel gp) {
        super(gp); // pass gp to super class

        this.gp = gp;

        type = type_monster;
        name = "Green Slime";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 4;
        life = maxLife;
        attack = 3;
        defense = 0;
        exp = 2;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage() {

        up1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
    }
    public void setAction() {
        
        if(onPath == true) {

            // Check if it stops chasing
            checkStopChasing(gp.player, 8, 100);

            // Search the direction to go
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
        }
        else {
            // Check if it starts chasing
            checkStartChasing(gp.player, 5, 100);

            // Get a random direction
            getRandomDirection(120);
        }
    }
    public void damageReaction(){

        actionLockCounter = 0;
        onPath = true;
    }
    public void checkDrop() {

        // CAST A DIE
        int i = new Random().nextInt(100) + 1;

        // SET THE MONSTER DROP
        if(i < 50) {
            dropItem(new OBJ_Coin_Bronze(gp));
        }
        else if(i >= 50 && i < 75) {
            dropItem(new OBJ_Heart(gp));
        }
        else if(i > 75 && i < 100) {
            dropItem(new OBJ_ManaCrystal(gp));
        }
    }
}
