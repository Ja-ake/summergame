package entities;

import collisions.Vector;
import engine.Keys;
import org.lwjgl.input.Keyboard;

public class Player extends MovingEntity {

    private double facing;

    public Player(Vector pos) {
        super(pos);
    }

    @Override
    public void update() {
        super.update();
        if (Keys.pressed(Keyboard.KEY_LEFT)) {
            facing += .05;
        }
        if (Keys.pressed(Keyboard.KEY_RIGHT)) {
            facing -= .05;
        }
        if (Keys.pressed(Keyboard.KEY_UP)) {
            setMotionRelative(.5, facing, Math.PI / 2);
        }
        vel = vel.multiply(.5);
        room.camera.pos = pos;
        room.camera.xyDirection = facing;
    }

}
