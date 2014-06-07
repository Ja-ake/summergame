package collisions;

class Plane {

    double[] equation;
    Vector origin;
    Vector normal;

    Plane(Vector origin, Vector normal) {
        this.normal = normal;
        this.origin = origin;
        equation = new double[4];
        equation[0] = normal.x;
        equation[1] = normal.y;
        equation[2] = normal.z;
        equation[3] = -(normal.x * origin.x + normal.y * origin.y + normal.z * origin.z);
    }

    // Construct from triangle:
    Plane(Vector p1, Vector p2, Vector p3) {
        normal = (p2.subtract(p1)).cross(p3.subtract(p1));
        normal = normal.normalize();
        origin = p1;
        equation = new double[4];
        equation[0] = normal.x;
        equation[1] = normal.y;
        equation[2] = normal.z;
        equation[3] = -(normal.x * origin.x + normal.y * origin.y + normal.z * origin.z);
    }

    boolean isFrontFacingTo(Vector direction) {
        double dot = normal.dot(direction);
        return (dot <= 0);
    }

    double signedDistanceTo(Vector point) {
        return (point.dot(normal)) + equation[3];
    }
}
