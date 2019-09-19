package Model;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public class Main {

    public static void main(String[] args) {
        Environment environment = Environment.getInstance();
        environment.createEnvironment(20, 20);
        environment.printMatrix();
        Robot robot = Robot.getIntance();

        robot.update(environment, 8, 12);
        robot.start();
        System.out.println("Matrix final!");
        environment.printMatrix();
    }

}
