package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
/**
 * Class that allows the application to run from an applet.
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;

public class ApplicationStarter extends JApplet implements ActionListener {
	private static final long serialVersionUID = 1L;
	private GUI gui = null;

	@Override
	public void init() {
		final JButton startButton = new JButton("Start " + GUI.APP_NAME);
		startButton.setMnemonic('S');
		final Container contentPane = getContentPane();
		contentPane.setBackground(getBackgroundColor(getParameter("background")));
		contentPane.setLayout(new FlowLayout());
		contentPane.add(startButton);
		startButton.addActionListener(this);
	}

	public void close() {
		this.gui.stop();
		this.gui.setVisible(false);
		this.gui.dispose();
		this.gui = null;
	}

	@Override
	public void actionPerformed(final ActionEvent ae) {
		if (this.gui == null) {
			this.gui = new GUI(this);
		} else {
			this.gui.toFront();
		}
	}

	private Color getBackgroundColor(final String background) {
		if ((background == null) || (background.length() != 7) || (background.charAt(0) != '#')) {
			return Color.white;
		} else {
			String hexcolor, red, green, blue;
			hexcolor = background.substring(1, background.length());
			red = hexcolor.substring(0, 2);
			green = hexcolor.substring(2, 4);
			blue = hexcolor.substring(4, 6);
			return new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16),
					Integer.parseInt(blue, 16));
		}
	}
}
