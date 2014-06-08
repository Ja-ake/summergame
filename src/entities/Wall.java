package entities;

import collisions.RectPrism;
import collisions.Triangle;
import collisions.Vector;
import graphics.Graphics;
import java.util.ArrayList;

public class Wall extends Solid {

    public Wall(Vector pos) {
        super(pos);
        bounds.setSize(new Vector(16, 16, 16));
        setSprite("Wall");
    }

    @Override
    public void draw() {
        //Top
        Graphics.drawSprite(sprite, pos.x, pos.y, pos.z + 16, (int) Math.round(imageIndex));
        //Bottom
        Graphics.drawSprite(sprite, pos.x, pos.y, pos.z - 16, (int) Math.round(imageIndex));

        //Sides
        Graphics.drawSprite(sprite, pos.x, pos.y + 16, pos.z, (int) Math.round(imageIndex), Math.PI / 2, new Vector(1, 0, 0));
        Graphics.drawSprite(sprite, pos.x, pos.y - 16, pos.z, (int) Math.round(imageIndex), Math.PI / 2, new Vector(1, 0, 0));
        Graphics.drawSprite(sprite, pos.x + 16, pos.y, pos.z, (int) Math.round(imageIndex), Math.PI / 2, new Vector(0, 1, 0));
        Graphics.drawSprite(sprite, pos.x - 16, pos.y, pos.z, (int) Math.round(imageIndex), Math.PI / 2, new Vector(0, 1, 0));
    }

    @Override
    public ArrayList<Triangle> getTriangles() {
        return bounds.getTriangles();
    }

    @Override
    public ArrayList<Triangle> getTrianglesTouching(RectPrism rp) {
        ArrayList<Triangle> r = new ArrayList();
        for (Triangle t : getTriangles()) {
            if (t.couldCollide(rp)) {
                r.add(t);
            }
        }
        return r;
    }

}
