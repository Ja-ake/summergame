package engine;

import collisions.CollisionPacket;
import collisions.Collisions;
import collisions.Triangle;
import collisions.Vector;
import entities.Entity;
import entities.MovingEntity;
import entities.Solid;
import java.util.ArrayList;

public class Room {

    private static final int SECTION_SIZE = 100;

    private final ArrayList<Entity> movingEntities;
    private final int width;
    private final int height;
    private final ArrayList<Entity>[][] sections;

    public Room(int w, int h) {
        movingEntities = new ArrayList();
        width = w;
        height = h;
        sections = new ArrayList[(w + SECTION_SIZE - 1) / SECTION_SIZE][(h + SECTION_SIZE - 1) / SECTION_SIZE];
        for (int i = 0; i < sections.length; i++) {
            for (int j = 0; j < sections[0].length; j++) {
                sections[i][j] = new ArrayList();
            }
        }
    }

    public void addEntity(Entity e) {
        if (e instanceof MovingEntity) {
            movingEntities.add(e);
        } else {
            if (getSection(e.getPos().x, e.getPos().y) != null) {
                getSection(e.getPos().x, e.getPos().y).add(e);
            }
        }
    }

    public void checkCollision(CollisionPacket c) {
        Entity temp = new Entity(c.R3Position.add(c.R3Velocity));
        temp.setBounds(c.eRadius);
        temp.addToRoom(this);
        for (Solid s : temp.touching(Solid.class)) {
            for (Triangle t : s.getTrianglesTouching(temp.getBounds())) {
                Collisions.checkTriangle(c, t.p1.divide(c.eRadius), t.p2.divide(c.eRadius), t.p3.divide(c.eRadius));
            }
        }
    }

    public void draw() {
        for (Entity e : getAllEntities()) {
            e.draw();
        }
//        for (Entity e : getNearbyEntities(Game.getCamera().pos.x, Game.getCamera().pos.y, Game.getCamera().getRenderDistance())) {
//            if (e.distanceTo(Game.getCamera().pos) < Game.getCamera().getRenderDistance()) {
//                e.draw();
//            }
//        }
    }

    public ArrayList<Entity> getAllEntities() {
        ArrayList<Entity> r = new ArrayList(movingEntities);
        for (int i = 0; i < sections.length; i++) {
            for (int j = 0; j < sections[0].length; j++) {
                r.addAll(sections[i][j]);
            }
        }
        return r;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Entity> getNearbyEntities(double x, double y, double d) {
        int cap = (int) d / SECTION_SIZE + 1;
        ArrayList<Entity> r = new ArrayList(movingEntities);
        for (int i = -cap; i <= cap; i++) {
            for (int j = -cap; j <= cap; j++) {
                if (getSection(x + i * SECTION_SIZE, y + j * SECTION_SIZE) != null) {
                    r.addAll(getSection(x + i * SECTION_SIZE, y + j * SECTION_SIZE));
                }
            }
        }
        return r;
    }

    public ArrayList<Entity> getSection(double x, double y) {
        if (inRoom(x, y)) {
            return sections[(int) x / SECTION_SIZE][(int) y / SECTION_SIZE];
        } else {
            return null;
        }
    }

    public int getWidth() {
        return width;
    }

    public boolean inRoom(double x, double y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public boolean position(Class c, Vector v) {
        for (int i = 0; i < movingEntities.size(); i++) {
            Entity e = movingEntities.get(i);
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

    public void removeEntity(Entity e) {
        if (e instanceof MovingEntity) {
            movingEntities.remove(e);
        } else {
            getSection(e.getPos().x, e.getPos().y).remove(e);
        }
    }

    public void update() {
        for (Entity e : new ArrayList<>(movingEntities)) {
            e.update();
        }
    }
}
