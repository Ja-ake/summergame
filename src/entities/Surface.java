package entities;

import collisions.RectPrism;
import collisions.Triangle;
import collisions.Vector;
import engine.Game;
import graphics.Graphics;
import java.util.ArrayList;

public class Surface extends Solid {

    private Vector pos1, pos2, pos3, pos4;

    public Surface(Vector pos1, Vector pos2, Vector pos3, Vector pos4) {
        super(pos1);
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
        this.pos4 = pos4;
        setSprite("Grass");
        ArrayList<Vector> a = new ArrayList();
        a.add(pos1);
        a.add(pos2);
        a.add(pos3);
        a.add(pos4);
        bounds = new RectPrism(a);
    }

    @Override
    public void draw() {
        if (distanceTo(Game.getCamera().pos) < 1000) {
            Graphics.drawSprite(sprite, pos1, pos2, pos3, pos4, 0);
//        Graphics.drawLine(pos1, pos2, 1, 0, 0);
//        Graphics.drawLine(pos1, pos3, 1, 0, 0);
//        Graphics.drawLine(pos3, pos2, 1, 0, 0);
//        Graphics.drawLine(pos1, pos4, 1, 0, 0);
//        Graphics.drawLine(pos4, pos3, 1, 0, 0);
        }
    }

    @Override
    public ArrayList<Triangle> getTriangles() {
        ArrayList<Triangle> a = new ArrayList();
        //Value n creates 2^(n+1) triangles
        a.addAll(new Triangle(pos1, pos3, pos2).getSubtriangles(5));
        a.addAll(new Triangle(pos1, pos3, pos4).getSubtriangles(5));
        return a;
    }

}
