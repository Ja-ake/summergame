package engine;

import collisions.Vector;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Camera {

    public static final Vector UP = new Vector(0, 0, 1);
    public static final float ASPECT_RATIO = 1;
    public static final float FOV = 90;

    public Vector pos;
    public double xyDirection, zDirection;

    public Camera() {
        pos = new Vector(0, 0, 0);
        xyDirection = 0;
        zDirection = Math.PI / 1.5;
    }

    public Vector getLookAt() {
        double x = pos.x + Math.sin(zDirection) * Math.cos(xyDirection);
        double y = pos.y + Math.sin(zDirection) * Math.sin(xyDirection);
        double z = pos.z + Math.cos(zDirection);
        return new Vector(x, y, z);
    }

    public void update() {
        // Set the aspect ratio of the clipping volume to match the viewport
        glMatrixMode(GL_PROJECTION);  // To operate on the Projection matrix
        glLoadIdentity();             // Reset
        // Enable perspective projection with fovy, aspect, zNear and zFar
        gluPerspective(FOV, ASPECT_RATIO, 0.1f, 1000);
        gluLookAt((float) pos.x, (float) pos.y, (float) pos.z,
                (float) getLookAt().x, (float) getLookAt().y, (float) getLookAt().z,
                (float) UP.x, (float) UP.y, (float) UP.z);
    }
}
