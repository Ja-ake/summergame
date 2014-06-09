package files;

import collisions.Vector;
import engine.Room;
import entities.Duck;
import entities.Player;
import entities.Surface;
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
    private static void chooseEntityChar(char c, int x, int y, Room r) {
    }

    //Change this
    private static void chooseEntityColor(int c, int x, int y, Room r) {
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

    public static Room loadRandomTerrain(int width, int height) {
        int detail = 4;
        int surfaceSize = 64;
        Room room = new Room(width * detail, height * detail);
        Noise n = new Noise(100 * Math.random());
        double[][] heightMap = new double[width][height];
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[0].length; j++) {
                heightMap[i][j] = 100 * n.multi(i, j, 6, .001);
            }
        }
        for (int i = 0; i < heightMap.length - surfaceSize; i += surfaceSize) {
            for (int j = 0; j < heightMap[0].length - surfaceSize; j += surfaceSize) {
                Vector[][] a = new Vector[surfaceSize + 1][surfaceSize + 1];
                for (int k = 0; k <= surfaceSize; k++) {
                    for (int l = 0; l <= surfaceSize; l++) {
                        a[k][l] = vectorAt(heightMap, i + k, j + l, detail);
                    }
                }
                new Surface(a).addToRoom(room);
            }
        }
        new Player(vectorAt(heightMap, width / 2, height / 2, detail).add(new Vector(0, 0, 20))).addToRoom(room);
        return room;
    }

    private static Vector vectorAt(double[][] heightMap, int i, int j, int detail) {
        return new Vector(i * detail, j * detail, heightMap[i][j]);
    }
}
