package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MessageBox extends JDialog {
	public final static int EXCLAIM = 0, INFORM = 1;
	private static final long serialVersionUID = 1L;
	private final static String[] imageFilename = { "/images/exclaim.gif", "/images/information.gif" };

	public MessageBox(final JFrame parent, final String title, final String messageString,
			final int imageType) {
		super(parent, title, true);
		final ImagePanel imagePanel = new ImagePanel(imageFilename[imageType]);
		final JLabel messageLabel = new JLabel(messageString);
		final JPanel imageMessagePanel = new JPanel();
		imageMessagePanel.setLayout(new FlowLayout());
		imageMessagePanel.add(imagePanel);
		imageMessagePanel.add(messageLabel);
		final JButton okButton = new JButton("OK");
		okButton.setMnemonic('O');
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				setVisible(false);
				dispose();
			}
		});
		okButton.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent ke) {
				if ((KeyEvent.getKeyText(ke.getKeyCode()).equals("Enter"))
						|| (KeyEvent.getKeyText(ke.getKeyCode()).equals("Escape"))) {
					setVisible(false);
					dispose();
				}
			}

			@Override
			public void keyReleased(final KeyEvent ke) {
			}

			@Override
			public void keyTyped(final KeyEvent ke) {
			}
		});
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		setLayout(new GridLayout(2, 1));
		add(imageMessagePanel);
		add(buttonPanel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				e.getWindow().setVisible(false);
				e.getWindow().dispose();
			}
		});
		validate();
		pack();
		setResizable(false);
		setCenteredInParent(parent);
		play("audio/notification.wav");
		setVisible(true);
	}

	private void setCenteredInParent(final JFrame parent) {
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		final int screenWidth = d.getSize().width, screenHeight = d.getSize().height, parentWidth = parent
				.getSize().width, parentHeight = parent.getSize().height, w = this.getSize().width, h = this
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

	private static void play(final String filename) {
		URL url = null;
		url = MessageBox.class.getResource("/audio/notification.wav");
		if (url == null) {
			try {
				final File file = new File(filename);
				if (file.canRead())
					url = file.toURI().toURL();
			} catch (final IllegalArgumentException iae) {
				// URL is not absolute.
			} catch (final MalformedURLException murle) {
				// Not a URL.
			}
		}
		if (url == null) {
			System.err.println("Audio file " + filename + " not found.");
			return;
		}
		final AudioClip clip = Applet.newAudioClip(url);
		clip.play();
	}
}
