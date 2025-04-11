package tile_interactive;

import java.awt.Graphics2D;

import entity.Entity;
import main.GamePanel;

public class InteractiveTile extends Entity {

    GamePanel gp;
    public boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;
    }

    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;
        return isCorrectItem;
    }
    public void playSE() {}

    public InteractiveTile getDestroyedForm() {
        InteractiveTile tile = null;
        return tile;
    }
    public void update() {

        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    @Override
    public void draw(Graphics2D g2) { // Removing half transparent effect, method can be removed if you want the transparency while tree is invincible

        // worldX, worldY are what matters here it is coordinates of the object
        int screenX = worldX - gp.player.worldX + gp.player.screenX; // gp.player.screenX is final, always will be screenWidth/2
        int screenY = worldY - gp.player.worldY + gp.player.screenY; // gp.player.worldX is changing depending on the whole map
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(down1, screenX, screenY, null);
        }
    }
}
