package HillClimbing;

import Observer.IEvm;
import Observer.IHillClimbing;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author VUWIT14 - Group 01
 */
public class HillClimbing implements IHillClimbing {

    private IEvm iEvm;
    private static HillClimbing unique; // thể hiện duy nhất
    private int[][] matrix; // thể hiện bàn cờ
    private int sizeMatrix; // kích thước của bàn cờ
    private ArrayList<Queen> listQueen; // danh sách các quân hậu
    private int numberPairOfQueen_Antagonistic; // số cặp hậu đối kháng
    private Queen[] arrayQueen = null; // [0] là chứa quân hậu để di chuyển, [1] chứa ô cờ mà quân hậu di chuyển tới

    // tạo thể hiện duy nhất
    public static HillClimbing getInstance() {
        if (unique == null) {
            unique = new HillClimbing();
        }
        return unique;
    }

    // tạo bàn cờ
    public void createChessboard(int n) {
        setSizeMatrix(n);
        matrix = new int[getSizeMatrix()][getSizeMatrix()];
    }

    // tạo trạng thái quân hậu
    public void createStateQueen() {
        Queen tempQueen;
        listQueen = new ArrayList<Queen>();
        Random random = new Random();

        while (listQueen.size() < getSizeMatrix()) {
            tempQueen = new Queen(random.nextInt(getSizeMatrix()), random.nextInt(getSizeMatrix()));
            if (!samePositionQueen(tempQueen)) {
                listQueen.add(tempQueen);
            }
        }
    }

    // kiểm tra vị trí quân hậu
    private boolean samePositionQueen(Queen tempQueen) {
        for (Queen queen : getListQueen()) {
            if (queen.getI() == tempQueen.getI() && queen.getJ() == tempQueen.getJ()) {
                return true;
            }
        }
        return false;
    }

    // tạo trạng thái bàn cờ
    public void createStateChessboard() {
        for (Queen queen : getListQueen()) {
            matrix[queen.getI()][queen.getJ()] = 1;
        }
        setNumberPairOfQueen_Antagonistic(numberPairOfQueen_Antagonistic());
    }

    // tính số cặp hậu đối kháng
    private int numberPairOfQueen_Antagonistic() {
        // sô cặp hậu theo hàng ngang - dọc
        int sum = 0, sumRow, sumColumn;
        for (int i = 0; i < getSizeMatrix(); i++) {
            sumRow = 0;
            sumColumn = 0;
            for (int j = 0; j < getSizeMatrix(); j++) {
                if (matrix[i][j] == 1) { // tính số hậu trên 1 hàng
                    sumRow++;
                }
                if (matrix[j][i] == 1) { // tính số hậu trên 1 cột
                    sumColumn++;
                }
            }
            // cứ 2 hậu kề nhau sẽ có 1 đối kháng (thuật toán này cho 3 hậu kề nhau thì sẽ có 2 đối kháng)
            if (sumRow > 1) {
                sum = sum + (sumRow - 1);
            }
            if (sumColumn > 1) {
                sum = sum + (sumColumn - 1);
            }
        }

        // sắp xếp lại thứ tự các quân hậu theo vị trí hàng (hàng nhỏ thì đúng trước)
        ArrayList<Queen> tempListQueen = getListQueen();
        Queen tempQueen;
        for (int i = 0; i < getSizeMatrix(); i++) {
            for (int j = i; j < getSizeMatrix(); j++) {
                if (tempListQueen.get(i).getI() >= tempListQueen.get(j).getI()) {
                    tempQueen = tempListQueen.get(i);
                    tempListQueen.set(i, tempListQueen.get(j));
                    tempListQueen.set(j, tempQueen);
                }
            }
        }

        // sô cặp hậu theo hàng đường chéo dưới trái - phải (vì đã sắp xếp hậu theo hàng rồi)
        int i, j;
        for (Queen queen : tempListQueen) {
            // đường chéo dưới phải
            i = queen.getI();
            j = queen.getJ();
            while (++i < getSizeMatrix() && ++j < getSizeMatrix()) {
                if (matrix[i][j] == 1) {
                    sum += 1;
                    break;
                }
            }
            // đường chéo dưới trái
            i = queen.getI();
            j = queen.getJ();
            while (++i < getSizeMatrix() && --j >= 0) {
                if (matrix[i][j] == 1) {
                    sum += 1;
                    break;
                }
            }
        }
        return sum;
    }

    // tính số đối kháng của một quân hậu
    private int numberOfQueen_Antagonistic(Queen queen) {
        int i, j, h = 0;
        // tính bên trên
        i = queen.getI();
        j = queen.getJ();
        while (--i >= 0) {
            if (matrix[i][j] == 1) {
                h++;
                break;
            }
        }
        // tính bên dưới
        i = queen.getI();
        while (++i < getSizeMatrix()) {
            if (matrix[i][j] == 1) {
                h++;
                break;
            }
        }
        // tính bên phải
        i = queen.getI();
        while (++j < getSizeMatrix()) {
            if (matrix[i][j] == 1) {
                h++;
                break;
            }
        }
        // tính bên trái
        j = queen.getJ();
        while (--j >= 0) {
            if (matrix[i][j] == 1) {
                h++;
                break;
            }
        }
        // tính chéo dưới phải
        j = queen.getJ();
        while (++i < getSizeMatrix() && ++j < getSizeMatrix()) {
            if (matrix[i][j] == 1) {
                h++;
                break;
            }
        }
        // tính chéo trên trái
        i = queen.getI();
        j = queen.getJ();
        while (--i >= 0 && --j >= 0) {
            if (matrix[i][j] == 1) {
                h++;
                break;
            }
        }
        // tính chéo trên phải
        i = queen.getI();
        j = queen.getJ();
        while (--i >= 0 && ++j < getSizeMatrix()) {
            if (matrix[i][j] == 1) {
                h++;
                break;
            }
        }
        // tính chéo dưới trái
        i = queen.getI();
        j = queen.getJ();
        while (++i < getSizeMatrix() && --j >= 0) {
            if (matrix[i][j] == 1) {
                h++;
                break;
            }
        }
        return h;
    }
    // săp xếp quân hậu theo chiều đối kháng nhiều nhất

