/* I declare that this code is my own work */
/* Author Matthew Walker mjwalker1@sheffield.ac.uk */

import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.event.ChangeListener;
import javax.swing.event.*;

public class Arty extends JFrame implements ActionListener  {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
    private GLCanvas canvas;
    private Arty_GLEventListener glEventListener;
    private final FPSAnimator animator;
    private Camera camera;
    private JSlider lightDimmer;
    private boolean main_lights = true;
    private boolean floor_lights = true;
    private boolean wall_lights = true;

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        Arty b1 = new Arty("Arty");
        b1.getContentPane().setPreferredSize(dimension);
        b1.pack();
        b1.setVisible(true);
    }

    public Arty(String textForTitleBar) {
        super(textForTitleBar);
        GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        canvas = new GLCanvas(glcapabilities);
        camera = new Camera(new Vec3(4f,7f,7.9f), Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
        glEventListener = new Arty_GLEventListener(camera);
        canvas.addGLEventListener(glEventListener);
        canvas.addMouseMotionListener(new MyMouseInput(camera));
        canvas.addKeyListener(new MyKeyboardInput(camera));
        getContentPane().add(canvas, BorderLayout.CENTER);

        JMenuBar menuBar=new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);

        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(new BoxLayout(buttonArea, BoxLayout.PAGE_AXIS));

        JPanel p = new JPanel();
        JButton b = new JButton("start");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("stop");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("reset");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("M");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("J");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("W");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Vulcan Salute");
        b.addActionListener(this);
        p.add(b);


        buttonArea.add(p);

        p = new JPanel();
        JToggleButton tb = new JToggleButton("Night time");
        tb.addActionListener(this);
        p.add(tb);
        tb = new JToggleButton("Floor Lights");
        tb.addActionListener(this);
        p.add(tb);
        tb = new JToggleButton("Ceiling Lights");
        tb.addActionListener(this);
        p.add(tb);
        p.add(new JLabel("Outside Light dimmer:"));
        lightDimmer = new JSlider(JSlider.HORIZONTAL,
                0, 100, 100);
        lightDimmer.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    float intensity = (float)source.getValue() / 100;
                    glEventListener.setLight(intensity);
                }
            }
        });
        lightDimmer.setMajorTickSpacing(10);
        lightDimmer.setMinorTickSpacing(1);
        lightDimmer.setPaintTicks(true);
        lightDimmer.setPaintLabels(true);
        p.add(lightDimmer);
        buttonArea.add(p);

        this.add(buttonArea, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                animator.stop();
                remove(canvas);
                dispose();
                System.exit(0);
            }
        });
        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("start")) {
            glEventListener.startAnimation();
        }
        else if (e.getActionCommand().equalsIgnoreCase("stop")) {
            glEventListener.stopAnimation();
        }
        else if (e.getActionCommand().equalsIgnoreCase("reset")) {
            glEventListener.resetHand();
        }
        else if (e.getActionCommand().equalsIgnoreCase("M")) {
            glEventListener.M();
        }
        else if (e.getActionCommand().equalsIgnoreCase("J")) {
            glEventListener.J();
        }
        else if (e.getActionCommand().equalsIgnoreCase("W")) {
            glEventListener.W();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Vulcan Salute")) {
            glEventListener.VulcanSalute();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Night time")) {
            if (main_lights) {
                glEventListener.lightOut();
                lightDimmer.setEnabled(false);
            } else {
                glEventListener.lightOn();
                lightDimmer.setEnabled(true);
                glEventListener.setLight((float) lightDimmer.getValue() / 100);
            }

            main_lights = !main_lights;
        }
        else if (e.getActionCommand().equalsIgnoreCase("Floor Lights")) {
            if (floor_lights) {
                glEventListener.floorLightOut();
            } else {
                glEventListener.floorLightOn();
            }

            floor_lights = !floor_lights;
        }
        else if (e.getActionCommand().equalsIgnoreCase("Ceiling Lights")) {
            if (wall_lights) {
                glEventListener.wallLightOut();
            } else {
                glEventListener.wallLightOn();
            }

            wall_lights = !wall_lights;
        }
        else if(e.getActionCommand().equalsIgnoreCase("quit"))
            System.exit(0);
    }

}

class MyKeyboardInput extends KeyAdapter  {
    private Camera camera;

    public MyKeyboardInput(Camera camera) {
        this.camera = camera;
    }

    public void keyPressed(KeyEvent e) {
        Camera.Movement m = Camera.Movement.NO_MOVEMENT;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
            case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
            case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
            case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
            case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
            case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
        }
        camera.keyboardInput(m);
    }
}

class MyMouseInput extends MouseMotionAdapter {
    private Point lastpoint;
    private Camera camera;

    public MyMouseInput(Camera camera) {
        this.camera = camera;
    }

    /**
     * mouse is used to control camera position
     *
     * @param e  instance of MouseEvent
     */
    public void mouseDragged(MouseEvent e) {
        Point ms = e.getPoint();
        float sensitivity = 0.001f;
        float dx=(float) (ms.x-lastpoint.x)*sensitivity;
        float dy=(float) (ms.y-lastpoint.y)*sensitivity;
        //System.out.println("dy,dy: "+dx+","+dy);
        if (e.getModifiers()==MouseEvent.BUTTON1_MASK)
            camera.updateYawPitch(dx, -dy);
        lastpoint = ms;
    }

    /**
     * mouse is used to control camera position
     *
     * @param e  instance of MouseEvent
     */
    public void mouseMoved(MouseEvent e) {
        lastpoint = e.getPoint();
    }
}