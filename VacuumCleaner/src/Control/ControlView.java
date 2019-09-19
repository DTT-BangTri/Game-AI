package Control;

import Model.Environment;
import Model.Robot;
import View.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public class ControlView implements ActionListener {

    private Window window;
    private Environment environment;
    private Robot robot;

    public ControlView(Window window) {
        this.window = window;
        environment = Environment.getInstance();
        robot = Robot.getIntance();
        environment.createEnvironment(3, 3);
        window.setEvmSize(3, 3);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(window.getJcChooseEvmWidth())) {
            String selected = (String) window.getJcChooseEvmWidth().getSelectedItem();
            if (selected.equals(window.listWidth[0])) {
                environment.createEnvironment(3, 3);
                window.setEvmSize(3, 3);
            }
            if (selected.equals(window.listWidth[1])) {
                environment.createEnvironment(4, 4);
                window.setEvmSize(4, 4);
            }
            if (selected.equals(window.listWidth[2])) {
                environment.createEnvironment(5, 5);
                window.setEvmSize(5, 5);
            }
            if (selected.equals(window.listWidth[3])) {
                environment.createEnvironment(10, 10);
                window.setEvmSize(10, 10);
            }
        }
        if (e.getSource().equals(window.getJbStart())) {
            if (!robot.isReady()) {
                JOptionPane.showConfirmDialog(window, "Hãy đặt robot vào bản đồ", "", -1);
                return;
            }
            int countTemp = 0;
            for (int i = 0; i < environment.getRow(); i++) {
                for (int j = 0; j < environment.getColumn(); j++) {
                    if (environment.getMatrix()[i][j] == 1) {
                        countTemp++;
                    }
                }
            }
            if (countTemp == 0) {
                JOptionPane.showConfirmDialog(window, "Không thể bắt đầu", "", -1);
                return;
            }
            new Thread() {

                @Override
                public void run() {
                    window.jcChooseEvmWidth.setEnabled(false);
                    window.robotStand.setEnable(false);
                    window.jbStart.setEnabled(false);
                    robot.start();
                    window.jcChooseEvmWidth.setEnabled(true);
                    window.robotStand.setEnable(true);
                    window.jbStart.setEnabled(true);
                }
            }.start();
        }
    }
}
