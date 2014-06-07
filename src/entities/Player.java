package entities;

import collisions.Vector;
import engine.Keys;
import org.lwjgl.input.Keyboard;

public class Player extends MovingEntity {

    private double xyFacing;
    private double zFacing;

    public Player(Vector pos) {
        super(pos);
        zFacing = Math.PI / 2;
        setBounds(new Vector(6, 6, 10));
    }

    @Override
    public void update() {
        super.update();
        System.out.println(pos);
        if (Keys.pressed(Keyboard.KEY_LEFT)) {
            xyFacing += .05;
        }
        if (Keys.pressed(Keyboard.KEY_RIGHT)) {
            xyFacing -= .05;
        }
        if (Keys.pressed(Keyboard.KEY_W)) {
            if (zFacing > .05) {
                zFacing -= .05;
            }
        }
        if (Keys.pressed(Keyboard.KEY_S)) {
            if (zFacing < Math.PI - .05) {
                zFacing += .05;
            }
        }
        if (Keys.pressed(Keyboard.KEY_UP)) {
            setMotionRelative(.5, xyFacing, Math.PI / 2);
        }
        vel = new Vector(vel.x * .5, vel.y * .5, vel.z);
        room.camera.pos = pos;
        room.camera.xyDirection = xyFacing;
        room.camera.zDirection = zFacing;
    }

}
