package View;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public class RobotStand extends JPanel implements MouseListener {

    private Image img;
    private int width, height;
    public static boolean pressed;
    private boolean enable = true;
    private int x, y;

    public RobotStand(int width, int height) {
        this.width = width;
        this.height = height;
        addMouseListener(RobotStand.this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        img = new ImageIcon(getClass().getResource("/Image/robot_d.png")).getImage();
        g.drawImage(img, x, y, width, height, null);
    }

    public void setEnable(boolean b) {
        enable = b;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if(enable)
            pressed = true;
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
