/* I declare that this code is my own work */
/* Author Matthew Walker mjwalker1@sheffield.ac.uk */

import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import java.util.*;

public class Outside {
    private Mesh grass, tree;
    private float[] randoms;

    //Setup the outside
    public Outside(GL3 gl, ArrayList<Light> lights, Camera camera, float[] randoms) {
        this.randoms = randoms;
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/grass.jpg");
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/tree-leafs.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/tree-leafs_specular.jpg");

        grass = new TwoTriangles(gl, textureId0);
        tree = new Cube(gl, textureId1, textureId2);

        //Add lights to the outside
        for(Light light: lights) {
            grass.addLight(light);
            tree.addLight(light);
        }

        grass.setCamera(camera);
        tree.setCamera(camera);
    }

    //Allow the room to be rendered
    public void render(GL3 gl) {
        grass.setModelMatrix(outsideFloor());
        grass.render(gl);

        //Create 100 random trees
        for (int i = 0; i < 100; i++) {
            tree.setModelMatrix(newTreePosition(i));
            tree.render(gl);
        }
    }

    //Allow the perpective to be updated
    public void updatePerspective(Mat4 perspective) {
        grass.setPerspective(perspective);
        tree.setPerspective(perspective);
    }

    //Setup the dispose of the outside objects
    public void dispose(GL3 gl) {
        tree.dispose(gl);
        grass.dispose(gl);
    }

    //Calculates a tree position and size
    private Mat4 newTreePosition(int i) {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(2f + randoms[i + 200] * 6f,2f + randoms[i + 300] * 10f,2f + randoms[i + 400] * 6f), model);
        model = Mat4.multiply(Mat4Transform.translate(-90f + randoms[i] * 70,0,-20f + randoms[i + 100] * 50), model);
        return model;
    }

    private Mat4 outsideFloor() {
        float size = 90f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size,size,size), model);
        model = Mat4.multiply(Mat4Transform.translate(-(size * 0.5f) - 8f,0,0), model);
        return model;
    }
}