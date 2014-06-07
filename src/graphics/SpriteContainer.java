package graphics;

import engine.Sprite;
import files.TextureLoader;
import java.util.HashMap;

public class SpriteContainer {

    private static HashMap<String, Sprite> spriteMap;
    private static TextureLoader texLoader;

    public static void add(String name, String path) {
        spriteMap.put(name, new Sprite(texLoader, path));
    }

    public static void add(String name, String path, int num) {
        spriteMap.put(name, new Sprite(texLoader, path, num));
    }

    public static void create() {
        System.out.println("Loading sprites");
        spriteMap = new HashMap();
        texLoader = new TextureLoader();
        add("Wall", "wall");
        add("Grass", "grass");
    }

    public static Sprite get(String name) {
        return spriteMap.get(name);
    }
}
