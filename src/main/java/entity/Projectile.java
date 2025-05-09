package entity;

import main.GamePanel;

public class Projectile extends Entity {

    GamePanel gp;

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
    }
    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {

        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
    }
    public void update() {

        if(user == gp.player) {
            int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monsters);
            if(monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, this, attack*(gp.player.level), knockBackPower);
                generateParticle(user.projectile, gp.monsters[gp.currentMap][monsterIndex]);
                alive = false;
            }
        }
        else {
            boolean contactPlayer = gp.collisionChecker.checkPlayer(this);
            if(gp.player.invincible == false && contactPlayer == true) {
                damagePlayer(attack);
                generateParticle(user.projectile, gp.player);
                alive = false;
            }
        }

        switch(direction) {
        case "up": worldY -= speed; break;
        case "down": worldY += speed; break;
        case "left": worldX -= speed; break;
        case "right": worldX += speed; break;
        }

        life--;
        if(life <= 0) {
            alive = false;
        }
        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) {
                spriteNum = 2;
            }
            if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    public boolean haveResource(Entity user) {
        boolean haveResource = false;
        return haveResource;
    }
    public void subtractResource(Entity user) {}
    
}
