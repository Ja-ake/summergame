package entities;

import collisions.CollisionPacket;
import collisions.Vector;
import engine.Game;
import engine.MouseInput;
import graphics.Graphics;
import org.newdawn.slick.Color;

public class Player extends MovingEntity {

    private double xyFacing;
    private double zFacing;
    private boolean onWall;
    private double cameraFollowDist;
    private final ControlPacket controls;

    public Player(Vector pos) {
        super(pos);
        zFacing = Math.PI / 2;
        setBounds(new Vector(6, 6, 10));
        controls = new ControlPacket();
        flatFriction = .05;
        percentFriction = .2;
        cameraFollowDist = 30;
        setModel("sphere");
        setSprite("Grass");
    }

    @Override
    public void collideWithSolid(CollisionPacket c) {
        Vector nor = c.intersectionNormal;
        double t = Math.acos(vel.dot(nor) / vel.length() / nor.length());
        double newSpeed = Math.sin(t) * vel.length();
        vel = nor.cross(vel).cross(nor).setLength(newSpeed);
    }

    @Override
    public void draw() {
        super.draw();
        Graphics.drawText("Hello", "Default", 200, 100, Color.green);
    }

    public void move() {
        double moveSpeed;
        if (controls.sprint) {
            moveSpeed = .2;
        } else {
            moveSpeed = .1;
        }
        if (onWall) {
            if (controls.forward) {
                setMotionRelative(moveSpeed * 2, xyFacing, Math.PI / 2);
            }
            if (controls.back) {
                setMotionRelative(moveSpeed, xyFacing + Math.PI, Math.PI / 2);
            }
            if (controls.right) {
                setMotionRelative(moveSpeed, xyFacing + Math.PI * 3 / 2, Math.PI / 2);
            }
            if (controls.left) {
                setMotionRelative(moveSpeed, xyFacing + Math.PI / 2, Math.PI / 2);
            }
            if (controls.jump) {
                vel = new Vector(vel.x, vel.y, 4);
            }
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

    @Override
    public void update() {
        controls.update();
        onWall = placeSolid(new Vector(pos.x, pos.y, pos.z - 1));
        move();
        if (onWall) {
            friction();
        }
        if (vel.length() > 20) {
            vel = vel.setLength(20);
        }
        super.update();
        cameraFollowDist -= MouseInput.getMouseDWheel();
        if (cameraFollowDist < 15) {
            cameraFollowDist = 15;
        }
        Game.getCamera().follow(pos, cameraFollowDist, xyFacing, zFacing);
    }

}