    private ArrayList<Queen> listQueen_Antagonistic() {
        for (Queen queen : getListQueen()) {
            queen.setH(numberOfQueen_Antagonistic(queen));
        }
        ArrayList<Queen> tempListQueen = getListQueen();
        Queen tempQueen;
        for (int i = 0; i < getSizeMatrix(); i++) {
            for (int j = i; j < getSizeMatrix(); j++) {
                if (tempListQueen.get(i).getH() <= tempListQueen.get(j).getH()) {
                    tempQueen = tempListQueen.get(i);
                    tempListQueen.set(i, tempListQueen.get(j));
                    tempListQueen.set(j, tempQueen);
                }
            }
        }
        return tempListQueen;
    }

    // tìm ô cờ đề di chuyển quân hậu tốt nhất
    private Queen[] findBestSquare() {
        int supposeNumberPairOfQueen_Antagonistic; // số cặp hậu đôi kháng ở trạng thái bàn cờ mới
        int tempI, tempJ, count = 0;
        Queen[] tempArrayQueen = null; // [0] là chứa quân hậu để di chuyển, [1] chứa ô cờ mà quân hậu di chuyển tới
        ArrayList<Queen> tempListQueen_Antagonistic = listQueen_Antagonistic();
        for (Queen queen : tempListQueen_Antagonistic) {
            tempI = queen.getI();
            tempJ = queen.getJ();
            matrix[queen.getI()][queen.getJ()] = 0;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    if (matrix[i][j] == 0) {
                        matrix[i][j] = 1;
                        queen.setI(i);
                        queen.setJ(j);
                        supposeNumberPairOfQueen_Antagonistic = numberPairOfQueen_Antagonistic();
                        queen.setI(tempI);
                        queen.setJ(tempJ);
                        if (supposeNumberPairOfQueen_Antagonistic < numberPairOfQueen_Antagonistic) {
                            setNumberPairOfQueen_Antagonistic(supposeNumberPairOfQueen_Antagonistic);
                            tempArrayQueen = new Queen[2];
                            tempArrayQueen[0] = new Queen(queen.getI(), queen.getJ());
                            tempArrayQueen[1] = new Queen(i, j);
                        }
                        matrix[i][j] = 0;
                    }
                }
            }
            matrix[queen.getI()][queen.getJ()] = 1;
            if (tempArrayQueen != null) {
                break;
            }
        }
        return tempArrayQueen;
    }

    // di chuyển con hậu đến ô cờ mong đợi
    private void move(Queen[] queens) {
        matrix[queens[0].getI()][queens[0].getJ()] = 0;
        matrix[queens[1].getI()][queens[1].getJ()] = 1;
        for (Queen queen : listQueen) {
            if (queen.getI() == queens[0].getI() && queen.getJ() == queens[0].getJ()) {
                queen.setI(queens[1].getI());
                queen.setJ(queens[1].getJ());
                break;
            }
        }
    }

    // bắt đầu thuật toán
    public void start() {
        while (numberPairOfQueen_Antagonistic != 0) {
            arrayQueen = findBestSquare();
            if (arrayQueen == null) {
                createChessboard(sizeMatrix);
                createStateQueen();
                createStateChessboard();
            } else {
                commitChessboard();
                move(arrayQueen);
            }
        }
        commitDone();
    }

    // in trạng thái bàn cờ
    public void printMatrix() {
        System.out.println("\nTrạng thái bàn cờ:");
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println("");
        }
    }

    public int getSizeMatrix() {
        return sizeMatrix;
    }

    public void setSizeMatrix(int sizeMatrix) {
        this.sizeMatrix = sizeMatrix;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public ArrayList<Queen> getListQueen() {
        return listQueen;
    }

    public void setListQueen(ArrayList<Queen> listQueen) {
        this.listQueen = listQueen;
    }

    public int getNumberPairOfQueen_Antagonistic() {
        return numberPairOfQueen_Antagonistic;
    }

    public void setNumberPairOfQueen_Antagonistic(int numberPairOfQueen_Antagonistic) {
        this.numberPairOfQueen_Antagonistic = numberPairOfQueen_Antagonistic;
    }

    public Queen[] getArrayQueen() {
        return arrayQueen;
    }

    public void register(IEvm iEvm) {
        this.iEvm = iEvm;
    }

    public void commitChessboard() {
        iEvm.updateChessboard();
    }

    public void commitDone() {
        iEvm.updateDone();
    }

}
