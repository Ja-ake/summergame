package entities;

import collisions.CollisionPacket;
import collisions.Collisions;
import collisions.Vector;

public class MovingEntity extends Entity {

    protected Vector prevPos;
    protected Vector vel;
    protected Vector gravity;
    protected double flatFriction;
    protected double percentFriction;

    public MovingEntity(Vector pos) {
        super(pos);
        this.pos = new Vector(pos.x + .5, pos.y, pos.z);
        prevPos = pos;
        vel = new Vector(0, 0, 0);
        gravity = new Vector(0, 0, -.2);
    }

    public void collideWithSolid(CollisionPacket c) {
    }

    protected void friction() {
        vel = new Vector(vel.x * (1 - percentFriction), vel.y * (1 - percentFriction), vel.z);
        if (vel.length() > flatFriction) {
            vel = vel.setLength(vel.length() - flatFriction);
        } else {
            vel = new Vector(0, 0, 0);
        }
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

    public boolean placeSolid(Vector v) {
        CollisionPacket c = new CollisionPacket();
        c.eRadius = getBounds().getSize();
        c.R3Position = pos;
        c.R3Velocity = new Vector(0, 0, -1);
        c.velocity = v.subtract(pos).divide(c.eRadius);
        c.normalizedVelocity = c.velocity.normalize();
        c.basePoint = pos.divide(c.eRadius);
        c.foundCollision = false;
        room.checkCollision(c);
        if (c.foundCollision) {
            return true;
            //return Math.abs(c.intersectionNormal.z) > .5;
        }
        return false;
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
        CollisionPacket c = Collisions.collideAndSlide(room, pos, getBounds().getSize(), vel, gravity);
        pos = c.finalPoint;
        if (c.foundCollision) {
            collideWithSolid(c);
        }
    }
}
