package graphics;

import graphics.Texture;
import files.TextureLoader;
import java.io.IOException;
import java.util.ArrayList;

public class Sprite {

    public String baseSource = "sprites\\";
    private ArrayList<Texture> textures = new ArrayList();

    public Sprite(TextureLoader loader, String ref) {
        try {
            textures = loader.getTexture(baseSource + ref + ".png", 1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Sprite(TextureLoader loader, String ref, int n) {
        try {
            textures = loader.getTexture(baseSource + ref + ".png", n);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public int getWidth() {
        return textures.get(0).getImageWidth();
    }

    public int getHeight() {
        return textures.get(0).getImageHeight();
    }

    public Texture getTexture(int index) {
        return textures.get(index % textures.size());
    }
}