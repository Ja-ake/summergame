package engine;

import collisions.CollisionPacket;
import collisions.Collisions;
import collisions.Triangle;
import collisions.Vector;
import entities.Entity;
import entities.Solid;
import java.util.ArrayList;

public class Room {

    public ArrayList<Entity> entityArray;
    public int width;
    public int height;

    public Room(int w, int h) {
        entityArray = new ArrayList();
        width = w;
        height = h;
    }

    public Entity addEntity(Entity e) {
        if (!entityArray.contains(e)) {
            int pos = 0;
            while (pos < entityArray.size() && entityArray.get(pos).getDepth() < e.getDepth()) {
                pos++;
            }
            entityArray.add(pos, e);
            return e;
        }
        return null;
    }

    public void checkCollision(CollisionPacket c) {
        Entity temp = new Entity(c.R3Position.add(c.R3Velocity));
        temp.setBounds(c.eRadius);
        temp.addToRoom(this);
        for (Solid s : temp.touching(Solid.class)) {
            for (Triangle t : s.getTriangles()) {
                if (t.couldCollide(temp.getBounds())) {
                    Collisions.checkTriangle(c, t.p1.divide(c.eRadius), t.p2.divide(c.eRadius), t.p3.divide(c.eRadius));
                }
            }
        }
    }

    public void checkDepth(Entity e) {
        if (entityArray.contains(e)) {
            entityArray.remove(e);
            int pos = 0;
            while (pos < entityArray.size() && entityArray.get(pos).getDepth() < e.getDepth()) {
                pos++;
            }
            entityArray.add(pos, e);
        }
    }

    public void draw() {
        for (int i = 0; i < entityArray.size(); i++) {
            entityArray.get(i).draw();
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean inRoom(double x, double y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public void orderByDepth() {
        for (int i = 0; i < entityArray.size(); i++) {
            for (int j = i + 1; j < entityArray.size(); j++) {
                if (entityArray.get(i).getDepth() > entityArray.get(j).getDepth()) {
                    Entity temp = entityArray.get(j);
                    entityArray.set(j, entityArray.get(i));
                    entityArray.set(i, temp);
                }
            }
        }
    }

    public boolean position(Class c, Vector v) {
        for (int i = 0; i < entityArray.size(); i++) {
            Entity e = entityArray.get(i);
            if (e != null) {
                if (c.isInstance(e)) {
                    if (e.getBounds().contains(v)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean positionEmpty(Vector v) {
        return !positionSolid(v) && !position(Entity.class, v);
    }

    public boolean positionSolid(Vector v) {
        return position(Solid.class, v);
    }

    public void update() {
        for (Entity e : new ArrayList<>(entityArray)) {
            e.update();
        }
    }
}
