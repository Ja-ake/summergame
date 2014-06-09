package entities;

import collisions.Vector;

public class Duck extends Entity {

    public Duck(Vector pos) {
        super(pos);
        setModel("ducky");
    }
    
}
