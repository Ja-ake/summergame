package entities;

import collisions.RectPrism;
import collisions.Triangle;
import collisions.Vector;
import java.util.ArrayList;

public abstract class Solid extends Entity {

    public Solid(Vector pos) {
        super(pos);
    }

    public abstract ArrayList<Triangle> getTriangles();

    public abstract ArrayList<Triangle> getTrianglesTouching(RectPrism r);
}
