package engine;

import collisions.RectPrism;
import collisions.Triangle;
import collisions.Vector;
import entities.Entity;
import java.util.ArrayList;

public class BoundingBox extends RectPrism {

    public Entity e;

    public BoundingBox(Entity e, Vector size) {
        super(e.getPos(), size);
        this.e = e;
    }

    @Override
    public Vector getCenter() {
        return e.getPos();
    }

    public ArrayList<Triangle> getTriangles() {
        ArrayList<Triangle> r = new ArrayList();
        double x1 = corner1().x;
        double y1 = corner1().y;
        double z1 = corner1().z;
        double x2 = corner2().x;
        double y2 = corner2().y;
        double z2 = corner2().z;
        //x1 face
        r.add(new Triangle(x1, y1, z1, x1, y2, z1, x1, y1, z2));
        r.add(new Triangle(x1, y2, z2, x1, y2, z1, x1, y1, z2));
        //x2 face
        r.add(new Triangle(x2, y1, z1, x2, y2, z1, x2, y1, z2));
        r.add(new Triangle(x2, y2, z2, x2, y2, z1, x2, y1, z2));
        //y1 face
        r.add(new Triangle(x1, y1, z1, x2, y1, z1, x1, y1, z2));
        r.add(new Triangle(x2, y1, z2, x2, y1, z1, x1, y1, z2));
        //y2 face
        r.add(new Triangle(x1, y2, z1, x2, y2, z1, x1, y2, z2));
        r.add(new Triangle(x2, y2, z2, x2, y2, z1, x1, y2, z2));
        //z1 face
        r.add(new Triangle(x1, y1, z1, x2, y1, z1, x1, y2, z1));
        r.add(new Triangle(x2, y2, z1, x2, y1, z1, x1, y2, z1));
        //z2 face
        r.add(new Triangle(x1, y1, z2, x2, y1, z2, x1, y2, z2));
        r.add(new Triangle(x2, y2, z2, x2, y1, z2, x1, y2, z2));
        return r;
    }
}
