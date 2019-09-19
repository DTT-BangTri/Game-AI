package View;

import HillClimbing.HillClimbing;
import HillClimbing.Queen;
import Observer.IEvm;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author VUWIT14 - Group 01
 */
public class Evm extends JPanel implements IEvm {

    private int[][] matrix;
    private int rowEvm, columnEvm;
    private int widthASquare, heightASquare;
    private Image imgQueen, imgWhite, imgBlack, imgMove;
    private Window window;
    private HillClimbing hillClimbing;
    private static int speed = 2000;
    private boolean finish = false;

    public Evm(Window w) {
        this.window = w;
        hillClimbing = HillClimbing.getInstance();
        hillClimbing.register(this);
        imgQueen = new ImageIcon(getClass().getResource("/Image/Queen.png")).getImage();
        imgWhite = new ImageIcon(getClass().getResource("/Image/White.jpg")).getImage();
        imgBlack = new ImageIcon(getClass().getResource("/Image/Black.jpg")).getImage();
        imgMove = new ImageIcon(getClass().getResource("/Image/Move.png")).getImage();
    }

    public void createMatrix(int row, int column) {
        rowEvm = row;
        columnEvm = column;
        matrix = new int[rowEvm][columnEvm];
        widthASquare = getWidth() / columnEvm;
        heightASquare = getHeight() / rowEvm;
        matrix = hillClimbing.getMatrix();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (rowEvm != 0) {
            widthASquare = getWidth() / columnEvm;
            heightASquare = getHeight() / rowEvm;
        }
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < rowEvm; i++) {
            for (int j = 0; j < columnEvm; j++) {
                Point point = new Point(j * widthASquare, i * heightASquare);
                if ((i + j) % 2 == 0) {
                    g2.drawImage(imgWhite, point.x, point.y, widthASquare, heightASquare, this);
                } else {
                    g2.drawImage(imgBlack, point.x, point.y, widthASquare, heightASquare, this);
                }
            }
        }
        if (!finish && hillClimbing.getArrayQueen() != null && hillClimbing.getArrayQueen()[0] != null && hillClimbing.getArrayQueen()[1] != null) {
            g2.drawImage(imgMove, hillClimbing.getArrayQueen()[0].getJ() * widthASquare, hillClimbing.getArrayQueen()[0].getI() * heightASquare, widthASquare, heightASquare, this);
            g2.drawImage(imgMove, hillClimbing.getArrayQueen()[1].getJ() * widthASquare, hillClimbing.getArrayQueen()[1].getI() * heightASquare, widthASquare, heightASquare, this);
        }
        if (hillClimbing.getListQueen() != null) {
            for (Queen queen : hillClimbing.getListQueen()) {
                matrix[queen.getI()][queen.getJ()] = 1;
                g2.drawImage(imgQueen, queen.getJ() * widthASquare, queen.getI() * heightASquare, widthASquare, heightASquare, this);
            }
        }
    }

    private void execute() {
        repaint();
        window.setNumberPairOfQueen_Antagonistic(hillClimbing.getNumberPairOfQueen_Antagonistic());
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
        }
    }

    public void updateChessboard() {
        finish = false;
        execute();
    }

    public void updateDone() {
        finish = true;
        repaint();
        JOptionPane.showConfirmDialog(this, "Hoàn thành!", "", -1);
    }

    public static void setSpeed(int speed) {
        Evm.speed = speed;
    }
}
