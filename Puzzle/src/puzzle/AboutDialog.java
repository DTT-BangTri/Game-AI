package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class that implements a modal dialog box to display info about the application.
 */
public class AboutDialog extends CenterableDialog implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	public AboutDialog(final JFrame parent, final String title, final String[] data,
			final String imageName) {
		super(parent, "Author...", true);
		setBackground(parent.getBackground());
		final Container contentPane = getContentPane();
		final ImagePanel icon = new ImagePanel(imageName);
		final JPanel stringPanel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		stringPanel.setLayout(new GridBagLayout());
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		for (int i = 0; i < data.length; ++i) {
			stringPanel.add(new JLabel(" " + data[i] + " "), gbc);
		}
		final JButton ok = new JButton("OK");
		ok.addActionListener(this);
		ok.addKeyListener(this);
		ok.setFocusPainted(false);
		ok.setDefaultCapable(true);
		ok.setMnemonic('O');
		final JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("sansserif", Font.BOLD, 16));
		final JPanel imageStringPanel = new JPanel();
		imageStringPanel.add(icon);
		imageStringPanel.add(titleLabel);
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(imageStringPanel);
		final JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new FlowLayout());
		middlePanel.add(stringPanel);
		middlePanel.setBorder(BorderFactory.createEtchedBorder());
		final JPanel bottomPanel = new JPanel();
		bottomPanel.add(ok);
		gbc = new GridBagConstraints();
		contentPane.setLayout(new GridBagLayout());
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		contentPane.add(topPanel, gbc);
		contentPane.add(middlePanel, gbc);
		contentPane.add(bottomPanel, gbc);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				e.getWindow().setVisible(false);
				e.getWindow().dispose();
			}
		});
		validate();
		pack();
		setCenter(this, parent);
		setResizable(false);
		setVisible(true);
	}

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

	@Override
	public void actionPerformed(final ActionEvent e) {
		setVisible(false);
		dispose();
	}
}
