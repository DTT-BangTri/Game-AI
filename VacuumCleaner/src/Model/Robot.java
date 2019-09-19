package Model;

import Commit.EmptyEvm;
import Commit.IEvm;
import Commit.IRobot;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public class Robot implements IRobot{
    private IEvm iEvm;
    private static Robot unique;
    private Environment environment;
    private int i, j;
    public int radius, count;

    private Robot() {
    }

    public static Robot getIntance() {
        if (unique == null) {
            unique = new Robot();
        }
        return unique;
    }

    public void update(Environment newEnvironment, int valueI, int valueJ) {
        environment = newEnvironment;
        i = valueI;
        j = valueJ;
    }

    public void start() {
        //--Comit giao dien neu co the
        if(iEvm == null){
            iEvm = new EmptyEvm();
        }
        //---
        System.out.println("Vị trí bắt đầu của Robot: " + i + ", " + j);
        count = environment.getCountDust(); // đếm số rác trong môi trường
        System.out.println("Số rác: " + environment.getCountDust());

        // thực hiện kiểm tra vị trí mà robot được thả xem có rác không
        if (environment.getMatrix()[i][j] == 1) {
            count--;
            suck();
            System.out.println("Số rác còn lại: " + count);
        }

        // quá trình robot quét xung quanh và đi đến chỗ có rác
        Point point = null;
        while (count != 0) {
            point = searchDust(); // tìm ra sai số giữa vị trí rác và vị trí robot đang đứng (đây là khoảng cách ngắn nhất giữa rác và robot
            go(point.x, point.y); // robot bắt đầu đi từ vị trí đứng đến vị trí rác
            count--;
            suck(); // robot thực hiện việc xúc rác và môi trường từ trạng thái rác (1) thành trạng thái sạch (0)
            System.out.println("Số rác còn lại: " + count);
        }
        commitDone();
    }

    // phương thức tìm khoảng cách từ robot đến các vị trí rác xung quanh nó
    private Point searchDust() {
        ArrayList<Point> list = new ArrayList<Point>(); // mảng chứa danh sách khoảng cách từ robot đến rác
        radius = 0; // bán kính quét của robot

        // thực hiện quét xung quanh: bán kính lớn dần từ 1, 2 ,3... đến khi thấy rác thì dừng lại
        while (true) {
            radius++;
            commitRadar();
            for (int d = 0; d <= radius; d++) {
                if (condition(i + d, j - radius) && environment.getMatrix()[i + d][j - radius] == 1) {
                    list.add(new Point(d, -radius));
                }
                if (condition(i - d, j + radius) && environment.getMatrix()[i - d][j + radius] == 1) {
                    list.add(new Point(-d, radius));
                }
                if (condition(i - radius, j - d) && environment.getMatrix()[i - radius][j - d] == 1) {
                    list.add(new Point(-radius, -d));
                }
                if (condition(i + radius, j + d) && environment.getMatrix()[i + radius][j + d] == 1) {
                    list.add(new Point(radius, d));
                }
                if (d != 0 || d != radius) {
                    if (condition(i - d, j - radius) && environment.getMatrix()[i - d][j - radius] == 1) {
                        list.add(new Point(-d, -radius));
                    }
                    if (condition(i + d, j + radius) && environment.getMatrix()[i + d][j + radius] == 1) {
                        list.add(new Point(d, radius));
                    }
                    if (condition(i - radius, j + d) && environment.getMatrix()[i - radius][j + d] == 1) {
                        list.add(new Point(-radius, d));
                    }
                    if (condition(i + radius, j - d) && environment.getMatrix()[i + radius][j - d] == 1) {
                        list.add(new Point(radius, -d));
                    }
                }
                // khi thấy rác robot sẽ trả về khoảng cách giữa robot đến rác
                if (!list.isEmpty()) {
                    Random random = new Random();
                    return list.get(random.nextInt(list.size()));
                }
            }
        }
    }

// phương thức cho robot đi từ vị trí đứng đến vị trí rác
    private void go(int row, int column) {
        if (row != 0 && column != 0) {
            goRow(row);
            goColumn(column);
            return;
        }

        if (row == 0) {
            goColumn(column);
            return;
        }

        if (column == 0) {
            goRow(row);
            return;
        }
    }

    // phương thức cho robot đi theo cột
    private void goColumn(int column) {// trục dy
        if (column > 0) {
            while (column-- != 0) {
                right();
            }
        } else {
            while (column++ != 0) {
                left();
            }
        }
    }

    // phương thức cho robot đi theo hàng
    private void goRow(int row) {// trục dx
        if (row > 0) {
            while (row-- != 0) {
                down();
            }
        } else {
            if (row < 0) {
                while (row++ != 0) {
                    up();
                }
            }
        }
    }

    // kiểm tra vị trí có tồn tại trong ma trận không
    private boolean condition(int tempX, int tempY){
        if(0 <= tempX && tempX < environment.getRow() &&
                0 <= tempY && tempY < environment.getColumn()){
            return true;
        }else{
            return false;
        }
    }

    private void up() {
        i = i - 1;
        System.out.println("Di chuyển lên đến: " + i + ", "+ j);
        commitMoveUp();
        return;
    }

    private void down() {
        i = i + 1;
        System.out.println("Di chuyển xuống đến: " + i + ", "+ j);
        commitMoveDown();
        return;
    }

    private void left() {
        j = j - 1;
        System.out.println("Di chuyển qua trái đến: " + i + ", "+ j);
        commitMoveLeft();
        return;
    }

    private void right() {
        j = j + 1;
        System.out.println("Di chuyển qua phải đến: " + i + ", "+ j);
        commitMoveRight();
        return;
    }

    private void suck() {
        environment.getMatrix()[i][j] = 0;
        System.out.println("Quét tại vị trí: " + i + ", " + j);
        System.out.println("Trạng thái ma trận:");
        environment.printMatrix();
        commitSuck();
        return;
    }

    public void registry(IEvm iEvm) {
        this.iEvm = iEvm;
    }

    public void commitMoveUp() {
        iEvm.updateMoveUp();
    }

    public void commitMoveDown() {
        iEvm.updateMoveDown();
    }

    public void commitMoveLeft() {
       iEvm.updateMoveLeft();
    }

    public void commitMoveRight() {
       iEvm.updateMoveRight();
    }

    public void commitDone() {
         iEvm.updateDone();
    }

    public void commitSuck() {
        iEvm.updateSuck();
    }

    public void commitRadar() {
        iEvm.updateRadar();
    }

    public boolean isReady(){
        if(environment == null)
            return false;
        return true;
    }

    
}
