package files;

import collisions.Vector;
import engine.Room;
import entities.Player;
import entities.Wall;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;

public class Loader {

    public static final String ROOMS_PATH = "rooms\\";
    public static final int BLOCK_SIZE = 32;

    public static Room loadText(String name) {
        try {
            String path = ROOMS_PATH + name + ".txt";
            List<String> text = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            int width = text.get(0).length();
            int height = text.size();
            Room room = new Room(width * BLOCK_SIZE, height * BLOCK_SIZE);
            for (int i = 0; i < text.size(); i++) {
                for (int j = 0; j < text.get(i).length(); j++) {
                    char c = text.get(i).charAt(j);
                    int x = j * BLOCK_SIZE + BLOCK_SIZE / 2;
                    int y = height * BLOCK_SIZE - BLOCK_SIZE - i * BLOCK_SIZE + BLOCK_SIZE / 2;
                    chooseEntityChar(c, x, y, room);
                }
            }
            return room;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Room loadImage(String name) {
        try {
            String path = ROOMS_PATH + name + ".png";
            BufferedImage image = ImageIO.read(new File(path));
            int width = image.getWidth();
            int height = image.getHeight();
            Room room = new Room(width * BLOCK_SIZE, height * BLOCK_SIZE);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int color = image.getRGB(j, i);
                    int x = j * BLOCK_SIZE + BLOCK_SIZE / 2;
                    int y = height * BLOCK_SIZE - BLOCK_SIZE - i * BLOCK_SIZE + BLOCK_SIZE / 2;
                    chooseEntityColor(color, x, y, room);
                }
            }
            return room;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //Change this
    public static void chooseEntityChar(char c, int x, int y, Room r) {
    }

    //Change this
    public static void chooseEntityColor(int c, int x, int y, Room r) {
        new Wall(new Vector(x, y, -16)).addToRoom(r);
        switch (c) {
            case 0xFF000000:
                new Wall(new Vector(x, y, 16)).addToRoom(r);
                break;
            case 0xFF0000FF:
                new Player(new Vector(x, y, 16)).addToRoom(r);
                break;
        }
    }
}
