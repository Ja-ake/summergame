package engine;

import collisions.Vector;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Camera {

    public static final Vector UP = new Vector(0, 0, 1);
    public static final float ASPECT_RATIO = 1;
    public static final float FOV = 90;

    private Vector pos;
    private double xyDirection, zDirection;

    public Camera() {
        pos = new Vector(0, 0, 0);
        xyDirection = 0;
        zDirection = Math.PI / 1.5;
    }

    public void follow(Vector pos, double followDist, double xyDirection, double zDirection) {
        double x = followDist * Math.sin(zDirection) * Math.cos(xyDirection);
        double y = followDist * Math.sin(zDirection) * Math.sin(xyDirection);
        double z = followDist * Math.cos(zDirection);
        this.pos = pos.subtract(new Vector(x, y, z));
        this.xyDirection = xyDirection;
        this.zDirection = zDirection;
    }

    public Vector getLookAt() {
        double x = pos.x + Math.sin(zDirection) * Math.cos(xyDirection);
        double y = pos.y + Math.sin(zDirection) * Math.sin(xyDirection);
        double z = pos.z + Math.cos(zDirection);
        return new Vector(x, y, z);
    }
    
    public Vector getPos() {
        return pos;
    }

    public int getRenderDistance() {
        return 1000000;
    }

    public void setProjectionFPS() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(FOV, ASPECT_RATIO, 0.1f, getRenderDistance());
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        gluLookAt((float) pos.x, (float) pos.y, (float) pos.z,
                (float) getLookAt().x, (float) getLookAt().y, (float) getLookAt().z,
                (float) UP.x, (float) UP.y, (float) UP.z);
    }

    public void setProjectionOrtho() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 0, 1000, 1000, -100, 100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glOrtho(0, 0, 1000, 1000, -100, 100);
    }
}
