package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class CenterableDialog extends JDialog implements Centerable {
	private static final long serialVersionUID = 1L;

	public CenterableDialog(final JFrame parent, final String title, final boolean isModal) {
		super(parent, title, isModal);
	}

	@Override
	public void setCenter(final Container me, final JFrame parent) {
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		final int screenWidth = d.getSize().width, screenHeight = d.getSize().height - 27, parentWidth = parent
				.getSize().width, parentHeight = parent.getSize().height, w = me.getSize().width, h = me
				.getSize().height;
		final Point p = parent.getLocation(), setp = parent.getLocation();
		if (p.x < 0)
			setp.x = (parentWidth + p.x - w) / 2;
		else if (p.x + parentWidth > screenWidth)
			setp.x = p.x + (screenWidth - p.x - w) / 2;
		else
			setp.x = p.x + (parentWidth - w) / 2;
		if (p.y < 0)
			setp.y = (parentHeight + p.y - h) / 2;
		else if (p.y + parentHeight > screenHeight)
			setp.y = p.y + (screenHeight - p.y - h) / 2;
		else
			setp.y = p.y + (parentHeight - h) / 2;
		if (setp.x < 0)
			setp.x = 0;
		else if (setp.x + w > screenWidth)
			setp.x = screenWidth - w;
		if (setp.y < 0)
			setp.y = 0;
		else if (p.y + h > screenHeight)
			setp.y = screenHeight - h;
		setLocation(setp);
	}
}
