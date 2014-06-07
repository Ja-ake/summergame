package entities;

import collisions.Vector;

public class Wall extends Solid {

    public Wall(Vector pos) {
        super(pos);
        bounds.size = new Vector(16, 16, 16);
        setSprite("Wall");
    }

}
