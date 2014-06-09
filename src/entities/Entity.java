package entities;

import collisions.RectPrism;
import collisions.Vector;
import engine.Game;
import engine.Room;
import graphics.Graphics;
import files.SpriteContainer;
import graphics.Sprite;
import graphics.OBJModel;
import java.util.ArrayList;

public class Entity {

    protected Vector pos;
    protected RectPrism bounds;
    protected Sprite sprite;
    protected OBJModel model;
    protected double imageIndex;
    protected double imageSpeed;

    protected int ticks;
    protected Room room;

    public Entity(Vector pos) {
        this.pos = pos;

        bounds = new RectPrism(pos, new Vector(0, 0, 0));
        sprite = null;
        model = null;
    }

    public void addToRoom(Room r) {
        room = r;
        r.addEntity(this);
//        r.addEntity(this);
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
        if (distanceTo(Game.getCamera().getPos()) < 1000) {
            if (model != null) {
                Graphics.drawModel(model, sprite, pos, (int) Math.round(imageIndex));
            } else if (sprite != null) {
                Graphics.drawSprite(sprite, pos, (int) Math.round(imageIndex));
            }
        }
    }

//    public void drawHealthbar(double x, double y, double w, double h, double val, double max, double r, double g, double b) {
//        Graphics.fillRect(pos.x, pos.y, w, h, 0, 0, 0);
//        Graphics.fillRect(pos.x, pos.y, w * val / max, h, r, g, b);
//    }
    public RectPrism getBounds() {
        return bounds;
    }

    public Vector getPos() {
        return pos;
    }

    public <E extends Entity> E nearest(Class<E> c) {
        E maxEntity = null;
        for (int i = 0; i < room.getNearbyEntities(pos.x, pos.y, 50).size(); i++) {
            Entity e = room.getNearbyEntities(pos.x, pos.y, 50).get(i);
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
        for (Entity e : room.getNearbyEntities(pos.x, pos.y, 50)) {
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

    public void removeSelf() {
        room.removeEntity(this);
    }

    public void setBounds(Vector size) {
        bounds.setSize(size);
    }

    public void setModel(String name) {
        model = new OBJModel(name, true);
    }

    public void setSprite(String name) {
        sprite = SpriteContainer.get(name);
    }

    public <E extends Entity> ArrayList<E> touching(Class<E> c) {
        ArrayList<E> ae = new ArrayList();
        for (Entity e : room.getNearbyEntities(pos.x, pos.y, 2000)) {
            if (c.isInstance(e)) {
                if (collisionOther(e)) {
                    ae.add((E) e);
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
