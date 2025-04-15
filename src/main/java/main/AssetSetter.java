package main;

import data.Progress;
import entity.Entity;
import entity.NPC_BigRock;
import entity.NPC_Merchant;
import entity.NPC_OldMan;
import monster.MON_Bat;
import monster.MON_GreenSlime;
import monster.MON_RedSlime;
import monster.MON_Orc;
import monster.MON_SkeletonLord;
import object.OBJ_Axe;
import object.OBJ_BlueHeart;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Door_Iron;
import object.OBJ_Key;
import object.OBJ_Pickaxe;
import object.OBJ_Shield_Blue;
import object.OBJ_Tent;
import tile_interactive.IT_DestructibleWall;
import tile_interactive.IT_DryTree;
import tile_interactive.IT_MetalPlate;

public class AssetSetter {
    private final GamePanel gp;
    private int mapNum;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    public void placeObjectsOnMap() {
        mapNum = 0;
        placeEntity(new OBJ_Tent(gp), 28, 39, gp.objects);
        placeEntity(new OBJ_Axe(gp), 35, 32, gp.objects);
        placeEntity(new OBJ_Door(gp), 14, 28, gp.objects);
        placeEntity(new OBJ_Door(gp), 12, 12, gp.objects);
        placeChestWithLoot(new OBJ_Key(gp), 18, 20);
        placeChestWithLoot(new OBJ_Shield_Blue(gp), 30, 29);

        mapNum = 2;
        placeEntity(new OBJ_Door_Iron(gp), 18, 23, gp.objects);
        placeChestWithLoot(new OBJ_Pickaxe(gp), 40, 41);
        placeChestWithLoot(new OBJ_Pickaxe(gp), 13, 16);
        placeChestWithLoot(new OBJ_Pickaxe(gp), 26, 34);
        placeChestWithLoot(new OBJ_Pickaxe(gp), 27, 15);

        mapNum = 3;
        placeEntity(new OBJ_Door_Iron(gp), 25, 15, gp.objects);
        placeEntity(new OBJ_BlueHeart(gp), 25, 8, gp.objects);
    }
    private void placeChestWithLoot(Entity loot, int col, int row) {
        OBJ_Chest chest = new OBJ_Chest(gp);
        chest.setLoot(loot);
        placeEntity(chest, col, row, gp.objects);
    }
    public void placeNPCsOnMap() {
        mapNum = 0;
        placeEntity(new NPC_OldMan(gp), 21, 21, gp.npcs);

        mapNum = 1;
        placeEntity(new NPC_Merchant(gp), 12, 7, gp.npcs);

        mapNum = 2;
        placeEntity(new NPC_BigRock(gp), 20, 2, gp.npcs);
        placeEntity(new NPC_BigRock(gp), 11, 18, gp.npcs);
        placeEntity(new NPC_BigRock(gp), 23, 14, gp.npcs);
    }
    public void placeMonstersOnMap() {
        mapNum = 0;
        placeEntity(new MON_GreenSlime(gp), 21, 38, gp.monsters);
        placeEntity(new MON_GreenSlime(gp), 21, 39, gp.monsters);
        placeEntity(new MON_GreenSlime(gp), 23, 42, gp.monsters);
        placeEntity(new MON_GreenSlime(gp), 24, 37, gp.monsters);
        placeEntity(new MON_GreenSlime(gp), 34, 42, gp.monsters);
        placeEntity(new MON_GreenSlime(gp), 38, 42, gp.monsters);

        placeEntity(new MON_RedSlime(gp), 35, 8, gp.monsters);
        placeEntity(new MON_RedSlime(gp), 40, 8, gp.monsters);
        placeEntity(new MON_RedSlime(gp), 35, 11, gp.monsters);
        placeEntity(new MON_RedSlime(gp), 40, 11, gp.monsters);

        placeEntity(new MON_Orc(gp), 12, 33, gp.monsters);

        mapNum = 2;
        placeEntity(new MON_Bat(gp), 39, 26, gp.monsters);
        placeEntity(new MON_Bat(gp), 36, 25, gp.monsters);
        placeEntity(new MON_Bat(gp), 34, 39, gp.monsters);

        mapNum = 3;
        if (!Progress.skeletonLordDefeated) {
            placeEntity(new MON_SkeletonLord(gp), 23, 16, gp.monsters);
        }

    }
    public void setInteractiveTile() {
        mapNum = 0;
        placeEntity(new IT_DryTree(gp), 27, 12, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 28, 12, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 29, 12, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 30, 12, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 31, 12, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 32, 12, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 33, 12, gp.interactiveTiles);

        setPathToChestWithBlueShield();
        setPathToShop();

        // Passage stop
        placeEntity(new IT_DryTree(gp), 36, 30, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 31, 21, gp.interactiveTiles);

        mapNum = 2;
        placeEntity(new IT_DestructibleWall(gp), 18, 30, gp.interactiveTiles);
        placeEntity(new IT_DestructibleWall(gp), 17, 31, gp.interactiveTiles);
        placeEntity(new IT_DestructibleWall(gp), 17, 32, gp.interactiveTiles);

        placeEntity(new IT_MetalPlate(gp), 20, 22, gp.interactiveTiles);
        placeEntity(new IT_MetalPlate(gp), 8, 17, gp.interactiveTiles);
        placeEntity(new IT_MetalPlate(gp), 39, 31, gp.interactiveTiles);
    }
    private void setPathToChestWithBlueShield() {
        placeEntity(new IT_DryTree(gp), 25, 27, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 26, 27, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 27, 27, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 27, 28, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 27, 29, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 27, 30, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 27, 31, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 28, 31, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 29, 31, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 30, 31, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 30, 30, gp.interactiveTiles);
    }
    private void setPathToShop() {
        placeEntity(new IT_DryTree(gp), 18, 40, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 17, 40, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 16, 40, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 15, 40, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 14, 40, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 13, 40, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 13, 41, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 12, 41, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 11, 41, gp.interactiveTiles);
        placeEntity(new IT_DryTree(gp), 10, 41, gp.interactiveTiles);
    }
    private void placeEntity(Entity entity, int col, int row, Entity[][] entityArray) {
        Entity[] entities = entityArray[mapNum];
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] == null) {
                entity.setPosition(col, row);
                entities[i] = entity;
                break;
            }
        }
    }
}
