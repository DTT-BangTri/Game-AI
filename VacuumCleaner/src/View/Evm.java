package View;

import Commit.IEvm;
import Model.Environment;
import Model.Robot;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public class Evm extends JPanel implements MouseMotionListener, MouseListener, KeyListener, IEvm {

    private int[][] matrix;
    private int widthASquare, heightSquare;
    private int widthEvm, heightEvm;
    private Image imgTrash, imgGrass, imgRobot, imgRobotSuck;
    private int xMouseMove, yMouseMove;
    private int xMousePressed, yMousePressed;
    private int xRobot, yRobot;
    private int xLocationRobot, yLocationRobot;
    private int widthRadar = - 1, heightRadar = -1;
    private boolean robotInEvm, moving;
    private ArrayList<Trail> trails;
    private Environment environment;
    private Robot robot;
    private static int speed = 5;
    private Window window;
    public Evm(Window w) {
        this.window = w;
        imgTrash = new ImageIcon(getClass().getResource("/Image/rac.png")).getImage();
        imgGrass = new ImageIcon(getClass().getResource("/Image/grass.jpg")).getImage();
        imgRobot = new ImageIcon(getClass().getResource("/Image/robot_d.png")).getImage();
        imgRobotSuck = null;
        environment = Environment.getInstance();
        robot = Robot.getIntance();
        robot.registry(Evm.this);
        addMouseMotionListener(Evm.this);
        addMouseListener(Evm.this);
        addKeyListener(Evm.this);
        trails = new ArrayList<Trail>();
    }

    public void createMatrix(int widthEvm, int heightEvm) {
        this.widthEvm = widthEvm;
        this.heightEvm = heightEvm;
        this.matrix = new int[widthEvm][heightEvm];
        widthASquare = getWidth() / widthEvm;
        heightSquare = getHeight() / heightEvm;
        matrix = environment.getMatrix();
        trails.removeAll(trails);
        window.jlCountIphone.setText(String.valueOf(environment.getCountDust()));
        //------
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (widthEvm != 0) {
            widthASquare = getWidth() / widthEvm;
            heightSquare = getHeight() / heightEvm;
        }
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < widthEvm; i++) {
            for (int j = 0; j < heightEvm; j++) {
                Point point = new Point(j * widthASquare, i * heightSquare);
                g2.drawImage(imgGrass, point.x, point.y, widthASquare, heightSquare, this);
                if (matrix[i][j] == 1) {
                    g2.drawImage(imgTrash, point.x, point.y, widthASquare, heightSquare, this);
                }
            }
        }
        //----radar------
        g2.setColor(Color.red);
        g2.drawOval(xLocationRobot + (widthASquare / 3) - widthRadar, yLocationRobot + (heightSquare / 3) - heightRadar, widthRadar * 2, heightRadar * 2);
        //--
        try {
            for (Trail trail : trails) {
                int x = trail.x * widthASquare;
                int y = trail.y * heightSquare;
                g2.drawImage(trail.image, x, y, widthASquare, heightSquare, this);
            }
        } catch (ConcurrentModificationException e) {
        }
        //---
        if (RobotStand.pressed) {
            g2.drawImage(new ImageIcon(getClass().getResource("/Image/robot_d.png")).getImage(), xMouseMove - (widthASquare / 3), yMouseMove - (heightSquare / 3), widthASquare - widthASquare / 3, heightSquare - heightSquare / 3, this);
        }
        //----
        if (robotInEvm) {
            if (moving) {
                g2.drawImage(imgRobot, xLocationRobot, yLocationRobot, widthASquare - widthASquare / 3, heightSquare - heightSquare / 3, this);
            } else {
                xLocationRobot = xRobot * widthASquare + widthASquare / 5;
                yLocationRobot = yRobot * heightSquare;
                g2.drawImage(new ImageIcon(getClass().getResource("/Image/robot_d.png")).getImage(), xLocationRobot, yLocationRobot, widthASquare - widthASquare / 3, heightSquare - heightSquare / 3, this);
            }
        }
        //---Ve RAC
        g2.drawImage(imgRobotSuck, xLocationRobot, yLocationRobot, widthASquare - widthASquare / 3, heightSquare - heightSquare / 3, this);


    }

    private void moveRobotUp() {
        imgRobot = new ImageIcon(getClass().getResource("/Image/robot_u.png")).getImage();
        moving = true;
        trails.add(new Trail(xRobot, yRobot, 'u'));
        yRobot -= 1;
        final int yLocationRobotTemp = yRobot * heightSquare;
        xLocationRobot = xRobot * widthASquare + widthASquare / 5;
        while (true) {
            yLocationRobot--;
            repaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
            }
            if (yLocationRobot == yLocationRobotTemp) {
                break;
            }
        }
        moving = false;
    }

    private void moveRobotDown() {
        imgRobot = new ImageIcon(getClass().getResource("/Image/robot_d.png")).getImage();
        moving = true;
        trails.add(new Trail(xRobot, yRobot, 'd'));
        yRobot += 1;
        final int yLocationRobotTemp = yRobot * heightSquare;
        xLocationRobot = xRobot * widthASquare + widthASquare / 5;
        while (true) {
            yLocationRobot++;
            repaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
            }
            if (yLocationRobot == yLocationRobotTemp) {
                break;
            }
        }
        moving = false;
    }

    private void moveRobotLeft() {
        imgRobot = new ImageIcon(getClass().getResource("/Image/robot_l.png")).getImage();
        moving = true;
        trails.add(new Trail(xRobot, yRobot, 'l'));
        xRobot -= 1;
        final int xLocationRobotTemp = xRobot * widthASquare + widthASquare / 5;
        yLocationRobot = yRobot * heightSquare;
        while (true) {
            xLocationRobot--;
            repaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
            }
            if (xLocationRobot == xLocationRobotTemp) {
                break;
            }
        }
        moving = false;
    }

    private void moveRobotRight() {
        imgRobot = new ImageIcon(getClass().getResource("/Image/robot_r.png")).getImage();
        moving = true;
        trails.add(new Trail(xRobot, yRobot, 'r'));
        xRobot += 1;
        final int xLocationRobotTemp = xRobot * widthASquare + widthASquare / 5;
        yLocationRobot = yRobot * heightSquare;
        while (true) {
            xLocationRobot++;
            repaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
            }
            if (xLocationRobot == xLocationRobotTemp) {
                break;
            }
        }
        moving = false;
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        if (RobotStand.pressed) {
            robotInEvm = false;
            xMouseMove = e.getX();
            yMouseMove = e.getY();
            repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (RobotStand.pressed) {
            RobotStand.pressed = false;
            xMousePressed = e.getX();
            yMousePressed = e.getY();
            robotInEvm = true;
            for (int i = 0; i < widthEvm; i++) {
                if (xMousePressed >= (i * widthASquare) && xMousePressed < ((i + 1) * widthASquare)) {
                    xRobot = i;
                }
            }
            for (int i = 0; i < heightEvm; i++) {
                if (yMousePressed >= (i * heightSquare) && yMousePressed < ((i + 1) * heightSquare)) {
                    yRobot = i;
                }
            }
            xLocationRobot = xRobot * widthASquare + widthASquare / 5;
            yLocationRobot = yRobot * heightSquare;
            robot.update(environment, yRobot, xRobot);
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(final KeyEvent e) {
        if (!moving) {
            new Thread() {

                @Override
                public void run() {
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                        moveRobotUp();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        moveRobotRight();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                        moveRobotDown();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                        moveRobotLeft();
                    }

                }
            }.start();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void updateMoveUp() {
        moveRobotUp();
    }

    public void updateMoveDown() {
        moveRobotDown();
    }

    public void updateMoveLeft() {
        moveRobotLeft();
    }

    public void updateMoveRight() {
        moveRobotRight();
    }

    public void updateDone() {
        repaint();
        JOptionPane.showConfirmDialog(this, "Hoàn thành!", "", -1);
    }

    public void updateSuck() {
        imgRobotSuck = new ImageIcon(getClass().getResource("/Image/robot_suck.png")).getImage();
        window.jlCountIphone.setText(String.valueOf(robot.count));
        repaint();
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
        }
        imgRobotSuck = null;
    }

    public void updateRadar() {
        try {
            int temp = 0;
            int temp2 = 0;
            int size = widthASquare * (robot.radius);
            int size2 = heightSquare * (robot.radius) + heightSquare / 2;
            while (temp <= size) {
                temp++;
                temp2++;
                widthRadar = temp;
                if(temp2 < size2)
                    heightRadar = temp2;
                repaint();
                Thread.sleep(2);
            }
            Thread.sleep(300);
            widthRadar = -1;
        } catch (InterruptedException ex) {
        }
    }

    public static void setSpeed(int speed) {
        Evm.speed = speed;
    }

    
}
