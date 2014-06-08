package entities;

import collisions.Vector;
import engine.Game;
import graphics.Graphics;
import org.newdawn.slick.Color;

public class Player extends MovingEntity {

    private double xyFacing;
    private double zFacing;
    private boolean onWall;
    private ControlPacket controls;

    public Player(Vector pos) {
        super(pos);
        zFacing = Math.PI / 2;
        setBounds(new Vector(6, 6, 10));
        controls = new ControlPacket();
    }

    @Override
    public void draw() {
        Graphics.drawText("Hello", "Default", 200, 100, Color.green);
    }

    @Override
    public void update() {
        super.update();
        controls.update();
        onWall = placeSolid(new Vector(pos.x, pos.y, pos.z - 1));
        move();
        if (onWall) {
            vel = new Vector(vel.x * .5, vel.y * .5, vel.z);
        }
        if (vel.length() > 20) {
            vel = vel.setLength(20);
        }
        Game.getCamera().pos = pos;
        Game.getCamera().xyDirection = xyFacing;
        Game.getCamera().zDirection = zFacing;
    }

    public void move() {
        double moveSpeed;
        if (controls.sprint) {
            moveSpeed = 4;
        } else {
            moveSpeed = 2;
        }
        if (onWall) {
            if (controls.forward) {
                setMotionRelative(moveSpeed, xyFacing, zFacing);
            }
            if (controls.left) {
                setMotionRelative(moveSpeed, xyFacing + Math.PI * 3 / 2, Math.PI);
            }
            if (controls.right) {
                setMotionRelative(moveSpeed, xyFacing + Math.PI / 2, Math.PI);
            }
            if (controls.jump) {
                vel = new Vector(vel.x, vel.y, 4);
            }
        }
        if (controls.back) {
            setMotionRelative(moveSpeed, xyFacing, zFacing + Math.PI);
        }
        xyFacing -= controls.mouseX / 400;
        zFacing -= controls.mouseY / 400;
        if (zFacing < .2) {
            zFacing = .2;
        }
        if (zFacing > Math.PI - .2) {
            zFacing = Math.PI - .2;
        }
    }

}
