package tile_interactive;

import java.awt.Color;

import entity.Entity;
import main.GamePanel;

public class IT_DestructibleWall extends InteractiveTile {
    GamePanel gp;

    public IT_DestructibleWall (GamePanel gp) {
        super(gp);
        this.gp = gp;

        down1 = setup("/tiles_interactive/destructiblewall", gp.tileSize, gp.tileSize);
        destructible = true;
        life = 3;
    }
    @Override
    public boolean canBeDamaged(Entity entity) {
        return entity.currentWeapon.type == type_pickaxe;
    }
    public void playSE() {
        gp.playSE(20);
    }

    public Color getParticleColor() {
        return new Color(65,65,65);
    }
    public int getParticleSize() {
        int size = 6;
        return size;
    }
    public int getParticleSpeed() {
        int speed = 1;
        return speed;
    }
    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }

}
