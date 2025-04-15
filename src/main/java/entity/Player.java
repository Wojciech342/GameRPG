package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Lantern;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCancelled = false;
    public int npcIndex;
    public boolean lightUpdated = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp); // It is needed for the parent class Entity

        this.keyH = keyH;

        screenX = gp.screenWidth/2  - (gp.tileSize/2);
        screenY = gp.screenHeight/2  - (gp.tileSize/2);

        solidArea = new Rectangle(); // we want the solid area to be slightly smaller than the tile, for convenience
        solidArea.x = 12; // distance from the start of the tile to the right
        solidArea.y = 16; // distance from the start of the tile down
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 24;
        solidArea.height = 24;


        setDefaultValues();
    }

    public void setDefaultValues() {
         //DEFAULT
         worldX = gp.tileSize*23;
         worldY = gp.tileSize*21;
         gp.currentMap = 0;

        // SHOP
        // worldX = gp.tileSize*12;
        // worldY = gp.tileSize*9;
        // gp.currentMap = 1;
        
        // MAP 2
        // worldX = gp.tileSize*18;
        // worldY = gp.tileSize*20;
        // gp.currentMap = 2;

        //BOSS
        // worldX = gp.tileSize*20;
        // worldY = gp.tileSize*20;
        // gp.currentMap = 3;
        
        defaultSpeed = 6;
        speed = defaultSpeed;
        direction = "down";
        invincible = false;

        // PLAYER STATUS
        level = 1;
        maxLife = 8; // one heart = 2 lives
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        strength = 1; // The more strength he has, the more damage he gives.
        dexterity = 1; // The more dexterity he has, the less damage he receives
        exp = 0;
        nextLevelExp = 5;
        coin = 10;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        currentLight = new OBJ_Lantern(gp);
        //currentLight = null;
        projectile = new OBJ_Fireball(gp);
        attack = getAttack(); // The total attack value is decided by strength and weapon
        defense = getDefense(); // The total defense value is decided by dexterity and defense

        getImage();
        getAttackImage();
        getGuardImage();
        setItems();
        setDialogue();
    }
    public void setDefaultPosition() {
        gp.currentMap = 0;
        gp.currentArea = gp.outside;
        worldX = gp.tileSize*21;
        worldY = gp.tileSize*22;
        direction = "down";
    }
    public void setDialogue() {
        dialogues[0][0] = gp.ui.currentDialogue = "You are level " + level + " now!\n";
    }
    public void restoreStatus() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
        transparent = false;
        attacking = false;
        guarding = false;
        knockBack = false;
        lightUpdated = true;
        speed = defaultSpeed;
    }
    public void setItems() {
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Potion_Red(gp));
    }
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        motion1_duration = currentWeapon.motion1_duration;
        motion2_duration = currentWeapon.motion2_duration;
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }
    public int getCurrentWeaponSlot() {
        int currentWeaponSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentWeapon) {
                currentWeaponSlot = i;
                break;
            }
        }
        return currentWeaponSlot;
    }
    public int getCurrentShieldSlot() {
        int currentShieldSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentShield) {
                currentShieldSlot = i;
                break;
            }
        }
        return currentShieldSlot;
    }
    public void getImage() {

        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }
    public void getSleepingImage(BufferedImage image) {
        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
    }
    public void getAttackImage() {

        if(currentWeapon.type == type_sword) {
            attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
            attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
            attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
            attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
            attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
            attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
            attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
        }

        if(currentWeapon.type == type_axe) {
            attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize*2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize*2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize*2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize*2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize*2, gp.tileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize*2, gp.tileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize*2, gp.tileSize);
        }

        if(currentWeapon.type == type_pickaxe) {
            attackUp1 = setup("/player/boy_pick_up_1", gp.tileSize, gp.tileSize*2);
            attackUp2 = setup("/player/boy_pick_up_2", gp.tileSize, gp.tileSize*2);
            attackDown1 = setup("/player/boy_pick_down_1", gp.tileSize, gp.tileSize*2);
            attackDown2 = setup("/player/boy_pick_down_2", gp.tileSize, gp.tileSize*2);
            attackLeft1 = setup("/player/boy_pick_left_1", gp.tileSize*2, gp.tileSize);
            attackLeft2 = setup("/player/boy_pick_left_2", gp.tileSize*2, gp.tileSize);
            attackRight1 = setup("/player/boy_pick_right_1", gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("/player/boy_pick_right_2", gp.tileSize*2, gp.tileSize);
        }
        
    }
    public void getGuardImage() {

        guardUp = setup("/player/boy_guard_up", gp.tileSize, gp.tileSize);
        guardDown = setup("/player/boy_guard_down", gp.tileSize, gp.tileSize);
        guardLeft = setup("/player/boy_guard_left", gp.tileSize, gp.tileSize);
        guardRight = setup("/player/boy_guard_right", gp.tileSize, gp.tileSize);
    }

    public void update() {
        if(knockBack) {
            collisionOn = false; // // collisionOn is changed in the checkTile method if there is a collision
            gp.collisionChecker.checkTile(this);
            gp.collisionChecker.checkObject(this, true);
            gp.collisionChecker.checkEntity(this, gp.npcs); // check player collision with NPC
            gp.collisionChecker.checkEntity(this, gp.monsters);
            gp.collisionChecker.checkEntity(this, gp.interactiveTiles);

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
        else if(keyH.guardUp) {
            guarding = true;
            guardCounter++;
        }
        else if(keyH.upPressed || keyH.downPressed ||
           keyH.leftPressed  || keyH.rightPressed || keyH.enterPressed) {

            if(keyH.upPressed) {
                direction = "up";     
            }
            else if(keyH.downPressed) {
                direction = "down";                
            }
            else if(keyH.leftPressed) {
                direction = "left";
            }
            else if(keyH.rightPressed) {
                direction = "right";
            }

            // CHECK TILE COLLISION 
            collisionOn = false; // // collisionOn is changed in the checkTile method if there is a collision
            gp.collisionChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gp.collisionChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // CHECK NPC COLLISION
            npcIndex = gp.collisionChecker.checkEntity(this, gp.npcs); // check player collision with NPC
            interactNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monsters);
            contactMonster(monsterIndex);

            // CHECK INTERACTIVE TILE COLLISION
            gp.collisionChecker.checkEntity(this, gp.interactiveTiles);

            // CHECK EVENT
            gp.eventHandler.checkEvent();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(!collisionOn && !keyH.enterPressed) { // keyH.enterPressed == false without it the player will move when the enter is pressed

                switch(direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
                }
            }

            if(keyH.enterPressed && !attackCancelled) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCancelled = false;
            gp.keyHandler.enterPressed = false;
            guarding = false;
            guardCounter = 0;
    
            spriteCounter++;
            if(spriteCounter > 12) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                }
                else if(spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        else {
            standCounter++;

            if(standCounter == 20) {
                spriteNum = 1; // It would be best to create a new standing sprite
                standCounter = 0;
            }
            guarding = false;
            guardCounter = 0;
        }

        if(gp.keyHandler.shotKeyPressed == true && projectile.alive == false &&
           shotAvailableCounter == 30 && projectile.haveResource(this) == true) {

            // SET DEFAULT COORDINATES, DIRECTION AND USER
            projectile.set(worldX, worldY, direction, true, this);

            // SUBTRACT THE COST (MANA, AMMO, ETC.)
            projectile.subtractResource(this);

            for(int i = 0; i < gp.projectiles[i].length; i++) {
                if(gp.projectiles[gp.currentMap][i] == null) {
                    gp.projectiles[gp.currentMap][i] = projectile;
                    break;
                }
            }
            shotAvailableCounter = 0;

            gp.playSE(10);
        }

        // This needs to be outside of key if statement
        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                transparent = false;
                invincibleCounter = 0;
            }
        }
        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        if(life > maxLife) {
            life = maxLife;
        }
        if(mana > maxMana) {
            mana = maxMana;
        }
        if(keyH.godModeOn == false) {
            if(life <= 0) {
                gp.gameState = gp.gameOverState;
                gp.ui.commandNum = -1;
                gp.stopMusic();
                gp.playSE(12);
            }
        }
        
    }
    public void interactNPC(int i) {
        
        if(i != 999) {
            
            if(gp.keyHandler.enterPressed) {
                attackCancelled = true;
                gp.npcs[gp.currentMap][i].speak();
            }
            
            gp.npcs[gp.currentMap][i].move(direction);
        }
    }

    public void contactMonster(int i) {

        if(i != 999) {
            if(!invincible && !gp.monsters[gp.currentMap][i].dying) {
        
                gp.playSE(6);

                int damage = gp.monsters[gp.currentMap][i].attack - defense;
                if(damage < 1) {
                    damage = 1;
                }

                life -= damage;
                invincible = true;
                transparent = true;
            }
        }
    }
    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {

        if(i != 999) {

            if(!gp.monsters[gp.currentMap][i].invincible) {
                
                gp.playSE(5);

                if(knockBackPower > 0) {
                    setKnockBack(gp.monsters[gp.currentMap][i], attacker, knockBackPower);
                }
                
                if(gp.monsters[gp.currentMap][i].offBalance) {
                    attack *= 3;
                }

                int damage = attack - gp.monsters[gp.currentMap][i].defense;
                if(damage < 0) {
                    damage = 0;
                }
                gp.monsters[gp.currentMap][i].life -= damage;
                gp.ui.addMessage("dealt " + damage + " damage");

                gp.monsters[gp.currentMap][i].invincible = true;
                gp.monsters[gp.currentMap][i].damageReaction();

                if(gp.monsters[gp.currentMap][i].life <= 0) {
                    gp.monsters[gp.currentMap][i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monsters[gp.currentMap][i].name);
                    gp.ui.addMessage("Exp + " + gp.monsters[gp.currentMap][i].exp);
                    exp += gp.monsters[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }
    public void damageInteractiveTile(int i) {
        if(i != 999 && gp.interactiveTiles[gp.currentMap][i].destructible && gp.interactiveTiles[gp.currentMap][i].canBeDamaged(this) &&
                !gp.interactiveTiles[gp.currentMap][i].invincible) {

            gp.interactiveTiles[gp.currentMap][i].playSE();
            gp.interactiveTiles[gp.currentMap][i].life--;
            gp.interactiveTiles[gp.currentMap][i].invincible = true;

            // Generate particle
            generateParticle(gp.interactiveTiles[gp.currentMap][i], gp.interactiveTiles[gp.currentMap][i]);

            if(gp.interactiveTiles[gp.currentMap][i].life == 0) {
                //gp.iTile[gp.currentMap][i].checkDrop();
                gp.interactiveTiles[gp.currentMap][i] = gp.interactiveTiles[gp.currentMap][i].getDestroyedForm();
            }
        }
    }
    public void damageProjectile(int i) {
        if(i != 999) {
            Entity projectile = gp.projectiles[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile, projectile);
        }
    }
    public void checkLevelUp() {

        int count = 0;
        while(exp >= nextLevelExp) {

            level += 1;
            exp = exp - nextLevelExp;
            nextLevelExp = nextLevelExp + 5;
            life += 2;
            maxLife += 2;
            mana += 1;
            maxMana += 1;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            
            dialogues[0][0] = gp.ui.currentDialogue = "You are level " + level + " now!\n";

            count++;
            if(count > 1) {
                dialogues[0][1] = gp.ui.currentDialogue = "You advanced " + count + " levels";
            }

            startDialogue(this, 0);
        }
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        switch(direction) {
            case "up":
                if(attacking == false) {
                    if(spriteNum == 1) {image = up1;}
                    if(spriteNum == 2) {image = up2;}
                }
                else if(attacking == true) {
                    tempScreenY = screenY - gp.tileSize; // because the attacking sprite is size 2x1 not 1x1, we have to adjust
                    if(spriteNum == 1) {image = attackUp1;}
                    if(spriteNum == 2) {image = attackUp2;}
                }
                if(guarding == true) {
                    image = guardUp;
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
                if(guarding == true) {
                    image = guardDown;
                }
                break;
            case "left":
                if(attacking == false) {
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left2;}
                }
                else if(attacking == true) {
                    tempScreenX = screenX - gp.tileSize; // because the attacking sprite is size 2x1 not 1x1, we have to adjust
                    if(spriteNum == 1) {image = attackLeft1;}
                    if(spriteNum == 2) {image = attackLeft2;}
                }
                if(guarding == true) {
                    image = guardLeft;
                }
                break;
            case "right":
                if(attacking == false) {
                    if(spriteNum == 1) {image = right1;}
                    if(spriteNum == 2) {image = right2;}
                }
                else if(attacking == true) {
                    if(spriteNum == 1) {image = attackRight1;}
                    if(spriteNum == 2) {image = attackRight2;}
                }
                if(guarding == true) {
                    image = guardRight;
                }
                break;
        }

        if(transparent == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        if(drawing == true) {
            g2.drawImage(image, tempScreenX , tempScreenY, null);
            // g2.setColor(Color.red);
            // g2.setStroke(new BasicStroke(1));
            // g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }

        // Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    public void selectItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if(itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword || selectedItem.type == type_axe || selectedItem.type == type_pickaxe) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getAttackImage();
            }
            else if(selectedItem.type == type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
            }
            else if(selectedItem.type == type_light) {
                if(currentLight == selectedItem) {
                    currentLight = null; // unequip the item
                }
                else {
                    currentLight = selectedItem; // equip the item
                }
                lightUpdated = true;
            }
            else if(selectedItem.type == type_consumable) {
                if(selectedItem.use(this)) {
                    if(selectedItem.amount > 1) {
                        selectedItem.amount--;
                    }
                    else {
                        inventory.remove(itemIndex);
                    }
                }
            }
        }
    }
    public boolean canObtainItem(Entity item) { // Add item to the inventory, check if you can stack item
        boolean canObtain = false;
        Entity newItem = gp.entityGenerator.getObject(item.name);

        // CHECK IF STACKABLE
        if(newItem.stackable) {
            int index = searchItemInInventory(newItem.name);

            if(index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            }
            else { // New item so need to check vacancy
                if(inventory.size() != maxInventorySize) {
                    inventory.add(newItem);
                    canObtain = true;
                }
            }
        }
        else { // NOT STACKABLE
            if(inventory.size() != maxInventorySize) {
                inventory.add(newItem);
                canObtain = true;
            }
        }
        return canObtain;
    }
    public void pickUpObject(int i) {
        if(i != 999) {
            // PICKUP ONLY ITEMS
            if(gp.objects[gp.currentMap][i].type == type_pickUpOnly) {
                gp.objects[gp.currentMap][i].use(this);
                gp.objects[gp.currentMap][i] = null;
            }

            else if(gp.objects[gp.currentMap][i].type == type_obstacle) {
                if(keyH.enterPressed) {
                    attackCancelled = true;
                    gp.objects[gp.currentMap][i].interact();
                }
            }

            // INVENTORY ITEMS
            else{
                String text;

                if(canObtainItem(gp.objects[gp.currentMap][i])) {
                    gp.playSE(1);
                    text = "Got a " + gp.objects[gp.currentMap][i].name;
                }
                else {
                    text = "Your inventory is full";
                }
                gp.ui.addMessage(text);
                gp.objects[gp.currentMap][i] = null;
            }
        }
    }
    public int searchItemInInventory(String itemName) {
        int itemIndex = 999;

        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }



}