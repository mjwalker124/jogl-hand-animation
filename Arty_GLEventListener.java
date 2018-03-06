/* I declare that this code is my own work */
/* Author Matthew Walker mjwalker1@sheffield.ac.uk
 *
  * This file is based on the GLEventListener files from the exercises*/


import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import java.util.*;

public class Arty_GLEventListener implements GLEventListener {
    private static final boolean DISPLAY_SHADERS = true;
    private float aspect;

    public Arty_GLEventListener(Camera camera) {
        this.camera = camera;
    }

    // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

    /* Initialisation */
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        gl.glClearColor(0.529f, 0.808f, 0.922f, 1.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
        gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
        gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
        initialise(gl);
        startTime = getSeconds();
    }

    /* Called to indicate the drawing surface has been moved and/or resized  */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(x, y, width, height);
        aspect = (float)width/(float)height;
    }

    /* Draw */
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        render(gl);
    }

    /* Clean up memory, if necessary */
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        disposeMeshes(gl);
    }


    // ***************************************************
  /* TIME
   */

    private double startTime;
    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    // ***************************************************
  /* An array of random numbers, this is so that when they are used to generate an object, the same numbers can be used for each fram.
   */

    private int NUM_RANDOMS = 1000;
    private float[] randoms;
    private boolean isNight = false;
    private void createRandomNumbers() {
        randoms = new float[NUM_RANDOMS];
        for (int i=0; i<NUM_RANDOMS; ++i) {
            randoms[i] = (float)Math.random();
        }
    }

    // ***************************************************
  /* INTERACTION
   *
   *
   */

    private boolean animation = false;

    private double savedTime = 0;

    public void startAnimation() {
        if (savedTime == 0) {
            resetHand();
        }
        animation = true;
        startTime = getSeconds()-savedTime;
    }

    public void stopAnimation() {
        animation = false;
        double elapsedTime = getSeconds()-startTime;
        savedTime = elapsedTime;
    }

    //Turn off the global light, and set the world to night
    public void lightOut() {
        main_light.setAmbient(0);
        main_light.setDiffuse(0.1f);
        isNight = true;
    }

    //Turn on light, and turn off night mode
    public void lightOn() {
        main_light.setAmbient(1f);
        main_light.setDiffuse(0.5f);
        isNight = false;
    }

    //Turn off floor light
    public void floorLightOut() {
        floor_light.setBrightness(0f);
        floor_light2.setBrightness(0f);
        floor_light.setAmbient(0);
        floor_light.setSpecular(0);
        floor_light.setDiffuse(0);
        floor_light2.setAmbient(0);
        floor_light2.setSpecular(0);
        floor_light2.setDiffuse(0);
    }

    //Turn on floor light
    public void floorLightOn() {
        floor_light.setBrightness(1f);
        floor_light2.setBrightness(1f);
        floor_light.setAmbient(0.25f);
        floor_light.setSpecular(0.6f);
        floor_light.setDiffuse(0.25f);
        floor_light2.setAmbient(0.25f);
        floor_light2.setSpecular(0.6f);
        floor_light2.setDiffuse(0.25f);
    }

    //Turn wall light off
    public void wallLightOut() {
        wall_light.setAmbient(0);
        wall_light.setSpecular(0);
        wall_light.setDiffuse(0);
    }

    //Turn wall light on
    public void wallLightOn() {
        wall_light.setAmbient(0.5f);
        wall_light.setSpecular(0.9f);
        wall_light.setDiffuse(0.5f);
    }

    // Put the hand back into its natural position
    public void resetHand() {
        animation = false;
        double elapsedTime = 2;
        little.curlFinger(0, 0, elapsedTime, 0, 0);
        little.angleFinger(0,0, elapsedTime, 0, 0);
        ring.curlFinger(0, 0, elapsedTime, 0, 0);
        ring.angleFinger(0,0, elapsedTime, 0, 0);
        middle.angleFinger(0, 0, elapsedTime, 0, 0);
        middle.curlFinger(0, 0, elapsedTime, 0, 0);
        index.curlFinger(0, 0, elapsedTime, 0, 0);
        index.angleFinger(0, 0, elapsedTime, 0, 0);
        thumb.thumbForward(0, 0, elapsedTime, 0, 0);
        thumb.thumbOut(0, 0, elapsedTime, 0, 0);
        thumb.curlFinger(0, 0, elapsedTime, 0, 0);
        thumb.angleFinger(0,0, elapsedTime, 0, 0);

        savedTime = 0;
    }

    // Move hand into the M position
    public void M() {
        resetHand();
        double elapsedTime = 2;
        little.curlFinger(0, 0, elapsedTime, 0, 120);
        little.angleFinger(0,0, elapsedTime, 0, 30);
        ring.angleFinger(0, 0, elapsedTime, 0, 10);
        index.angleFinger(0, 0, elapsedTime, 0, 10);
        middle.angleFinger(0, 0, elapsedTime, 0, 10);
        thumb.thumbForward(0, 0, elapsedTime, 0, 60);
        thumb.thumbOut(0, 0, elapsedTime, 0, -50);
        thumb.curlFinger(0, 0, elapsedTime, 0, -60);
        thumb.angleFinger(0,0, elapsedTime, 0, 130);
        ring.curlFinger(0, 0, elapsedTime, 0, 90);
        middle.curlFinger(0, 0, elapsedTime, 0, 105);
        index.curlFinger(0, 0, elapsedTime, 0, 120);
    }

    //Move hand into J position
    public void J() {
        resetHand();
        double elapsedTime = 2;
        thumb.thumbForward(0, 0, elapsedTime, 0, 60);
        thumb.curlFinger(0, 0, elapsedTime, 0, -60);
        thumb.thumbOut(0, 0, elapsedTime, 0, -70);
        ring.angleFinger(0, 0, elapsedTime, 0, 15);
        middle.angleFinger(0, 0, elapsedTime, 0, 15);
        index.angleFinger(0, 0, elapsedTime, 0, 15);
        ring.curlFinger(0, 0, elapsedTime, 0, 110);
        middle.curlFinger(0, 0, elapsedTime, 0, 110);
        index.curlFinger(0, 0, elapsedTime, 0, 110);
        little.angleFinger(0, 0, elapsedTime, 0, 0);
        little.curlFinger(0, 0, elapsedTime, 0, 0);
        thumb.angleFinger(0, 0, elapsedTime, 0,100);
    }

    //Move hand into W position
    public void W() {
        resetHand();
        double elapsedTime = 2;
        thumb.thumbForward(0, 0, elapsedTime, 0, 60);
        thumb.curlFinger(0, 0, elapsedTime, 0, -60);
        little.curlFinger(0, 0, elapsedTime, 0, 100);
        little.angleFinger(0,0, elapsedTime, 0, 30);
        ring.angleFinger(0, 0, elapsedTime, 0, 10);
        index.angleFinger(0, 0, elapsedTime, 0, -10);
        thumb.thumbOut(0, 0, elapsedTime, 0, -50);
        thumb.angleFinger(0, 0, elapsedTime, 0, 120);
    }

    //Move hand into Vulcan Salute...Make it so...
    public void VulcanSalute() {
        resetHand();
        double elapsedTime = 2;
        little.angleFinger(0,0, elapsedTime, 0, 30);
        thumb.thumbOut(0, 0, elapsedTime, 0, -50);

        ring.angleFinger(0, 0, elapsedTime, 0, 30);

        index.angleFinger(0, 0, elapsedTime, 0, -30);
        middle.angleFinger(0, 0, elapsedTime, 0, -30);
    }

    //Change the light based on a value passed in
    public void setLight(float light) {
        //If the value is less than 0.3 then it's night time
        if (light < 0.3) {
            isNight = true;
            main_light.setDiffuse(0.1f);
        } else {
            main_light.setDiffuse(0.5f);
            isNight = false;
        }
        main_light.setAmbient(light);
    }

    // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   */
    private Camera camera;
    private Mat4 perspective;
    private Mesh sphere, cube, ringTexture, gem;
    private Light main_light, wall_light, floor_light, spotLight, floor_light2;
    private SGNode robot;
    private Thumb thumb;
    private Finger index;
    private Finger middle;
    private Finger ring;
    private Finger little;
    private Room room;
    private Outside outside;
    private ArrayList<Light> lights, outsideLights;
    private float xPosition = 0;
    private TransformNode translateX, robotMoveTranslate, armRotate, rightArmRotate, indexFingerRotate, indexFinger2Rotate,indexFinger3Rotate;

    private void initialise(GL3 gl) {
        createRandomNumbers();
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/metal_specular.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/metal-wires.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/metal-wires_specular.jpg");
        int[] textureId11 = TextureLibrary.loadTexture(gl, "textures/ring.jpg");
        int[] textureId12 = TextureLibrary.loadTexture(gl, "textures/ring_specular.jpg");
        int[] textureId13 = TextureLibrary.loadTexture(gl, "textures/gem.jpg");
        int[] textureId14 = TextureLibrary.loadTexture(gl, "textures/gem_specular.jpg");


        sphere = new Sphere(gl, textureId1, textureId2);
        cube = new Cube(gl, textureId3, textureId4);
        ringTexture = new Sphere(gl, textureId11, textureId12);
        gem = new Sphere(gl, textureId13, textureId14);

        main_light = new Light(gl);
        main_light.setCamera(camera);
        main_light.setAmbient(1f);

        wall_light = new Light(gl);
        wall_light.setCamera(camera);
        wallLightOn();

        floor_light = new Light(gl);
        floor_light.setCamera(camera);

        floor_light2 = new Light(gl);
        floor_light2.setCamera(camera);

        floorLightOn();

        spotLight = new Light(gl, true, 10f);
        spotLight.setSpecular(0f);
        spotLight.setAmbient(0f);
        spotLight.setDiffuse(0.1f,0.1f,0.0f);

        lights = new ArrayList();

        lights.add(main_light);
        lights.add(wall_light);
        lights.add(floor_light);
        lights.add(floor_light2);
        lights.add(spotLight);

        outsideLights = new ArrayList();
        outsideLights.add(main_light);
        outsideLights.add(spotLight);
        main_light.setPosition(getLightPosition());
        wall_light.setPosition(getWallLightPosition());
        floor_light.setPosition(getFloorLightPosition());
        floor_light2.setPosition(getFloorLight2Position());
        room = new Room(gl, lights, camera, randoms);
        outside = new Outside(gl, outsideLights, camera, randoms);

        for(Light light: lights) {
            sphere.addLight(light);
            cube.addLight(light);
            ringTexture.addLight(light);
            gem.addLight(light);
        }

        sphere.setCamera(camera);
        cube.setCamera(camera);
        ringTexture.setCamera(camera);
        gem.setCamera(camera);

        spotLight.setCamera(camera);

        float palmHeight = 3.0f;
        float palmWidth = 2.5f;
        float palmDepth = 0.8f;
        float armHeight = 1.5f;
        float armWidth = 1f;
        float armDepth = 1f;


        robot = new NameNode("root");
        TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0.75f,0));

        NameNode arm = new NameNode("arm");
        Mat4 m = Mat4Transform.scale(armWidth,armHeight,armDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0,0));
        TransformNode armTransform = new TransformNode("arm transform", m);
        MeshNode armShape = new MeshNode("Cube(arm)", cube);
        armRotate = new TransformNode("arm rotate", Mat4Transform.rotateAroundZ(0));

        NameNode palm = new NameNode("palm");
        m = Mat4Transform.scale(palmWidth,palmHeight,palmDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.75f,0));
        TransformNode bodyTransform = new TransformNode("body transform", m);
        MeshNode bodyShape = new MeshNode("Cube(body)", cube);

        thumb = new Thumb(gl, 1.6f, 3f, -0.4f, 0, sphere);
        index = new Finger(gl, 1.1f, 0.5f + 3f*(2f/3f), 2.2f, 0, sphere, ringTexture, gem, spotLight);
        middle = new Finger(gl, 1.3f, 0.5f + 2f*(2f/3f), 2.2f, 0, sphere);
        ring = new Finger(gl, 1.1f, 0.5f + (2f/3f), 2.2f, 0, sphere);
        little = new Finger(gl, 0.8f, 0.5f, 2.2f, 0, sphere);

        robot.addChild(robotTranslate);
        robotTranslate.addChild(arm);
        arm.addChild(armRotate);
        armRotate.addChild(armTransform);
        armTransform.addChild(armShape);
        armRotate.addChild(palm);
        palm.addChild(bodyTransform);
        bodyTransform.addChild(bodyShape);
        palm.addChild(thumb.getFinger());
        palm.addChild(index.getFinger());
        palm.addChild(middle.getFinger());
        palm.addChild(ring.getFinger());
        palm.addChild(little.getFinger());

        robot.update();  // IMPORTANT - don't forget this
    }

    private void render(GL3 gl) {
        //Change the colour of the background if it's night time
        if (isNight) {
            gl.glClearColor(0, 0.071f, 0.091f, 1.0f);
        } else {
            gl.glClearColor(0.529f, 0.808f, 0.922f, 1.0f);
        }
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        updatePerspectiveMatrices();
        room.render(gl);
        outside.render(gl);

        main_light.render(gl);
        wall_light.render(gl);
        floor_light.render(gl);
        floor_light2.render(gl);
        if (animation) runAnimation(gl);
        robot.draw(gl);

        // Move the spotlight with the ring
        spotLight.render(gl,rotateSpotLightAtOrigin(index.index1.worldTransform) );
    }



    private void updatePerspectiveMatrices() {
        // needs to be changed if user resizes the window
        perspective = Mat4Transform.perspective(45, aspect);
        room.updatePerspective(perspective);
        outside.updatePerspective(perspective);

        main_light.setPerspective(perspective);
        spotLight.setPerspective(perspective);
        wall_light.setPerspective(perspective);
        floor_light.setPerspective(perspective);
        floor_light2.setPerspective(perspective);
        sphere.setPerspective(perspective);
        cube.setPerspective(perspective);
        ringTexture.setPerspective(perspective);
        gem.setPerspective(perspective);
    }

    private void disposeMeshes(GL3 gl) {
        room.dispose(gl);
        outside.dispose(gl);
        main_light.dispose(gl);
        wall_light.dispose(gl);
        floor_light2.dispose(gl);
        floor_light.dispose(gl);
        spotLight.dispose(gl);
        sphere.dispose(gl);
        cube.dispose(gl);
        ringTexture.dispose(gl);
        gem.dispose(gl);

    }

    private void runAnimation(GL3 gl) {
        double elapsedTime = getSeconds()-startTime;

        //Start M
        little.curlFinger(0, 2, elapsedTime, 0, 120);
        little.angleFinger(0,2, elapsedTime, 0, 30);
        ring.angleFinger(0, 2, elapsedTime, 0, 10);
        index.angleFinger(0, 2, elapsedTime, 0, 10);
        middle.angleFinger(0, 2, elapsedTime, 0, 10);
        thumb.thumbForward(0.5, 2, elapsedTime, 0, 60);
        thumb.thumbOut(0, 2, elapsedTime, 0, -50);
        thumb.curlFinger(0, 2, elapsedTime, 0, -60);
        thumb.angleFinger(0,2, elapsedTime, 0, 130);
        if (elapsedTime > 0.5) {
            ring.curlFinger(0.5, 2, elapsedTime, 0, 90);
            middle.curlFinger(0.5, 2, elapsedTime, 0, 105);
            index.curlFinger(0.5, 2, elapsedTime, 0, 120);
        }
        //M Completed

        //Start J
        if (elapsedTime > 4.5) {
            ring.curlFinger(4.5, 1, elapsedTime, 90, 0);
            middle.curlFinger(4.5, 1, elapsedTime, 105, 0);
            index.curlFinger(4.5, 1, elapsedTime, 120, 0);
        }

        if (elapsedTime > 5) {
            thumb.angleFinger(5.0,2, elapsedTime, 130, 0);
            thumb.thumbOut(5, 1, elapsedTime, -50, -70);
        }

        if (elapsedTime > 5.5) {

            ring.angleFinger(5.5, 0.5, elapsedTime, 10, 15);
            middle.angleFinger(5.5, 0.5, elapsedTime, 10, 15);
            index.angleFinger(5.5, 0.5, elapsedTime, 10, 15);

            ring.curlFinger(5.5, 2, elapsedTime, 0, 110);
            middle.curlFinger(5.5, 2, elapsedTime, 0, 110);
            index.curlFinger(5.5, 2, elapsedTime, 0, 110);

            little.angleFinger(5.5, 2, elapsedTime, 30, 0);
            little.curlFinger(5.5, 2, elapsedTime, 120, 0);
        }

        if (elapsedTime > 6.5) {
            thumb.angleFinger(6.5, 2, elapsedTime, 0,100);
        }
        // J Completed
        // Start W
        if (elapsedTime > 10.5) {
            thumb.angleFinger(10.5, 2, elapsedTime, 100,0);
        }

        if (elapsedTime > 12.5) {
            little.curlFinger(12.5, 2, elapsedTime, 0, 100);
            little.angleFinger(12.5,1, elapsedTime, -10, 30);
            ring.angleFinger(12.5, 1, elapsedTime, 5, 10);
            index.angleFinger(12.5, 1, elapsedTime, 0, -10);
            middle.angleFinger(12.5, 1, elapsedTime, 10, 0);

            ring.curlFinger(12.5, 2, elapsedTime, 120, 0);
            middle.curlFinger(12.5, 2, elapsedTime, 120, 0);
            index.curlFinger(12.5, 2, elapsedTime, 130, 0);
        }

        if (elapsedTime > 14) {
            thumb.thumbOut(14, 0.5, elapsedTime, -70, -50);
            thumb.angleFinger(14, 1, elapsedTime, 0, 120);
        }
        // W Completed
        // Start Vulcan Salute
        if (elapsedTime > 17) {
            thumb.thumbForward(17, 1, elapsedTime, 60, 0);
            thumb.angleFinger(17, 2, elapsedTime, 120, 00);

            little.curlFinger(17, 2, elapsedTime, 100, 0);
            ring.angleFinger(17, 1, elapsedTime, 10, 30);

            index.angleFinger(17, 1, elapsedTime, 0, -30);
            middle.angleFinger(17, 1, elapsedTime, 0, -30);

        }
        //Vulcan Salute completed
    }


    private Mat4 rotateSpotLightAtOrigin(Mat4 current) {
        float xpos = current.get(0, 3);
        float ypos = current.get(1, 3);
        float zpos = current.get(2, 3);
        current.set(0, 3, 0);
        current.set(1, 3, 0);
        current.set(2, 3, 0);
        current = Mat4.multiply(current, Mat4Transform.rotateAroundX(180));
        current = Mat4.multiply(current, Mat4Transform.rotateAroundY((float) -45.0));
        current = Mat4.multiply(current, Mat4Transform.rotateAroundZ((float) -90.0));
        current.set(0, 3, xpos);
        current.set(1, 3, ypos);
        current.set(2, 3, zpos);
        return current;
    }

    private Vec3 getLightPosition() {
        float x = 0f;
        float y = 20f;
        float z = 0f;
        return new Vec3(x,y,z);
    }
    private Vec3 getWallLightPosition() {
        float x = 0f;
        float y = 10f;
        float z = 0f;
        return new Vec3(x,y,z);
    }
    private Vec3 getFloorLightPosition() {
        float x = 5f;
        float y = 2f;
        float z = 5f;
        return new Vec3(x,y,z);
    }

    private Vec3 getFloorLight2Position() {
        float x = -5f;
        float y = 2f;
        float z = -5f;
        return new Vec3(x,y,z);
    }
}