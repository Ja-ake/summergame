package graphics;

import files.FontContainer;
import collisions.Triangle;
import collisions.Vector;
import engine.Game;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

public abstract class Graphics {

    public static void drawLine(Vector v1, Vector v2) {
        drawLine(v1, v2, 0, 0, 0);
    }

    public static void drawLine(Vector v1, Vector v2, double r, double g, double b) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glLineWidth(2);
        glColor3d(r, g, b);
        glBegin(GL_LINES);
        {
            glVertex3d(v1.x, v1.y, v1.z);
            glVertex3d(v2.x, v2.y, v2.z);
        }
        glEnd();
        glPopMatrix();
    }

    public static void drawModel(OBJModel m, Vector pos, Sprite s, int index) {
        glPushMatrix();
        if (s != null) {
            s.getTexture(index).bind();
        } else {
            TextureImpl.bindNone();
        }
        glTranslated(pos.x, pos.y, pos.z);
        glColor3d(1, 1, 1);

        m.opengldraw();
        glPopMatrix();
    }

    public static void drawSprite(Sprite s, Vector p1, Vector p2, Vector p3, Vector p4, int index) {
        glPushMatrix();
        glEnable(GL_TEXTURE_2D);
        s.getTexture(index).bind();

        glColor3d(1, 1, 1);
        glBegin(GL_QUADS);
        {
            glTexCoord2d(0, 0);
            glVertex3d(p1.x, p1.y, p1.z);
            glTexCoord2d(0, s.getTexture(index).getHeight());
            glVertex3d(p2.x, p2.y, p2.z);
            glTexCoord2d(s.getTexture(index).getWidth(), s.getTexture(index).getHeight());
            glVertex3d(p3.x, p3.y, p3.z);
            glTexCoord2d(s.getTexture(index).getWidth(), 0);
            glVertex3d(p4.x, p4.y, p4.z);
        }
        glEnd();
        glPopMatrix();
    }

    public static void drawSprite(Sprite s, Vector pos, int index) {
        drawSprite(s, pos, index, 0, null);
    }

    public static void drawSprite(Sprite s, double x, double y, double z, int index) {
        drawSprite(s, x, y, z, index, 0, null);
    }

    public static void drawSprite(Sprite s, Vector pos, int index, double angle, Vector normal) {
        drawSprite(s, pos.x, pos.y, pos.z, index, angle, normal);
    }

    public static void drawSprite(Sprite s, double x, double y, double z, int index, double angle, Vector normal) {
        glPushMatrix();
        glEnable(GL_TEXTURE_2D);
        s.getTexture(index).bind();
        //Translate twice to rotate at center
        glTranslated(x, y, z);
        if (angle != 0) {
            glRotated(angle * 180 / Math.PI, normal.x, normal.y, normal.z);
        }
        glTranslated(-s.getWidth() / 2, -s.getHeight() / 2, 0);

        glColor3d(1, 1, 1);
        glBegin(GL_QUADS);
        {
            glTexCoord2d(0, 0);
            glVertex3d(0, s.getHeight(), 0); //Height reversed because sprite y axis upside-down
            glTexCoord2d(0, s.getTexture(index).getHeight());
            glVertex3d(0, 0, 0);
            glTexCoord2d(s.getTexture(index).getWidth(), s.getTexture(index).getHeight());
            glVertex3d(s.getWidth(), 0, 0);
            glTexCoord2d(s.getTexture(index).getWidth(), 0);
            glVertex3d(s.getWidth(), s.getHeight(), 0);
        }
        glEnd();
        glPopMatrix();
    }

    public static void drawText(String s, double x, double y) {
        drawText(s, "Default", x, y, Color.black);
    }

    public static void drawText(String s, String font, double x, double y, Color c) {
        glPushMatrix();
        Game.getCamera().setProjectionOrtho();
        TextureImpl.bindNone();
        FontContainer.get(font).drawString((float) x, (float) y, s, c);
        Game.getCamera().setProjectionFPS();
        glPopMatrix();
    }

    public static void drawTriangle(Triangle t, double r, double g, double b) {
        drawLine(t.p1, t.p2, r, g, b);
        drawLine(t.p3, t.p2, r, g, b);
        drawLine(t.p1, t.p3, r, g, b);
    }

}
