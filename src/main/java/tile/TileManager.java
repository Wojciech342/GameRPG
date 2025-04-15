package tile;

import main.GamePanel;
import main.UtilityTool;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TileManager {
    GamePanel gp;
    public Tile[] tiles;
    public int mapTileNum[][][];
    //boolean drawPath = true;
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();


    public TileManager(GamePanel gp) {
        this.gp = gp;

        InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // GETTING TILE NAMES AND COLLISION INFO FROM THE FILE
        String line;

        try {
            while((line = br.readLine()) != null) {
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        tiles = new Tile[fileNames.size()];
        setTilesData();

        // GET THE maxWorldCol & Row
        is = getClass().getResourceAsStream("/maps/worldmap.txt");
        br = new BufferedReader(new InputStreamReader(is));

        // IMPORTANT THE MAP MUST BE SQUARE OTHERWISE THE CODE WONT WORK (THE CODE CAN BE CHANGED)
        try{
            String line2 = br.readLine();
            String[] maxTile = line2.split(" ");

            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;
            mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        loadMap("/maps/worldmap.txt", 0);
        loadMap("/maps/indoor01.txt", 1);
        loadMap("/maps/dungeon01.txt", 2);
        loadMap("/maps/dungeon02.txt", 3);
    }

    public void setTilesData() {
        for(int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);
            boolean collision = collisionStatus.get(i).equals("true");
            setup(i, fileName, collision);
        }
    }
    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try{
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName));
            tiles[index].image = uTool.scaleImage(tiles[index].image, gp.tileSize, gp.tileSize);
            tiles[index].collision = collision;

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap(String filePath, int map) {
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {

                String line = br.readLine();

                while(col < gp.maxWorldCol) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
                
            }
            br.close();
        }catch (Exception e) {

        }
    }
    public void draw(Graphics2D g2) {
        
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            // worldX - gp.player.worldX: This calculates the offset of the tile from the player in the world.
            // gp.player.screenX: This adjustment centers the player on the screen by ensuring their screen position is static (e.g., in the middle of the screen).

            int screenX = worldX - gp.player.worldX + gp.player.screenX; // gp.player.screenX is final, always will be screenWidth/2
            int screenY = worldY - gp.player.worldY + gp.player.screenY; // gp.player.worldX is changing depending on the whole map
            
            // Even if the map is ending the player needs to be in the center, so thats why we add gp.player.screenX/(Y) 
            // So the screenX and screenY can be negative, they are relative to the player
            // So if the player is in the middle of the world then the tiles in the top left will have negative coordinates
            
            // So like the tiles are always relative to the player, So it all depends on gp.player.worldY/X
            
            // IT ONLY DRAWS TILES HERE

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tiles[tileNum].image, screenX, screenY, null);
            }

            worldCol++;
            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow ++;
            }
        }
        

        // DRAW PATH

        // if(drawPath == true) {
        //     g2.setColor(new Color(255,0,0,70));

        //     for(int i = 0; i < gp.pFinder.pathList.size(); i++) {

        //         int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
        //         int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
        //         int screenX = worldX - gp.player.worldX + gp.player.screenX;
        //         int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //         g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
        //     }
        // }
    }
}
