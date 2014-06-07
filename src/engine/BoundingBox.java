package engine;

import collisions.Triangle;
import collisions.Vector;
import entities.Entity;
import java.util.ArrayList;

public class BoundingBox {

    public Entity e;
    public Vector size;

    public BoundingBox(Entity e, Vector size) {
        this.e = e;
        this.size = size;
    }

    public boolean contains(Vector v) {
        return corner1().x < v.x && v.x < corner2().x
                && corner1().y < v.y && v.y < corner2().y
                && corner1().z < v.z && v.z < corner2().z;
    }

    private Vector corner1() {
        return e.getPos().add(size);
    }

    private Vector corner2() {
        return e.getPos().subtract(size);
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

    public boolean intersects(BoundingBox other) {
        return corner1().x > other.corner2().x && other.corner1().x > corner2().x
                && corner1().y > other.corner2().y && other.corner1().y > corner2().y
                && corner1().z > other.corner2().z && other.corner1().z > corner2().z;
    }
}
