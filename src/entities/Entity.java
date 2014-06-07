package entities;

import collisions.RectPrism;
import collisions.Vector;
import engine.Room;
import graphics.Graphics;
import graphics.SpriteContainer;
import engine.Sprite;
import java.util.ArrayList;

public class Entity {

    protected Vector pos;
    protected RectPrism bounds;
    protected Sprite sprite;
    protected double imageIndex;
    protected double imageSpeed;

    protected int ticks;
    protected int depth;
    protected Room room;

    public Entity(Vector pos) {
        this.pos = pos;

        bounds = new RectPrism(pos, new Vector(0, 0, 0));
        sprite = null;
    }

    public void addToRoom(Room r) {
        room = r;
        r.addEntity(this);
    }

    public boolean collisionOther(Entity other) {
        return (getBounds().intersects(other.getBounds()));
    }

    public double distanceTo(Vector v) {
        return pos.subtract(v).length();
    }

    public double distanceTo(Entity e) {
        return distanceTo(e.getPos());
    }

    public void draw() {
        if (sprite != null) {
            Graphics.drawSprite(sprite, pos, (int) Math.round(imageIndex));
        }
    }

//    public void drawHealthbar(double x, double y, double w, double h, double val, double max, double r, double g, double b) {
//        Graphics.fillRect(pos.x, pos.y, w, h, 0, 0, 0);
//        Graphics.fillRect(pos.x, pos.y, w * val / max, h, r, g, b);
//    }
    public RectPrism getBounds() {
        return bounds;
    }

    public int getDepth() {
        return depth;
    }

    public Vector getPos() {
        return pos;
    }

    public <E extends Entity> E nearest(Class<E> c) {
        E maxEntity = null;
        for (int i = 0; i < room.entityArray.size(); i++) {
            Entity e = room.entityArray.get(i);
            if (e != null && e != this) {
                if (c.isInstance(e)) {
                    if (distanceTo(e) < distanceTo(maxEntity) || maxEntity == null) {
                        maxEntity = (E) e;
                    }
                }
            }
        }
        return maxEntity;
    }

    public boolean place(Class c, Vector v) {
        Vector oldPos = pos;
        pos = v;
        for (Entity e : room.entityArray) {
            if (e != this) {
                if (c.isInstance(e)) {
                    if (collisionOther(e)) {
                        pos = oldPos;
                        return true;
                    }
                }
            }
        }
        pos = oldPos;
        return false;
    }

    public boolean placeSolid(Vector v) {
        return place(Solid.class, v);
    }

    public void removeSelf() {
        room.entityArray.remove(this);
    }

    public void setBounds(Vector size) {
        bounds.setSize(size);
    }

    public void setDepth(int d) {
        depth = d;
        room.checkDepth(this);
    }

    public void setSprite(String name) {
        sprite = SpriteContainer.get(name);
    }

    public <E extends Entity> ArrayList<E> touching(Class<E> c) {
        ArrayList<E> ae = new ArrayList();
        for (int i = 0; i < room.entityArray.size(); i++) {
            Entity e = room.entityArray.get(i);
            if (e != null) {
                if (c.isInstance(e)) {
                    if (collisionOther(e)) {
                        ae.add((E) e);
                    }
                }
            }
        }
        return ae;
    }

    public void update() {
        ticks++;
        imageIndex += imageSpeed;
    }
}
