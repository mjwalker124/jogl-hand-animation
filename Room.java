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

public class Room {
    private Mesh floor, cube, wall, wall2, ceilling, painting1,painting2,painting3, wallThin, wallWindowBase, wallWindowTop, windowBorder, lightStand, lightCover;
    private float[] randoms;

    //Setup the room
    public Room(GL3 gl, ArrayList<Light> lights, Camera camera, float[] randoms) {
        this.randoms = randoms;
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/woodenfloor.jpg");
        int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/wall.jpg");
        int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/wall2.jpg");
        int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/ceilling.jpg");
        int[] textureId10 = TextureLibrary.loadTexture(gl, "textures/art.jpg");
        int[] textureId15 = TextureLibrary.loadTexture(gl, "textures/wall-side.jpg");
        int[] textureId16 = TextureLibrary.loadTexture(gl, "textures/wall-side-base.jpg");
        int[] textureId17 = TextureLibrary.loadTexture(gl, "textures/wall-side-top.jpg");
        int[] textureId21 = TextureLibrary.loadTexture(gl, "textures/window-border.jpg");
        int[] textureId22 = TextureLibrary.loadTexture(gl, "textures/art2.jpg");
        int[] textureId23 = TextureLibrary.loadTexture(gl, "textures/art3.jpg");
        int[] textureId20 = TextureLibrary.loadTexture(gl, "textures/tree-leafs_specular.jpg");
        int[] textureId24 = TextureLibrary.loadTexture(gl, "textures/brushed-metal.jpg");
        floor = new TwoTriangles(gl, textureId0);
        wall = new TwoTriangles(gl, textureId7);
        wallThin = new TwoTriangles(gl, textureId15);
        wallWindowBase = new TwoTriangles(gl, textureId16);
        wallWindowTop = new TwoTriangles(gl, textureId17);
        wall2 = new TwoTriangles(gl, textureId8);
        ceilling = new TwoTriangles(gl, textureId9);
        painting1 = new TwoTriangles(gl, textureId10);
        painting2 = new TwoTriangles(gl, textureId22);
        painting3 = new TwoTriangles(gl, textureId23);
        windowBorder = new Cube(gl, textureId21, textureId20);
        lightStand = new Cube(gl, textureId24, textureId24);
        lightCover = new Sphere(gl, textureId21, textureId20);

        floor.setModelMatrix(Mat4Transform.scale(16,1,16));
        painting1.setModelMatrix(getPainting1Position());
        painting2.setModelMatrix(getPainting2Position());
        painting3.setModelMatrix(getPainting3Position());

        //Add ligting to all objects in the room
        for(Light light: lights) {
            floor.addLight(light);
            ceilling.addLight(light);
            wall.addLight(light);
            wallWindowBase.addLight(light);
            wallWindowTop.addLight(light);
            wallThin.addLight(light);
            wall2.addLight(light);
            painting1.addLight(light);
            painting2.addLight(light);
            painting3.addLight(light);
            windowBorder.addLight(light);
            lightStand.addLight(light);
            lightCover.addLight(light);
        }

        floor.setCamera(camera);
        ceilling.setCamera(camera);
        wall.setCamera(camera);
        wallWindowBase.setCamera(camera);
        wallWindowTop.setCamera(camera);
        wallThin.setCamera(camera);
        wall2.setCamera(camera);
        painting1.setCamera(camera);
        painting2.setCamera(camera);
        painting3.setCamera(camera);
        windowBorder.setCamera(camera);
        lightStand.setCamera(camera);
        lightCover.setCamera(camera);
    }

