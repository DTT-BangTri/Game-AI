package HillClimbing;

/**
 * @author VUWIT14 - Group 01
 */

public class Queen {
    private int i, j, h;//i là vị trí hàng, j là vị trí cột, h là số đối kháng

    public Queen(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public String toString() {
        return i + "," + j;
    }
}
