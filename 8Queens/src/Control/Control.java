package Control;

import HillClimbing.HillClimbing;
import View.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author VUWIT14 - Group 01
 */
public class Control implements ActionListener {

    private int size;
    private int state = 1;
    private Window window;
    private HillClimbing hillClimbing;

    public Control(Window window) {
        this.window = window;
        hillClimbing = HillClimbing.getInstance();
        hillClimbing.createChessboard(4);
        size = hillClimbing.getSizeMatrix();
        window.setEvmSize(size, size);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(window.getJcChooseEvmSize())) {
            String nString = (String) window.getJcChooseEvmSize().getSelectedItem();
            size = Integer.parseInt(nString);
            hillClimbing.setSizeMatrix(size);
            hillClimbing.createChessboard(size);
            if (hillClimbing.getListQueen() != null) {
                hillClimbing.getListQueen().removeAll(hillClimbing.getListQueen());
            }
            window.setEvmSize(size, size);
            state = 1;
        }
        if (e.getSource().equals(window.getJbIcon())) {
            hillClimbing.createStateQueen();
            hillClimbing.createStateChessboard();
            window.setEvmSize(size, size);
            window.setNumberPairOfQueen_Antagonistic(hillClimbing.getNumberPairOfQueen_Antagonistic());
            state = 3;
        }
        if (e.getSource().equals(window.getJbStart())) {
            if (state == 1) {
                JOptionPane.showConfirmDialog(window, "Bạn hãy tạo trạng thái các quân hậu!", "", -1);
                return;
            }

            if (state == 2) {
                JOptionPane.showConfirmDialog(window, "Bàn cờ đang ở trạng thái hoàn thành!\nBạn hãy tạo mới trạng thái các quân hậu!", "", -1);
                return;
            }

            new Thread() {

                @Override
                public void run() {
                    window.jcChooseEvmSize.setEnabled(false);
                    window.jbIcon.setEnabled(false);
                    window.jbStart.setEnabled(false);
                    hillClimbing.start();
                    window.jcChooseEvmSize.setEnabled(true);
                    window.jbIcon.setEnabled(true);
                    window.jbStart.setEnabled(true);
                    state = 2;
                }
            }.start();
        }
    }
}
