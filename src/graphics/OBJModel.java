package graphics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Jeremy Adams (elias4444)
 *
 * Use these lines if reading from a file FileReader fr = new FileReader(ref);
 * BufferedReader br = new BufferedReader(fr);
 *
 * Use these lines if reading from within a jar InputStreamReader fr = new
 * InputStreamReader(new
 * BufferedInputStream(getClass().getClassLoader().getResourceAsStream(ref)));
 * BufferedReader br = new BufferedReader(fr);
 */
public class OBJModel {

    private static final String MODEL_PATH = "models\\";

    private final ArrayList<float[]> vertexsets = new ArrayList(); // Vertex Coordinates
    private final ArrayList<float[]> vertexsetsnorms = new ArrayList(); // Vertex Coordinates Normals
    private final ArrayList<float[]> vertexsetstexs = new ArrayList(); // Vertex Coordinates Textures
    private final ArrayList<int[]> faces = new ArrayList(); // Array of Faces (vertex sets)
    private final ArrayList<int[]> facestexs = new ArrayList(); // Array of of Faces textures
    private final ArrayList<int[]> facesnorms = new ArrayList(); // Array of Faces normals

    private int objectlist;
    private int numpolys = 0;

    //// Statisitcs for drawing ////
    public float toppoint = 0;		// y+
    public float bottompoint = 0;	// y-
    public float leftpoint = 0;		// x-
    public float rightpoint = 0;	// x+
    public float farpoint = 0;		// z-
    public float nearpoint = 0;		// z+

    public OBJModel(String name, boolean centerit) {
        try {
            loadobject(new BufferedReader(new FileReader(MODEL_PATH + name + ".obj")));
            if (centerit) {
                centerit();
            }
            opengldrawtolist();
            numpolys = faces.size();
            cleanup();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not load model: " + name);
        }
    }

    private void cleanup() {
        vertexsets.clear();
        vertexsetsnorms.clear();
        vertexsetstexs.clear();
        faces.clear();
        facestexs.clear();
        facesnorms.clear();
    }