    //Allow the room to be rendered
    public void render(GL3 gl) {
        painting1.render(gl);
        painting2.render(gl);
        painting3.render(gl);
        floor.render(gl);
        ceilling.render(gl);
        ceilling.setModelMatrix(getForCeilling());
        wall.render(gl);
        wall.setModelMatrix(getFrontWall());       // possibly changing cube transform each frame
        wall.render(gl);
        wallThin.setModelMatrix(leftWall1());       // possibly changing cube transform each frame
        wallThin.render(gl);
        windowBorder.setModelMatrix(getWindowBorderBase());
        windowBorder.render(gl);
        windowBorder.setModelMatrix(getWindowBorderTop());
        windowBorder.render(gl);
        windowBorder.setModelMatrix(getWindowBorderMiddle());
        windowBorder.render(gl);
        windowBorder.setModelMatrix(getWindowBorderVMiddle());
        windowBorder.render(gl);
        windowBorder.setModelMatrix(getWindowBorderLeft());
        windowBorder.render(gl);
        windowBorder.setModelMatrix(getWindowBorderRight());
        windowBorder.render(gl);
        wallThin.setModelMatrix(leftWall2());       // possibly changing cube transform each frame
        wallThin.render(gl);
        wallWindowBase.setModelMatrix(leftWall3());       // possibly changing cube transform each frame
        wallWindowBase.render(gl);
        wallWindowTop.setModelMatrix(leftWall4());
        wallWindowTop.render(gl);
        wall2.setModelMatrix(getBackWall());       // possibly changing cube transform each frame
        wall2.render(gl);
        wall2.setModelMatrix(getRightWall());       // possibly changing cube transform each frame
        wall2.render(gl);

        lightStand.setModelMatrix(getFloorLightBaseStand());
        lightStand.render(gl);

        lightStand.setModelMatrix(getFloorLightStand());
        lightStand.render(gl);

        lightStand.setModelMatrix(getFloorLight2BaseStand());
        lightStand.render(gl);

        lightStand.setModelMatrix(getFloorLight2Stand());
        lightStand.render(gl);

        lightStand.setModelMatrix(getCeillingLightBaseStand());
        lightStand.render(gl);

        lightStand.setModelMatrix(getCeillingLightStand());
        lightStand.render(gl);

        lightCover.setModelMatrix(getCeillingLightPosition());
        lightCover.render(gl);

        lightCover.setModelMatrix(getFloor1LightPosition());
        lightCover.render(gl);

        lightCover.setModelMatrix(getFloor2LightPosition());
        lightCover.render(gl);
    }

    //Allow the perspective to be updated
    public void updatePerspective(Mat4 perspective) {
        painting1.setPerspective(perspective);
        painting2.setPerspective(perspective);
        painting3.setPerspective(perspective);
        wallWindowBase.setPerspective(perspective);
        wallWindowTop.setPerspective(perspective);
        floor.setPerspective(perspective);
        ceilling.setPerspective(perspective);
        wall.setPerspective(perspective);
        wall2.setPerspective(perspective);
        wallThin.setPerspective(perspective);
        windowBorder.setPerspective(perspective);
        lightStand.setPerspective(perspective);
        lightCover.setPerspective(perspective);
    }

    //Dispose of the room
    public void dispose(GL3 gl) {
        wallWindowTop.dispose(gl);
        wallWindowBase.dispose(gl);
        painting1.dispose(gl);
        painting2.dispose(gl);
        painting3.dispose(gl);
        floor.dispose(gl);
        ceilling.dispose(gl);
        wall.dispose(gl);
        wall2.dispose(gl);
        windowBorder.dispose(gl);
        wallThin.dispose(gl);
        lightStand.dispose(gl);
        lightCover.dispose(gl);
    }

