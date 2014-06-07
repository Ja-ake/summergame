package collisions;

public class Vector {

    public final double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }

    Vector cross(Vector v) {
        return new Vector(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }

    public Vector divide(double d) {
        return new Vector(x / d, y / d, z / d);
    }

    public Vector divide(Vector v) {
        return new Vector(x / v.x, y / v.y, z / v.z);
    }

    double dot(Vector v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public double length() {
        return Math.sqrt(squaredLength());
    }

    public Vector multiply(double d) {
        return new Vector(x * d, y * d, z * d);
    }

    Vector multiply(Vector v) {
        return new Vector(x * v.x, y * v.y, z * v.z);
    }

    Vector normalize() {
        return setLength(1);
    }

    public Vector setLength(double d) {
        double l = length();
        return new Vector(x * d / l, y * d / l, z * d / l);
    }

    double squaredLength() {
        return x * x + y * y + z * z;
    }

    public Vector subtract(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y + ", z: " + z;
    }
}
