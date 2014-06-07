package entities;

import collisions.Collisions;
import collisions.Vector;

public class MovingEntity extends Entity {

    protected Vector prevPos;
    protected Vector vel;
    protected Vector gravity;

    public MovingEntity(Vector pos) {
        super(pos);
        this.pos = new Vector(pos.x + .5, pos.y, pos.z);
        prevPos = pos;
        vel = new Vector(0, 0, 0);
        gravity = new Vector(0, 0, -.1);
    }

    public Vector getPrevPos() {
        return prevPos;
    }

    public Vector getSpeed() {
        return vel;
    }

    public double getXYDirection() {
        return Math.atan2(vel.y, vel.x);
    }

    public double getZDirection() {
        return Math.acos(vel.z / getSpeed().length());
    }

    public void setDirection(double xyDirection, double zDirection) {
        setSpeedDirection(getSpeed().length(), xyDirection, zDirection);
    }

    public void setMotionRelative(double speed, double xyDirection, double zDirection) {
        Vector oldVel = vel;
        setSpeedDirection(speed, xyDirection, zDirection);
        vel = vel.add(oldVel);
    }

    public void setSpeed(double speed) {
        setSpeedDirection(speed, getXYDirection(), getZDirection());
    }

    public void setSpeedDirection(double speed, double xyDirection, double zDirection) {
        double x = speed * Math.sin(zDirection) * Math.cos(xyDirection);
        double y = speed * Math.sin(zDirection) * Math.sin(xyDirection);
        double z = speed * Math.cos(zDirection);
        vel = new Vector(x, y, z);
    }

    @Override
    public void update() {
        super.update();
        prevPos = pos;
        vel = vel.add(gravity);
        pos = Collisions.collideAndSlide(room, pos, getBounds().size, vel, gravity);
    }
}
