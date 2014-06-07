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
//        pos = new Vector(pos.x + .0005, pos.y + .0005, pos.z + .0005);
//        pos = new Vector(pos.x - pos.x % .001, pos.y - pos.y % .001, pos.z - pos.z % .001);
        if (pos.z < 10) {
            System.out.println(pos);
            System.out.println(vel);
        }
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
            setMotionRelative(2, xyFacing, Math.PI / 2);
        }
        if (Keys.pressed(Keyboard.KEY_DOWN)) {
            setMotionRelative(-2, xyFacing, Math.PI / 2);
        }
        if (Keys.pressed(Keyboard.KEY_SPACE)) {
            pos = new Vector(pos.x, pos.y, 15);
        }
        vel = new Vector(vel.x * .5, vel.y * .5, vel.z);
        room.camera.pos = pos;
        room.camera.xyDirection = xyFacing;
        room.camera.zDirection = zFacing;
    }

}
