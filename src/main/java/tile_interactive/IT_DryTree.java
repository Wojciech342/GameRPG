package tile_interactive;

import java.awt.Color;

import entity.Entity;
import main.GamePanel;

public class IT_DryTree extends InteractiveTile{

    GamePanel gp;

    public IT_DryTree(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
        destructible = true;
        life = 2;

    }
    @Override
    public boolean canBeDamaged(Entity entity) {
        return entity.currentWeapon.type == type_axe;
    }
    public void playSE() {
        gp.playSE(11);
    }
    
    public InteractiveTile getDestroyedForm() {
        InteractiveTile trunk = new IT_Trunk(gp);
        trunk.setPosition(worldX/gp.tileSize, worldY/gp.tileSize);
        return trunk;
    }
    public Color getParticleColor() {
        Color color = new Color(65,50,30);
        return color;
    }
    public int getParticleSize() {
        int size = 6; // 6 pixels
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
