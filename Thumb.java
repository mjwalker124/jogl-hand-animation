//This class modifies the finger class so a thumb will poerate in a more thumb like way than a finger;

import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Thumb extends Finger {
    public Thumb(GL3 gl, float height, float x, float y, float z, Mesh sphere) {
        super(gl, height, x, y, z, sphere);
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

        fingerRotateY.setTransform(Mat4Transform.rotateAroundY((float) position));
        fingerRotateY.update();
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
        part2RotateX.setTransform(Mat4Transform.rotateAroundX((float) angle * 0.5f));
        part3RotateX.setTransform(Mat4Transform.rotateAroundX((float) angle * 0.25f));
        part2RotateX.update();
        part3RotateX.update();
    }

    public void thumbOut(double startTime, double timeToComplete, double elapsedTime, double startAngle, double finishAngle) {
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
    public void thumbForward(double startTime, double timeToComplete, double elapsedTime, double startAngle, double finishAngle) {
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
        part1RotateX.setTransform(Mat4Transform.rotateAroundX((float) angle));
        part1RotateX.update();
    }
}