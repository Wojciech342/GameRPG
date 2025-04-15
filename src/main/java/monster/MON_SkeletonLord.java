package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Door_Iron;

import data.Progress;

public class MON_SkeletonLord extends Entity {

    GamePanel gp;
    public static final String monName = "Skeleton Lord";

    public MON_SkeletonLord(GamePanel gp) {
        super(gp); // pass gp to super class

        this.gp = gp;

        type = type_monster;
        boss = true;
        name = monName;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 1;
        life = maxLife;
        attack = 12;
        defense = 0;
        exp = 50;
        knockBackPower = 5;
        sleep = true;

        int size = gp.tileSize*5;
        solidArea.x = 48;
        solidArea.y = 48;
        solidArea.width = size - 48*2;
        solidArea.height = size - 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 170;
        attackArea.height = 170;
        motion1_duration = 25;
        motion2_duration = 50;

        getImage();
        getAttackImage();
        setDialogue();
    }
    public void getImage() {
        int i = 5;
        int size = gp.tileSize*i;

        if(!inRage) {
            up1 = setup("/monster/skeletonlord_up_1", size, size);
            up2 = setup("/monster/skeletonlord_up_2", size, size);
            down1 = setup("/monster/skeletonlord_down_1", size, size);
            down2 = setup("/monster/skeletonlord_down_2", size, size);
            left1 = setup("/monster/skeletonlord_left_1", size, size);
            left2 = setup("/monster/skeletonlord_left_2", size, size);
            right1 = setup("/monster/skeletonlord_right_1", size, size);
            right2 = setup("/monster/skeletonlord_right_2", size, size);
        }
        else {
            up1 = setup("/monster/skeletonlord_phase2_up_1", size, size);
            up2 = setup("/monster/skeletonlord_phase2_up_2", size, size);
            down1 = setup("/monster/skeletonlord_phase2_down_1", size, size);
            down2 = setup("/monster/skeletonlord_phase2_down_2", size, size);
            left1 = setup("/monster/skeletonlord_phase2_left_1", size, size);
            left2 = setup("/monster/skeletonlord_phase2_left_2", size, size);
            right1 = setup("/monster/skeletonlord_phase2_right_1", size, size);
            right2 = setup("/monster/skeletonlord_phase2_right_2", size, size);
        }
        
    }
    public void getAttackImage() {
        int i = 5;
        int size = gp.tileSize*i;

        if(!inRage) {
            attackUp1 = setup("/monster/skeletonlord_attack_up_1", size, size*2);
            attackUp2 = setup("/monster/skeletonlord_attack_up_2", size, size*2);
            attackDown1 = setup("/monster/skeletonlord_attack_down_1", size, size*2);
            attackDown2 = setup("/monster/skeletonlord_attack_down_2", size, size*2);
            attackLeft1 = setup("/monster/skeletonlord_attack_left_1", size*2, size);
            attackLeft2 = setup("/monster/skeletonlord_attack_left_2", size*2, size);
            attackRight1 = setup("/monster/skeletonlord_attack_right_1", size*2, size);
            attackRight2 = setup("/monster/skeletonlord_attack_right_2", size*2, size);
        }
        else {
            attackUp1 = setup("/monster/skeletonlord_phase2_attack_up_1", size, size*2);
            attackUp2 = setup("/monster/skeletonlord_phase2_attack_up_2", size, size*2);
            attackDown1 = setup("/monster/skeletonlord_phase2_attack_down_1", size, size*2);
            attackDown2 = setup("/monster/skeletonlord_phase2_attack_down_2", size, size*2);
            attackLeft1 = setup("/monster/skeletonlord_phase2_attack_left_1", size*2, size);
            attackLeft2 = setup("/monster/skeletonlord_phase2_attack_left_2", size*2, size);
            attackRight1 = setup("/monster/skeletonlord_phase2_attack_right_1", size*2, size);
            attackRight2 = setup("/monster/skeletonlord_phase2_attack_right_2", size*2, size);
        }
        
    }
    public void setDialogue() {
        dialogues[0][0] = "No one can steal my treasure.";
        dialogues[0][1] = "You will die here!";
        dialogues[0][2] = "WELCOME TO YOUR DOOM!";
    }
    public void setAction() {
        if(!inRage && life < maxLife/2) {
            inRage = true;
            getImage();
            getAttackImage();
            defaultSpeed++;
            speed = defaultSpeed;
            attack *= 2;
        }
        
        if(getTileDistance(gp.player) < 8) {
            moveTowardPlayer(60);
        }
        else {
            getRandomDirection(120);
        }

        if(!attacking) {
            checkAttack(60, gp.tileSize*7, attackArea.width*5);
        }
    }
    public void damageReaction(){
        actionLockCounter = 0;
    }
    public void checkDrop() {
        gp.bossBattleOn = false;
        Progress.skeletonLordDefeated = true;

        // Restore the previous music
        gp.stopMusic();
        gp.playMusic(19);

        // Remove the iron doors
        for(int i = 0; i < gp.objects[1].length; i++) {
            if(gp.objects[gp.currentMap][i] != null && gp.objects[gp.currentMap][i]instanceof OBJ_Door_Iron) {
                gp.playSE(21);
                gp.objects[gp.currentMap][i] = null;
            }
        }
    }
}