    private void loadobject(BufferedReader br) {
        int linecounter = 0;
        try {

            String newline;
            boolean firstpass = true;

            while (((newline = br.readLine()) != null)) {
                linecounter++;
                newline = newline.trim();
                if (newline.length() > 0) {
                    if (newline.charAt(0) == 'v' && newline.charAt(1) == ' ') {
                        float[] coords = new float[4];
                        String[] coordstext = newline.split("\\s+");
                        for (int i = 1; i < coordstext.length; i++) {
                            coords[i - 1] = Float.valueOf(coordstext[i]);
                        }
                        //// check for farpoints ////
                        if (firstpass) {
                            rightpoint = coords[0];
                            leftpoint = coords[0];
                            toppoint = coords[1];
                            bottompoint = coords[1];
                            nearpoint = coords[2];
                            farpoint = coords[2];
                            firstpass = false;
                        }
                        if (coords[0] > rightpoint) {
                            rightpoint = coords[0];
                        }
                        if (coords[0] < leftpoint) {
                            leftpoint = coords[0];
                        }
                        if (coords[1] > toppoint) {
                            toppoint = coords[1];
                        }
                        if (coords[1] < bottompoint) {
                            bottompoint = coords[1];
                        }
                        if (coords[2] > nearpoint) {
                            nearpoint = coords[2];
                        }
                        if (coords[2] < farpoint) {
                            farpoint = coords[2];
                        }
                        /////////////////////////////
                        vertexsets.add(coords);
                    }
                    if (newline.charAt(0) == 'v' && newline.charAt(1) == 't') {
                        float[] coords = new float[4];
                        String[] coordstext = newline.split("\\s+");
                        for (int i = 1; i < coordstext.length; i++) {
                            coords[i - 1] = Float.valueOf(coordstext[i]);
                        }
                        vertexsetstexs.add(coords);
                    }
                    if (newline.charAt(0) == 'v' && newline.charAt(1) == 'n') {
                        float[] coords = new float[4];
                        String[] coordstext = newline.split("\\s+");
                        for (int i = 1; i < coordstext.length; i++) {
                            coords[i - 1] = Float.valueOf(coordstext[i]);
                        }
                        vertexsetsnorms.add(coords);
                    }
                    if (newline.charAt(0) == 'f' && newline.charAt(1) == ' ') {
                        String[] coordstext = newline.split("\\s+");
                        int[] v = new int[coordstext.length - 1];
                        int[] vt = new int[coordstext.length - 1];
                        int[] vn = new int[coordstext.length - 1];

                        for (int i = 1; i < coordstext.length; i++) {
                            String fixstring = coordstext[i].replaceAll("//", "/0/");
                            String[] tempstring = fixstring.split("/");
                            v[i - 1] = Integer.valueOf(tempstring[0]);
                            if (tempstring.length > 1) {
                                vt[i - 1] = Integer.valueOf(tempstring[1]);
                            } else {
                                vt[i - 1] = 0;
                            }
                            if (tempstring.length > 2) {
                                vn[i - 1] = Integer.valueOf(tempstring[2]);
                            } else {
                                vn[i - 1] = 0;
                            }
                        }
                        faces.add(v);
                        facestexs.add(vt);
                        facesnorms.add(vn);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to read file: " + br.toString());
            //System.exit(0);			
        } catch (NumberFormatException e) {
            System.out.println("Malformed OBJ (on line " + linecounter + "): " + br.toString() + "\r \r" + e.getMessage());
            //System.exit(0);
        }

    }

    private void centerit() {
        float xshift = getXWidth() / 2;
        float yshift = getYHeight() / 2;
        float zshift = getZDepth() / 2;

        for (int i = 0; i < vertexsets.size(); i++) {
            float[] coords = new float[4];

            coords[0] = ((vertexsets.get(i)))[0] - leftpoint - xshift;
            coords[1] = ((vertexsets.get(i)))[1] - bottompoint - yshift;
            coords[2] = ((vertexsets.get(i)))[2] - farpoint - zshift;

            vertexsets.set(i, coords); // = coords;
        }

    }

    public float getXWidth() {
        return rightpoint - leftpoint;
    }

    public float getYHeight() {
        return toppoint - bottompoint;
    }

    public float getZDepth() {
        return nearpoint - farpoint;
    }

    public int numpolygons() {
        return numpolys;
    }

    public void opengldrawtolist() {

        objectlist = glGenLists(1);

        glNewList(objectlist, GL_COMPILE);
        for (int i = 0; i < faces.size(); i++) {
            int[] tempfaces = (faces.get(i));
            int[] tempfacesnorms = (facesnorms.get(i));
            int[] tempfacestexs = (facestexs.get(i));

            //// Quad Begin Header ////
            int polytype;
            switch (tempfaces.length) {
                case 3:
                    polytype = GL_TRIANGLES;
                    break;
                case 4:
                    polytype = GL_QUADS;
                    break;
                default:
                    polytype = GL_POLYGON;
            }
            glBegin(polytype);
            ////////////////////////////

            for (int w = 0; w < tempfaces.length; w++) {
                if (tempfacesnorms[w] != 0) {
                    float normtempx = (vertexsetsnorms.get(tempfacesnorms[w] - 1))[0];
                    float normtempy = (vertexsetsnorms.get(tempfacesnorms[w] - 1))[1];
                    float normtempz = (vertexsetsnorms.get(tempfacesnorms[w] - 1))[2];
                    glNormal3f(normtempx, normtempy, normtempz);
                }

                if (tempfacestexs[w] != 0) {
                    float textempx = (vertexsetstexs.get(tempfacestexs[w] - 1))[0];
                    float textempy = (vertexsetstexs.get(tempfacestexs[w] - 1))[1];
                    float textempz = (vertexsetstexs.get(tempfacestexs[w] - 1))[2];
                    glTexCoord3f(textempx, 1f - textempy, textempz);
                }

                float tempx = (vertexsets.get(tempfaces[w] - 1))[0];
                float tempy = (vertexsets.get(tempfaces[w] - 1))[1];
                float tempz = (vertexsets.get(tempfaces[w] - 1))[2];
                glVertex3f(tempx, tempy, tempz);
            }

            //// Quad End Footer /////
            glEnd();
            ///////////////////////////

        }
        glEndList();
    }

    public void opengldraw() {
        glCallList(objectlist);
    }

}
