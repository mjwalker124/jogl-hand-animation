/* I declare that this code is my own work */
/* Author Matthew Walker mjwalker1@sheffield.ac.uk */

import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Finger {
    private NameNode finger;
    private GL3 gl;
    public NameNode index1;
    public NameNode ringGem;
    public TransformNode fingerRotateZ;
    public TransformNode fingerRotateY;
    public TransformNode part1RotateX;
    public TransformNode part2RotateX;
    public TransformNode part3RotateX;
    public NameNode getFinger() {
        return finger;
    }

    public Finger(GL3 gl, float height, float x, float y, float z, Mesh sphere) {
        this.gl = gl;
        this.finger = createFinger(height, x, y, z, sphere, null, null, null);
    }

    public Finger(GL3 gl, float height, float x, float y, float z, Mesh sphere, Mesh ring, Mesh gem, Light spotlight) {
        this.gl = gl;
        this.finger = createFinger(height, x, y, z, sphere, ring, gem, spotlight);
    }

    public NameNode createFinger(float height, float x, float y, float z, Mesh sphere, Mesh ring, Mesh gem, Light spotlight) {
        boolean hasRing = (spotlight != null);

        Mat4 m;
        NameNode finger = new NameNode("finger");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-(1.5f) + x,1.8f + y,z));
        TransformNode indexTranslate = new TransformNode("finger translate", m);
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(1f,height,1f));
        TransformNode fingerTransform = new TransformNode("finger transform", m);

        index1 = new NameNode("index1");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f,height,0.5f));
        TransformNode index1Transform = new TransformNode("index1 transform", m);
        MeshNode index1Shape = new MeshNode("Sphere(index1)", sphere);
        part1RotateX = new TransformNode("finger rotate", Mat4Transform.rotateAroundX(0));

        fingerRotateY = new TransformNode("finger rotate y", Mat4Transform.rotateAroundY(0));
        fingerRotateZ = new TransformNode("finger rotate z", Mat4Transform.rotateAroundZ(0));

        NameNode ringLight = new NameNode("ring");
        m = Mat4Transform.scale(0.6f,0.2f,0.6f);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.75f,0));
        TransformNode ringTransform = new TransformNode("body transform", m);
        MeshNode ringShape = new MeshNode("Sphere(body)", sphere);
        if (hasRing) {
            ringShape = new MeshNode("Sphere(body)", ring);
        }
        ringGem = new NameNode("ring gem");
        m = Mat4Transform.scale(0.6f,1.2f,0.6f);
        m = Mat4.multiply(m, Mat4Transform.translate(0f,0f,-1f));
        TransformNode gemTransform = new TransformNode("body transform", m);
        MeshNode gemShape = new MeshNode("Sphere(body)", sphere);
        if (hasRing) {
            gemShape = new MeshNode("Sphere(body)", gem);
        }

        NameNode index2 = new NameNode("index2");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f,height,0.5f));
        TransformNode index2Transform = new TransformNode("index2 transform", m);

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0,height,0));
        TransformNode index2Translate = new TransformNode("index2 translate", m);

        MeshNode index2Shape = new MeshNode("Sphere(index2)", sphere);
        part2RotateX = new TransformNode("index2 rotate", Mat4Transform.rotateAroundX(0));

        NameNode index3 = new NameNode("index3");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f,height,0.5f));
        TransformNode index3Transform = new TransformNode("index3 transform", m);

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0,height*1,0));
        TransformNode index3Translate = new TransformNode("index3 translate", m);

        MeshNode index3Shape = new MeshNode("Sphere(index3)", sphere);
        part3RotateX = new TransformNode("index3 rotate", Mat4Transform.rotateAroundX(0f));

        finger.addChild(indexTranslate);
        indexTranslate.addChild(fingerRotateZ);
        fingerRotateZ.addChild(fingerRotateY);
        fingerRotateY.addChild(part1RotateX);
        part1RotateX.addChild(index1);
        index1.addChild(index1Transform);

        index1Transform.addChild(index1Shape);
        if (hasRing) {
            index1.addChild(ringLight);
            ringLight.addChild(ringTransform);
            ringTransform.addChild(ringShape);

            ringTransform.addChild(ringGem);
            ringGem.addChild(gemTransform);
            gemTransform.addChild(gemShape);
        }
        index1.addChild(index2);
        index2.addChild(index2Translate);
        index2Translate.addChild(part2RotateX);
        part2RotateX.addChild(index2Transform);
        index2Transform.addChild(index2Shape);
        part2RotateX.addChild(index3);
        index3.addChild(index3Translate);
        index3Translate.addChild(part3RotateX);
        part3RotateX.addChild(index3Transform);
        index3Transform.addChild(index3Shape);

        return finger;
    }

    public void curlFinger(double startTime, double timeToComplete, double elapsedTime, double startPoint, double finishPoint) {
        double position = 0;
        elapsedTime -= startTime;

        if (startPoint <= finishPoint) {
            double speed = (finishPoint - startPoint) / timeToComplete;
            if (elapsedTime * speed < finishPoint) {
                position = (elapsedTime * speed) + startPoint;
            } else {
                position = finishPoint;
            }
        } else {
            double speed = (startPoint - finishPoint) / timeToComplete;
            if (startPoint - (elapsedTime * speed) > finishPoint) {
                position = startPoint - (elapsedTime * speed) ;
            } else {
                position = finishPoint;
            }
        }

        part1RotateX.setTransform(Mat4Transform.rotateAroundX((float) position));
        part2RotateX.setTransform(Mat4Transform.rotateAroundX((float) position * 0.5f));
        part3RotateX.setTransform(Mat4Transform.rotateAroundX((float) position * 0.25f));
        part1RotateX.update();
        part2RotateX.update();
        part3RotateX.update();
    }

    public void angleFinger(double startTime, double timeToComplete, double elapsedTime, double startAngle, double finishAngle) {
        double angle = 0;
        elapsedTime -= startTime;

        if (startAngle <= finishAngle) {
            double speed = (finishAngle - startAngle) / timeToComplete;
            if (elapsedTime * speed < finishAngle) {
                angle = (elapsedTime * speed) + startAngle;
            } else {
                angle = finishAngle;
            }
        } else {
            double speed = (startAngle - finishAngle) / timeToComplete;
            if (startAngle - (elapsedTime * speed) > finishAngle) {
                angle = startAngle - (elapsedTime * speed);
            } else {
                angle = finishAngle;
            }
        }

        fingerRotateZ.setTransform(Mat4Transform.rotateAroundZ((float) angle));
        fingerRotateZ.update();
    }
}