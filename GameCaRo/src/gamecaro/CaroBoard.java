package gamecaro;

public class CaroBoard {

    private int[][] square ;
    private int size;

    public CaroBoard(int n) {
        this.size = n;
        square  = new int[size][size];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                square [i][j] = 0;
            }
        }
    }

    public void set(int a, int b, int x) {
        this.square [a][b] = x;
    }

    public int[][] getSquare() {
        return square;
    }

    public void setSquare(int[][] square) {
        this.square = square;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