    //Calculations for all of the room objects:
    private Mat4 getCeillingLightPosition() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,0.5f), model);
        model = Mat4.multiply(Mat4Transform.translate(5f,2f,5f), model);
        return model;
    }

    private Mat4 getFloor1LightPosition() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,0.5f), model);
        model = Mat4.multiply(Mat4Transform.translate(-5f,2f,-5f), model);
        return model;
    }

    private Mat4 getFloor2LightPosition() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,0.5f), model);
        model = Mat4.multiply(Mat4Transform.translate(0f,10f,0f), model);
        return model;
    }

    private Mat4 getCeillingLightStand() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.1f,6f,0.1f), model);
        model = Mat4.multiply(Mat4Transform.translate(0f,13f,0f), model);
        return model;
    }

    private Mat4 getFloorLightStand() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.1f,2f,0.1f), model);
        model = Mat4.multiply(Mat4Transform.translate(5f,1f,5f), model);
        return model;
    }

    private Mat4 getFloorLightBaseStand() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(1f,0.05f,1f), model);
        model = Mat4.multiply(Mat4Transform.translate(5f,0f,5f), model);
        return model;
    }

    private Mat4 getFloorLight2Stand() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.1f,2f,0.1f), model);
        model = Mat4.multiply(Mat4Transform.translate(-5f,1f,-5f), model);
        return model;
    }

    private Mat4 getFloorLight2BaseStand() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(1f,0.05f,1f), model);
        model = Mat4.multiply(Mat4Transform.translate(-5f,0f,-5f), model);
        return model;
    }

    private Mat4 getCeillingLightBaseStand() {
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(1f,0.05f,1f), model);
        model = Mat4.multiply(Mat4Transform.translate(0f,15.95f,0f), model);
        return model;
    }

    private Mat4 getFrontWall() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size,1f,size), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundX(90), model);
        model = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), model);
        return model;
    }

    private Mat4 leftWall1() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size / 4,1f,size), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundY(90), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,((size * 0.25f) + (size / 8))), model);
        return model;
    }

    private Mat4 leftWall2() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size / 4,1f,size), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundY(90), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,(-(size * 0.25f) - (size / 8))), model);
        return model;
    }

    private Mat4 leftWall3() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size / 2,1f,size / 4), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundY(90), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.125f,0), model);
        return model;
    }

    private Mat4 leftWall4() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size / 2,1f,size / 4), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundY(90), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size - size * 0.125f,0), model);
        return model;
    }

    private Mat4 getBackWall() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size,1f,size), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundX(-90), model);
        model = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,size*0.5f), model);
        return model;
    }

    private Mat4 getRightWall() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size,1f,size), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundY(90), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundZ(90), model);
        model = Mat4.multiply(Mat4Transform.translate(size*0.5f,size*0.5f,0), model);
        return model;
    }

    private Mat4 getForCeilling() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(size,size,size), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundZ(180), model);
        model = Mat4.multiply(Mat4Transform.translate(0,size,0), model);
        return model;
    }

    private Mat4 getWindowBorderBase() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,9f), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f, size * 0.25f,0f), model);
        return model;
    }

    private Mat4 getWindowBorderTop() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,9f), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f, size * 0.75f,0f), model);
        return model;
    }

    private Mat4 getWindowBorderMiddle() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,9f), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f, size * 0.5f,0f), model);
        return model;
    }

    private Mat4 getWindowBorderVMiddle() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,8f), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundX(90), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f, size * 0.5f,0f), model);
        return model;
    }

    private Mat4 getWindowBorderRight() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,8f), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundX(90), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f , size * 0.5f,- size / 4f  - 0.25f), model);
        return model;
    }

    private Mat4 getWindowBorderLeft() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(0.5f,0.5f,8f), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundX(90), model);
        model = Mat4.multiply(Mat4Transform.translate(-size*0.5f , size * 0.5f, size / 4f + 0.25f), model);
        return model;
    }

    private Mat4 getPainting1Position() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(8f,8f,8f), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundZ(90), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundX(90), model);
        model = Mat4.multiply(Mat4Transform.translate((size / 2) - 0.1f,size/2,0), model);
        return model;
    }

    private Mat4 getPainting2Position() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(8f,8f,8f), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundX(90), model);
        model = Mat4.multiply(Mat4Transform.translate(0f,size/2,-size/2 + 0.1f), model);
        return model;
    }

    private Mat4 getPainting3Position() {
        float size = 16f;
        Mat4 model = new Mat4(1);
        model = Mat4.multiply(Mat4Transform.scale(8f,8f,8f), model);
        model = Mat4.multiply(Mat4Transform.rotateAroundX(-90), model);
        model = Mat4.multiply(Mat4Transform.translate(0f,size/2,size/2 - 0.1f), model);
        return model;
    }
}