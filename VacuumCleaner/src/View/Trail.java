
package View;

import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public class Trail {
    public int x,y;
    public Image image;
    public ArrayList<Trail> list;
    public char type;
    public Trail(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
        switch(type){
            case 'u':
                image = new ImageIcon(getClass().getResource("/Image/trail_u.png")).getImage();
                break;
            case 'd':
                image = new ImageIcon(getClass().getResource("/Image/trail_d.png")).getImage();
                break;
            case 'l':
                image = new ImageIcon(getClass().getResource("/Image/trail_l.png")).getImage();
                break;
            case 'r':
                image = new ImageIcon(getClass().getResource("/Image/trail_r.png")).getImage();
                break;
        }
    }

}
