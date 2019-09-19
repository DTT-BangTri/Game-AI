
package Commit;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public interface IRobot {
    public void registry(IEvm iEvm);
    public void commitMoveUp();
    public void commitMoveDown();
    public void commitMoveLeft();
    public void commitMoveRight();
    public void commitDone();
    public void commitSuck();
    public void commitRadar();
}
