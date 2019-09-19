package Model;

import java.util.Random;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public class Environment {

    private static Environment unique;
    private int[][] matrix;
    private int row, column, countDust;

    private Environment() {
    }

    public void createEnvironment(int r, int c) {
        countDust = 0;
        column = c;
        row = r;
        Random random = new Random();
        matrix = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                matrix[i][j] = random.nextInt(2);
                if(matrix[i][j] == 1){
                    countDust++;
                }
            }
        }
        printMatrix();
    }

    public static Environment getInstance() {
        if (unique == null) {
            unique = new Environment();
        }
        return unique;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getCountDust() {
        return countDust;
    }

    public void printMatrix(){
        for (int[] array : matrix) {
            for (int value : array) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}
