package data;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;

import main.GamePanel;

public class SaveLoad {
    
    GamePanel gp;
    private final String saveDataFilepath = "src/main/java/data/save.dat";
    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }
    public void save() {

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveDataFilepath))) {

            DataStorage ds = new DataStorage();

            // PLAYER STATS
            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.mana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;

            // PLAYER INVENTORY
            for(int i = 0; i < gp.player.inventory.size(); i++) {
                ds.itemNames.add(gp.player.inventory.get(i).name);
                ds.itemAmounts.add(gp.player.inventory.get(i).amount);
            }

            // PLAYER EQUIPMENT
            ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
            ds.currentShieldSlot = gp.player.getCurrentShieldSlot();

            // OBJECTS ON MAP
            ds.mapObjectNames = new String[gp.maxMap][gp.objects[1].length];
            ds.mapObjectWorldX = new int[gp.maxMap][gp.objects[1].length];
            ds.mapObjectWorldY = new int[gp.maxMap][gp.objects[1].length];
            ds.mapObjectLootNames = new String[gp.maxMap][gp.objects[1].length];
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.objects[1].length];

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                
                for(int i = 0; i < gp.objects[1].length; i++) {

                    if(gp.objects[mapNum][i] == null) {
                        ds.mapObjectNames[mapNum][i] = "NA";
                    }
                    else {
                        ds.mapObjectNames[mapNum][i] = gp.objects[mapNum][i].name;
                        ds.mapObjectWorldX[mapNum][i] = gp.objects[mapNum][i].worldX;
                        ds.mapObjectWorldY[mapNum][i] = gp.objects[mapNum][i].worldY;
                        if(gp.objects[mapNum][i].loot != null) {
                            ds.mapObjectLootNames[mapNum][i] = gp.objects[mapNum][i].loot.name;
                        }
                        ds.mapObjectOpened[mapNum][i] = gp.objects[mapNum][i].opened;
                    }
                }
            }
            
            // Write the data storage object
            oos.writeObject(ds);

        } catch (Exception e) {
            
            System.out.println("Save exception");
        }
    }
    public void load() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveDataFilepath))) {

            // Read the DataStorage object
            DataStorage ds = (DataStorage)ois.readObject();

            // PLAYER STATS
            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.life = ds.life;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.mana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;

            // PLAYER INVENTORY
            gp.player.inventory.clear();
            for(int i = 0; i < ds.itemNames.size(); i++) {
                gp.player.inventory.add(gp.entityGenerator.getObject(ds.itemNames.get(i)));
                gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
            }

            // PLAYER EQUIPMENT
            gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);
            gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);
            gp.player.getAttack();
            gp.player.getDefense();
            gp.player.getAttackImage();

            // OBJECTS ON MAP
            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                
                for(int i = 0; i < gp.objects[1].length; i++) {

                    if(ds.mapObjectNames[mapNum][i].equals("NA")) {
                        gp.objects[mapNum][i] = null;
                    }
                    else {
                        gp.objects[mapNum][i] = gp.entityGenerator.getObject(ds.mapObjectNames[mapNum][i]);
                        gp.objects[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
                        gp.objects[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i];
                        if(ds.mapObjectLootNames[mapNum][i] != null) {
                            gp.objects[mapNum][i].setLoot(gp.entityGenerator.getObject(ds.mapObjectLootNames[mapNum][i]));
                        }
                        gp.objects[mapNum][i].opened = ds.mapObjectOpened[mapNum][i];
                        if(gp.objects[mapNum][i].opened == true) {
                            gp.objects[mapNum][i].down1 = gp.objects[mapNum][i].image2;
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            System.out.println("Load exception");
            //e.printStackTrace();
        }
        
    }
}
