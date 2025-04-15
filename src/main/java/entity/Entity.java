package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class Entity {
    
    GamePanel gp;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, 
    attackLeft1, attackLeft2, attackRight1, attackRight2,
    guardUp, guardDown, guardLeft, guardRight;
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0,0,48,48); // Default value, can override
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    public String dialogues[][] = new String[20][20];
    public Entity attacker;
    public Entity linkedEntity;
    public boolean temp = false;
    
    // STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    public int dialogueSet = 0;
    public int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean knockBack = false;
    public String knockBackDirection;
    public boolean guarding = false;
    public boolean transparent = false;
    public boolean offBalance = false;
    public Entity loot;
    public boolean opened = false;
    public boolean inRage = false;
    public boolean sleep = false;
    public boolean drawing = true;
    
    // COUNTER
    public int actionLockCounter = 0;
    public int spriteCounter = 0;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;
    public int knockBackCounter = 0;
    public int guardCounter = 0;
    public int offBalanceCounter = 0;
   

    // CHARACTER ATTRIBUTES
    public String name;
    public int defaultSpeed;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity; //  zręczność, sprawność
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public int motion1_duration;
    public int motion2_duration;
    public Entity currentWeapon;
    public Entity currentShield;
    public Entity currentLight;
    public Projectile projectile;
    public boolean boss = false;

    // ITEM ATTRIBUTES
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int price;
    public int knockBackPower = 0;
    public boolean stackable = false;
    public int amount = 1;
    public int lightRadius;

    // TYPE
    public int type;
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickUpOnly = 7;
    public final int type_obstacle = 8;
    public final int type_light = 9;
    public final int type_pickaxe = 10;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setPosition(int col, int row) {
        this.worldX = gp.tileSize * col;
        this.worldY = row;
    }
    public int getScreenX() {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        return screenX;
    }
    public int getScreenY() {
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        return screenY;
    }
    public int getLeftX() {
        return worldX + solidArea.x;
    }
    public int getRightX() {
        return worldX + solidArea.x + solidArea.width;
    }
    public int getTopY() {
        return worldY + solidArea.y;
    }
    public int getBottomY() {
        return worldY + solidArea.y + solidArea.height;
    }
    public int getCol() {
        return (worldX + solidArea.x)/gp.tileSize;
    }
    public int getRow() {
        return (worldY + solidArea.y)/gp.tileSize;
    }
    public int getCenterX() {
        int centerX = worldX + left1.getWidth()/2; // left1 and up1 is the same
        return centerX;
    }
    public int getCenterY() {
        int centerY = worldY + up1.getHeight()/2;
        return centerY;
    }
    public int getXDistance(Entity target) {
        int xDistance = Math.abs(getCenterX() - target.getCenterX());
        return xDistance;
    }
    public int getYDistance(Entity target) {
        int yDistance = Math.abs(getCenterY() - target.getCenterY());
        return yDistance;
    }
    public int getTileDistance(Entity target) {
        int tileDistance = (getXDistance(target) + getYDistance(target))/gp.tileSize;
        return tileDistance;
    }
    public int getGoalCol(Entity target) {
        int goalCol = (target.worldX + target.solidArea.x)/gp.tileSize;
        return goalCol;
    }
    public int getGoalRow(Entity target) {
        int goalRow = (target.worldY + target.solidArea.y)/gp.tileSize;
        return goalRow;
    }
    public void resetCounter() {

        actionLockCounter = 0;
        spriteCounter = 0;
        invincibleCounter = 0;
        shotAvailableCounter = 0;
        dyingCounter = 0;
        hpBarCounter = 0;
        knockBackCounter = 0;
        guardCounter = 0;
        offBalanceCounter = 0;
    }
    public void setLoot(Entity loot) {}
    public void setAction() {}
    public void move(String direction) {}
    public void damageReaction() {}
    public void speak() {}
    public void facePlayer() {

        switch(gp.player.direction) {
        case "up": direction = "down"; break;
        case "down": direction = "up"; break;
        case "left": direction = "right"; break;
        case "right": direction = "left"; break;
        }
    }
    public void startDialogue(Entity entity, int setNum) {
        
        gp.gameState = gp.dialogueState;
        gp.ui.npc = entity;
        dialogueSet = setNum;
    }
    public void interact() {}
    public boolean use(Entity entity) {return false;}
    public void checkDrop(){}
    public void dropItem(Entity droppedItem) {

        for(int i = 0; i < gp.objects[1].length; i++) {
            if(gp.objects[gp.currentMap][i] == null) {
                gp.objects[gp.currentMap][i] = droppedItem;
                gp.objects[gp.currentMap][i].worldX = worldX; // the dead monster coordinates
                gp.objects[gp.currentMap][i].worldY = worldY;
                break; // we want it only for one dead monster, we have to break it
            }
        }
    }
    public void generateParticle(Entity generator, Entity target) {

        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1); // left = -
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);// right = +
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);// top = -
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);// bottom = +
        gp.particles.add(p1);
        gp.particles.add(p2);
        gp.particles.add(p3);
        gp.particles.add(p4);
    }
    public Color getParticleColor() {
        Color color = null;
        return color;
    }
    public int getParticleSize() {
        int size = 0; // 6 pixels
        return size;
    }
    public int getParticleSpeed() {
        int speed = 0;
        return speed;
    }
    public int getParticleMaxLife() {
        int maxLife = 0;
        return maxLife;
    }
    public void checkCollision() {

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false); // not player so false
        gp.cChecker.checkEntity(this, gp.npcs);
        gp.cChecker.checkEntity(this, gp.monsters);
        gp.cChecker.checkEntity(this, gp.interactiveTiles);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == type_monster && contactPlayer == true) {
            damagePlayer(attack);
        }
    }
    public void update() { // OVERRIDE in player class
        if(!sleep) {
            if(knockBack) {
                checkCollision();
    
                if(collisionOn) {
                    knockBackCounter = 0;
                    knockBack = false;
                    speed = defaultSpeed;
                }
                else {
                    switch(knockBackDirection) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                    }
                }
    
                knockBackCounter++;
                if(knockBackCounter == 10) {
                    knockBackCounter = 0;
                    knockBack = false;
                    speed = defaultSpeed;
                }
            }
            else if(attacking) {
                attacking();
            }
            else {
                setAction(); // subclass method takes priority
                checkCollision();
    
                // IF COLLISION IS FALSE, PLAYER CAN MOVE
                if(collisionOn == false) {
    
                    switch(direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                    }
                }
    
                spriteCounter++;
                if(spriteCounter > 24) {
                    if(spriteNum == 1) {
                        spriteNum = 2;
                    }
                    else if(spriteNum == 2) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }
            }
            
            if(invincible) {
                invincibleCounter++;
                if(invincibleCounter > 30) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
            if(shotAvailableCounter < 30) {
                shotAvailableCounter++;
            }
            if(offBalance) {
                offBalanceCounter++;
                if(offBalanceCounter > 60) {
                    offBalance = false;
                    offBalanceCounter = 0;
                }
            }
        }
    }
    public void checkAttack(int rate, int straight, int horizontal) { // straight - vertical distance, horizontal - horizontal distance
        boolean targetInRange = false;
        int xDis = getXDistance(gp.player);
        int yDis = getYDistance(gp.player);

        switch(direction) {
        case "up": 
            if(gp.player.getCenterY() < getCenterY() && yDis < straight && xDis < horizontal) {
                targetInRange = true;
            }
            break;
        case "down":
            if(gp.player.getCenterY() > getCenterY() && yDis < straight && xDis < horizontal) {
                targetInRange = true;
            }
            break;
        case "left": 
            if(gp.player.getCenterX() < getCenterX() && yDis < straight && xDis < horizontal) {
                targetInRange = true;
            }
            break;
        case "right":
            if(gp.player.getCenterX() > getCenterX() && yDis < straight && xDis < horizontal) {
                targetInRange = true;
            }
            break;
        }

        if(targetInRange) {
            // Check if it initates an attack
            int i = new Random().nextInt(rate);
            if(i == 0) {
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;
                shotAvailableCounter = 0;
            }
        }
    }
    public void checkShoot(int rate, int shotInterval) {
        int i = new Random().nextInt(rate);// pick up a number from 0 to rate - 1
        if(i == 0 && projectile.alive == false && shotAvailableCounter == shotInterval) {
            
            projectile.set(worldX, worldY, direction, true, this);

            // CHECK VACANCY
            for(int ii = 0; ii < gp.projectiles[1].length; ii++) {
                if(gp.projectiles[gp.currentMap][ii] == null) {
                    gp.projectiles[gp.currentMap][ii] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0;
        }
    }
    public void checkStartChasing(Entity target, int distance, int rate) {
        if(getTileDistance(target) < distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                onPath = true;
            }
        }
    }
    public void checkStopChasing(Entity target, int distance, int rate) {
        if(getTileDistance(target) > distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                onPath = false;
            }
        }
    }
    public void getRandomDirection(int interval) {
        actionLockCounter++;

            if(actionLockCounter > interval) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;// pick up a number from 1 to 100

                if(i <= 25) {direction = "up";}
                else if(i > 25 && i <= 50) {direction = "down";}
                else if(i > 50 && i <= 75) {direction = "left";}
                else if(i > 75 && i <=100) {direction = "right";}
                actionLockCounter = 0;
            }
    }
    public void moveTowardPlayer(int interval) {
        actionLockCounter++;

        if(actionLockCounter > interval) {

            if(getXDistance(gp.player) > getYDistance(gp.player)) {
                if(gp.player.getCenterX() < getCenterX()) {
                    direction = "left";
                }
                else {
                    direction = "right";
                }
            }
            else if(getXDistance(gp.player) < getYDistance(gp.player)) {
                if(gp.player.getCenterY() < getCenterY()) {
                    direction = "up";
                }
                else {
                    direction = "down";
                }
            }
            actionLockCounter = 0;
        }
    }
    public String getOppositeDirection(String direction) {
        String oppositeDirection = "";
        switch(direction) {
        case "up": oppositeDirection = "down"; break;
        case "down": oppositeDirection = "up"; break;
        case "left": oppositeDirection = "right"; break;
        case "right": oppositeDirection = "left"; break;
        }
        return oppositeDirection;
    }
    public void attacking() {
        spriteCounter++;

        if(spriteCounter <= motion1_duration) { // First attacking image for motion1_duration frames
            spriteNum = 1;
        }
        else if(spriteCounter <= motion2_duration) {  // Second attacking image for motion2_duration frames
            spriteNum = 2;

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y for the attackArea
            switch(direction) {
            case "up": worldY -= attackArea.height; break;
            case "down": worldY += attackArea.height; break;
            case "left": worldX -= attackArea.width; break;
            case "right": worldX += attackArea.width; break;
            }

            // attackArea becomes solidArea
            // adjust the solidArea, so that it matches the attackArea (of the weapon)
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            if(type == type_monster) {
                if(gp.cChecker.checkPlayer(this) == true) {
                    damagePlayer(attack);
                }
            }
            else { // Player
                // Check monster collision with the updated worldX, worldY and solidArea
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monsters);
                gp.player.damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);

                int iTileIndex = gp.cChecker.checkEntity(this, gp.interactiveTiles);
                gp.player.damageInteractiveTile(iTileIndex);

                int projectileIndex = gp.cChecker.checkEntity(this, gp.projectiles);
                gp.player.damageProjectile(projectileIndex); 
            }
            // After checking collision, restore the original idea
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        else {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    public void damagePlayer(int attack) {
        if(!gp.player.invincible) {

            int damage = attack - gp.player.defense;

            // Get an opposite direction of this attacker
            String canGuardDirection = getOppositeDirection(direction);

            if(gp.player.guarding && gp.player.direction.equals(canGuardDirection)) {

                // Parry
                if(gp.player.guardCounter < 20) { // Parry if you press the block less than 20 frames before the attack
                    damage = 0;
                    gp.playSE(16);
                    setKnockBack(this, gp.player, knockBackPower); // monster knockBackPower (can be changed)
                    offBalance = true;
                    spriteCounter = -60;
                }
                // Normal guard
                else {
                    damage /= 3;
                    gp.playSE(15);
                }  
            }
            else {
                // Not guarding
                gp.playSE(6);
                if(damage < 1) {
                    damage = 1;
                }
            }
            if(damage != 0) {
                gp.player.transparent = true;
                setKnockBack(gp.player, this, knockBackPower);
            }
            gp.player.life -= damage;
            gp.player.invincible = true;
        }
    }
    public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {
        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockBackPower;
        target.knockBack = true;
    }
    public boolean inCamera() {
        boolean inCamera = false;
        if(worldX + gp.tileSize*5 > gp.player.worldX - gp.player.screenX && // cause we check top left corner (*5)
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize*5 > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            inCamera = true;
        }
        return inCamera;
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        if(inCamera()) {
            int tempScreenX = getScreenX();
            int tempScreenY = getScreenY();
            switch(direction) {
            case "up":
                if(attacking == false) {
                    if(spriteNum == 1) {image = up1;}
                    if(spriteNum == 2) {image = up2;}
                }
                else if(attacking == true) {
                    tempScreenY = getScreenY() - up1.getHeight(); // because the attacking sprite is size 2x1 not 1x1, we have to adjust
                    if(spriteNum == 1) {image = attackUp1;}
                    if(spriteNum == 2) {image = attackUp2;}
                }
                break;
            case "down":
                if(attacking == false) {
                    if(spriteNum == 1) {image = down1;}
                    if(spriteNum == 2) {image = down2;}
                }
                else if(attacking == true) {
                    if(spriteNum == 1) {image = attackDown1;}
                    if(spriteNum == 2) {image = attackDown2;}
                }
                break;
            case "left":
                if(attacking == false) {
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left2;}
                }
                else if(attacking == true) {
                    tempScreenX =  getScreenX()- left1.getWidth(); // because the attacking sprite is size 2x1 not 1x1, we have to adjust
                    if(spriteNum == 1) {image = attackLeft1;}
                    if(spriteNum == 2) {image = attackLeft2;}
                }
                break;
            case "right":
                if(!attacking) {
                    if(spriteNum == 1) {image = right1;}
                    if(spriteNum == 2) {image = right2;}
                }
                else {
                    if(spriteNum == 1) {image = attackRight1;}
                    if(spriteNum == 2) {image = attackRight2;}
                }
                break;
            }

            if(invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }
            if(dying) {
                dyingAnimation(g2);
            }
            g2.drawImage(image, tempScreenX, tempScreenY, null);
            changeAlpha(g2, 1f);
            
        }
    }
    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int i = 5;
        
        if(dyingCounter <= i) {changeAlpha(g2, 0f);}
        else if(dyingCounter <= i*2) {changeAlpha(g2, 1f);}
        else if(dyingCounter <= i*3) {changeAlpha(g2, 0f);}
        else if(dyingCounter <= i*4) {changeAlpha(g2, 1f);}
        else if(dyingCounter <= i*5) {changeAlpha(g2, 0f);}
        else if(dyingCounter <= i*6) {changeAlpha(g2, 1f);}
        else if(dyingCounter <= i*7) {changeAlpha(g2, 0f);}
        else if(dyingCounter <= i*8) {changeAlpha(g2, 1f);}
        
        if(dyingCounter > i*8) {
            alive = false;
        }

    }
    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }
    public BufferedImage setup(String imagePath, int width, int height) {
        
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
            
        }catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public int getDetected(Entity user, Entity[][] target, String targetName) {
        int index = 999;
        // Check the sorrounding object
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch(user.direction) {
        case "up": nextWorldY = user.getTopY()-gp.player.speed; break;
        case "down": nextWorldY = user.getBottomY()+gp.player.speed; break;
        case "left": nextWorldX = user.getLeftX()-gp.player.speed; break;
        case "right": nextWorldX = user.getRightX()+gp.player.speed; break;
        }
        int col = nextWorldX/gp.tileSize;
        int row = nextWorldY/gp.tileSize;

        for(int i = 0; i < target[1].length; i++) {
            if(target[gp.currentMap][i] != null) {
                if(target[gp.currentMap][i].getCol() == col &&
                   target[gp.currentMap][i].getRow() == row &&
                   target[gp.currentMap][i].name.equals(targetName)) {

                    index = i;
                    break;
                }
            }
        }
        return index;
    }
    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x)/gp.tileSize;
        int startRow = (worldY + solidArea.y)/gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if(gp.pFinder.search()) {
            // Next worldX & worldY
            int nextX = gp.pFinder.pathList.getFirst().col * gp.tileSize;
            int nextY = gp.pFinder.pathList.getFirst().row * gp.tileSize;

            // Entity's solidArea position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) { // entity is straight below the goal
                direction = "up";
            }
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) { // entity is straight up to the goal
                direction = "down";
            }
            else if(enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                // left or right
                if(enLeftX > nextX) { // entity is straight to the right to the goal
                    direction = "left";
                }
                else if(enLeftX < nextX) { // entity is straight to the left to the goal
                    direction = "right";
                }
            }
            else if(enTopY > nextY && enLeftX > nextX) { // entity is below the goal, but there can be collision upwards, (between two tiles)
                // up or left
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX) { // entity is below the goal, but there can be collision upwards, (between two tiles)
                // up or right
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX) { // entity is up to the goal, but there can be collision upwards, (between two tiles)
                // down or left
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX) { // entity is up to the goal, but there can be collision upwards, (between two tiles)
                // down or left
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    direction = "right";
                }
            }
        }
    }
}
