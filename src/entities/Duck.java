package entities;

import collisions.Vector;
import engine.Game;

public class Duck extends MovingEntity {
    
    public Duck(Vector pos) {
        super(pos);
        setModel("ducky");
        setSprite("Wall");
    }
    
    @Override
    public void update() {
        pos = Game.getCamera().getPos().add(new Vector(0, 0, -10));
    }
    
}
