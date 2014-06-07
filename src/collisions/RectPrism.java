package collisions;

public class RectPrism {

    private Vector center, size;

    public RectPrism(Vector center, Vector size) {
        this.center = center;
        this.size = size;
    }

    public RectPrism(Triangle t) {
        double minX = Math.min(t.p1.x, Math.min(t.p2.x, t.p3.x));
        double maxX = Math.max(t.p1.x, Math.max(t.p2.x, t.p3.x));
        double minY = Math.min(t.p1.y, Math.min(t.p2.y, t.p3.y));
        double maxY = Math.max(t.p1.y, Math.max(t.p2.y, t.p3.y));
        double minZ = Math.min(t.p1.z, Math.min(t.p2.z, t.p3.z));
        double maxZ = Math.max(t.p1.z, Math.max(t.p2.z, t.p3.z));
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

    public boolean intersects(RectPrism other) {
        return corner1().x < other.corner2().x && other.corner1().x < corner2().x
                && corner1().y < other.corner2().y && other.corner1().y < corner2().y
                && corner1().z < other.corner2().z && other.corner1().z < corner2().z;
    }

    public void setSize(Vector size) {
        this.size = size;
    }
}
