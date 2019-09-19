package gamecaro;

import java.awt.Point;

/**
 * This class describes the state of the a position on the chessboard, including
 * the position and value of that position
 */
public class State {

    private Point p;
    private int val;

    public State(Point p, int val) {
        this.p = new Point(p);
        this.val = val;
    }

    public void Set(Point p, int val) {
        this.p = new Point(p);
        this.val = val;
    }

    public Point getP() {
        return p;
    }

    public void setP(Point p) {
        this.p = p;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

}
