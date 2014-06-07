package collisions;

import java.util.ArrayList;

public class RectPrism {

    private Vector center, size;

    public RectPrism(Vector center, Vector size) {
        this.center = center;
        this.size = size;
    }

    public RectPrism(ArrayList<Vector> a) {
        double minX, maxX, minY, maxY, minZ, maxZ;
        minX = maxX = a.get(0).x;
        minY = maxY = a.get(0).y;
        minZ = maxZ = a.get(0).z;
        for (int i = 1; i < a.size(); i++) {
            Vector v = a.get(i);
            minX = Math.min(minX, v.x);
            maxX = Math.max(maxX, v.x);
            minY = Math.min(minY, v.y);
            maxY = Math.max(maxY, v.y);
            minZ = Math.min(minZ, v.z);
            maxZ = Math.max(maxZ, v.z);
        }
        center = new Vector((maxX + minX) / 2, (maxY + minY) / 2, (maxZ + minZ) / 2);
        size = new Vector((maxX - minX) / 2, (maxY - minY) / 2, (maxZ - minZ) / 2);
    }

    public boolean contains(Vector v) {
        return corner1().x < v.x && v.x < corner2().x
                && corner1().y < v.y && v.y < corner2().y
                && corner1().z < v.z && v.z < corner2().z;
    }

    protected Vector corner1() {
        return getCenter().subtract(getSize());
    }

    protected Vector corner2() {
        return getCenter().add(getSize());
    }

    public Vector getCenter() {
        return center;
    }

    public Vector getSize() {
        return size;
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

    public boolean intersects(RectPrism other) {
        return corner1().x < other.corner2().x && other.corner1().x < corner2().x
                && corner1().y < other.corner2().y && other.corner1().y < corner2().y
                && corner1().z < other.corner2().z && other.corner1().z < corner2().z;
    }

    public void setSize(Vector size) {
        this.size = size;
    }
}
