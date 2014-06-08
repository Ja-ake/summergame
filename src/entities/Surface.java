package entities;

import collisions.RectPrism;
import collisions.Triangle;
import collisions.Vector;
import engine.Game;
import graphics.Graphics;
import java.util.ArrayList;

public class Surface extends Solid {

    private static final int TRIANGLE_DETAIL = 5;
    public static boolean DRAW_MESH = false;

    private final Vector[][] points;

    public Surface(Vector[][] points) {
        super(points[0][0]);
        Vector c = new Vector(0, 0, 0);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[0].length; j++) {
                c = c.add(points[i][j]);
            }
        }
        pos = c.divide(points.length * points[0].length);
        this.points = points;
        setSprite("Grass");
        ArrayList<Vector> a = new ArrayList();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[0].length; j++) {
                a.add(points[i][j]);
            }
        }
        bounds = new RectPrism(a);
    }

    @Override
    public void draw() {
        double d = distanceTo(Game.getCamera().pos);
        if (DRAW_MESH) {
            if (d < 50) {
                for (Triangle t : getTriangles()) {
                    Graphics.drawTriangle(t, 1, 0, 0);
                }
            }
            Graphics.drawLine(points[0][0], points[points.length - 1][0]);
            Graphics.drawLine(points[points.length - 1][points[0].length - 1], points[points.length - 1][0]);
            Graphics.drawLine(points[points.length - 1][points[0].length - 1], points[0][points[0].length - 1]);
            Graphics.drawLine(points[0][0], points[0][points[0].length - 1]);
        } else if (d < 200) {
            drawDetail(1);
        } else if (d < 500) {
            drawDetail(2);
        } else if (d < 1000) {
            drawDetail(4);
        } else if (d < 2000) {
            drawDetail(8);
        } else {
            drawDetail(32);
        }
    }

    public void drawDetail(int d) {
        if (d > points.length - 1) {
            d = points.length - 1;
        }
        for (int i = 0; i < points.length - d; i += d) {
            for (int j = 0; j < points[0].length - d; j += d) {
                Graphics.drawSprite(sprite, points[i][j], points[i][j + d], points[i + d][j + d], points[i + d][j], 0);
            }
        }
    }

    @Override
    public ArrayList<Triangle> getTriangles() {
        ArrayList<Triangle> a = new ArrayList();
        //Value n creates 2^n*(size^2+1) triangles
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = 0; j < points[0].length - 1; j++) {
                a.addAll(new Triangle(points[i][j], points[i + 1][j + 1], points[i + 1][j]).getSubtriangles(TRIANGLE_DETAIL));
                a.addAll(new Triangle(points[i][j], points[i + 1][j + 1], points[i][j + 1]).getSubtriangles(TRIANGLE_DETAIL));
            }
        }
        return a;
    }

    @Override
    public ArrayList<Triangle> getTrianglesTouching(RectPrism r) {
        ArrayList<Triangle> a = new ArrayList();
        //Value n creates 2^n*(size^2+1) triangles
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = 0; j < points[0].length - 1; j++) {
                a.addAll(new Triangle(points[i][j], points[i + 1][j + 1], points[i + 1][j]).getSubtrianglesTouching(TRIANGLE_DETAIL, r));
                a.addAll(new Triangle(points[i][j], points[i + 1][j + 1], points[i][j + 1]).getSubtrianglesTouching(TRIANGLE_DETAIL, r));
            }
        }
        return a;
    }

}
