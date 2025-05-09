package config;

import main.GamePanel;

import java.io.*;

public class Config {
    GamePanel gp;
    private final String configPath = "src/main/java/config/config.txt";

    public Config(GamePanel gp) {
        this.gp = gp;
    }
    public void saveConfig() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(configPath));

            // Full screen 
            if(gp.fullScreenOn) {bw.write("On");}
            else {bw.write("Off");}
            bw.newLine();

            // Music volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            // SE volume
            bw.write(String.valueOf(gp.se.volumeScale));
            bw.newLine();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadConfig() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(configPath));

            String s = br.readLine();
            //Full screen
            if(s.equals("On")) {gp.fullScreenOn = true;}
            if(s.equals("Off")) {gp.fullScreenOn = false;}

            // Music volume
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            // SE volume
            s = br.readLine();
            gp.se.volumeScale = Integer.parseInt(s);

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
