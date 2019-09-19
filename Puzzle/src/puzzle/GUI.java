package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.BitSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class GUI extends JFrame implements Runnable {
	public static final String APP_NAME = "Bài toán ghép tranh 8/15-Puzzle";
	private static final long serialVersionUID = 1L;
	private final ApplicationStarter applicationStarter;
	private final DefaultListModel model;
	private final JButton shuffleButton, solveButton, showButton;
	private final JLabel statusLabel;
	private final JList directionsList;
	private final JTextField configurationField, expandedField, initialMovesEstimateField,
			initialOrderField, movesField, pathsField, statusField, timeField;
	private final Puzzle puzzle;
	private final Timer statusUpdateTimer;
	private JMenu optionsMenu;
	private JRadioButtonMenuItem aStarItem, idaStarItem, singleThreadedItem, multiThreadedItem;
	private GraphicsThread graphicsThread;
	private int puzzleType, algorithmType, heuristicType, stepNumber;
	private boolean shouldRun, keepZeroInCorner, useThreads = true;
	private byte[] initState, graphicsState;

	public GUI(final ApplicationStarter appStarter) {
		super(APP_NAME);
		this.applicationStarter = appStarter;
		this.puzzleType = PuzzleConfiguration.PUZZLE_15;
		this.algorithmType = PuzzleConfiguration.ALGORITHM_IDASTAR;
		this.heuristicType = PuzzleConfiguration.HEURISTIC_PD;
		PuzzleConfiguration.initialize(this.puzzleType, this.algorithmType, this.heuristicType,
				getNumberOfThreads());
		this.statusUpdateTimer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Algorithm algorithm = PuzzleConfiguration.getAlgorithm();
				if (Algorithm.running || Algorithm.solved) {
					GUI.this.initialMovesEstimateField
							.setText(Algorithm.initialMovesEstimate == Algorithm.NOT_APPLICABLE ? "N/A"
									: Algorithm.initialMovesEstimate == 1 ? Integer
											.toString(Algorithm.initialMovesEstimate) + " bước"
											: Integer.toString(Algorithm.initialMovesEstimate)
													+ " bước");
					GUI.this.movesField.setText(Long.toString(Algorithm.movesRequired));
					GUI.this.pathsField.setText(Utility.INT_FORMATTER
							.format(Algorithm.numberVisited));
					GUI.this.expandedField.setText(Utility.INT_FORMATTER
							.format(Algorithm.numberExpanded));
				}
				if (Algorithm.running) {
					GUI.this.timeField.setText(Utility.DEC_FORMATTER.format(Algorithm
							.getElapsedTimeInSeconds()) + " s");
				} else {
					if (Algorithm.solved) {
						GUI.this.statusField.setText("Tìm được lời giải");
						GUI.this.timeField.setText(Utility.DEC_FORMATTER.format(Algorithm
								.getRunningTimeInSeconds()) + " s");
						final String[] directions = Utility.getDirections(GUI.this.initState);
						for (int i = 0; i < directions.length; ++i) {
							GUI.this.model.add(i, directions[i]);
						}
						GUI.this.showButton.setEnabled(true);
					} else {
						GUI.this.statusField.setText("Hủy bỏ");
					}
					GUI.this.configurationField.setEnabled(true);
					GUI.this.shuffleButton.setEnabled(true);
					GUI.this.optionsMenu.setEnabled(true);
					GUI.this.solveButton.setText("Giải");
					GUI.this.statusUpdateTimer.stop();
					// Defer cleaning up until GUI has updated, otherwise a visible lag
					// is possible.
					algorithm.cleanup();
				}
			}
		});
		this.statusUpdateTimer.setInitialDelay(0);
		setJMenuBar(getCreatedMenuBar());
		this.configurationField = new JTextField(27);
		this.configurationField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent ke) {
				if (KeyEvent.getKeyText(ke.getKeyCode()).equals("Enter")) {
					GUI.this.solveButton.requestFocus();
					solvePuzzle();
				}
			}

			@Override
			public void keyReleased(final KeyEvent ke) {
			}

			@Override
			public void keyTyped(final KeyEvent ke) {
			}
		});
		final JLabel configurationLabel = new JLabel("Thứ tự các ô:", SwingConstants.RIGHT);
		configurationLabel.setDisplayedMnemonic('T');
		configurationLabel.setLabelFor(this.configurationField);
		this.solveButton = new JButton("Giải");
		this.solveButton.setMnemonic('S');
		this.solveButton.setPreferredSize(new Dimension(88, 26));
		this.solveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (GUI.this.solveButton.getText().equals("Giải")) {
					solvePuzzle();
				} else {
					stop();
				}
			}
		});
		this.shuffleButton = new JButton("Tráo ô");
		this.shuffleButton.setMnemonic('u');
		this.shuffleButton.setPreferredSize(new Dimension(88, 26));
		this.shuffleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.showButton.setEnabled(false);
				final byte[] state = Utility.getRandomArray(
						GUI.this.puzzleType == PuzzleConfiguration.PUZZLE_15 ? 16 : 9,
						GUI.this.keepZeroInCorner);
				GUI.this.configurationField.setText(Utility.byteArrayToString(state));
				GUI.this.puzzle.setOrder(state);
			}
		});
		final JPanel topPanel = new JPanel();
		topPanel.add(configurationLabel);
		topPanel.add(this.configurationField);
		topPanel.add(this.shuffleButton);
		topPanel.add(this.solveButton);
		topPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Nhập vào"));
		this.showButton = new JButton("Chỉ ra cách đi");
		this.showButton.setMnemonic('M');
		this.showButton.setPreferredSize(new Dimension(125, 26));
		this.showButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.graphicsState = new byte[GUI.this.initState.length];
				System.arraycopy(GUI.this.initState, 0, GUI.this.graphicsState, 0,
						GUI.this.initState.length);
				GUI.this.stepNumber = 0;
				GUI.this.graphicsThread = new GraphicsThread();
				GUI.this.graphicsThread.start();
			}
		});
		this.showButton.setEnabled(false);
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(this.showButton);
		this.puzzle = new Puzzle(PuzzleConfiguration.getNumOfTiles());
		final JPanel puzzleBorderPanel = new JPanel();
		puzzleBorderPanel.setLayout(new FlowLayout());
		puzzleBorderPanel.add(this.puzzle);
		final GridBagConstraints gbc = new GridBagConstraints();
		final JPanel puzzlePanel = new JPanel();
		puzzlePanel.setLayout(new GridBagLayout());
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 5, 5, 5);
		puzzlePanel.add(puzzleBorderPanel, gbc);
		puzzlePanel.add(buttonPanel, gbc);
		puzzlePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Bức tranh"));
		this.statusField = new JTextField(10);
		this.statusField.setEditable(false);
		this.statusField.setBackground(Color.white);
		this.initialMovesEstimateField = new JTextField(10);
		this.initialMovesEstimateField.setEditable(false);
		this.initialMovesEstimateField.setBackground(Color.white);
		this.initialOrderField = new JTextField(10);
		this.initialOrderField.setEditable(false);
		this.initialOrderField.setBackground(Color.white);
		this.timeField = new JTextField(10);
		this.timeField.setEditable(false);
		this.timeField.setBackground(Color.white);
		this.movesField = new JTextField(10);
		this.movesField.setEditable(false);
		this.movesField.setBackground(Color.white);
		this.pathsField = new JTextField(10);
		this.pathsField.setEditable(false);
		this.pathsField.setBackground(Color.white);
		this.expandedField = new JTextField(10);
		this.expandedField.setEditable(false);
		this.expandedField.setBackground(Color.white);
		this.model = new DefaultListModel();
		this.directionsList = new JList(this.model);
		this.directionsList.setDoubleBuffered(true);
		this.directionsList.setPrototypeCellValue("10 - right");
		final JScrollPane scrollPane = new JScrollPane(this.directionsList);
		scrollPane.setPreferredSize(new Dimension(210,
				this.directionsList.getFixedCellHeight() * 6 + 3));
		final JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		gbc.insets = new Insets(5, 0, 1, 10);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		infoPanel.add(new JLabel("Trạng thái:", SwingConstants.RIGHT), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(this.statusField, gbc);
		gbc.insets = new Insets(1, 0, 1, 10);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		infoPanel.add(new JLabel("Thời gian tính:", SwingConstants.RIGHT), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(this.timeField, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		infoPanel.add(new JLabel("Trạng thái đầu:", SwingConstants.RIGHT), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(this.initialOrderField, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		infoPanel.add(new JLabel("Ước tính số bước đi:", SwingConstants.RIGHT), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(this.initialMovesEstimateField, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		infoPanel.add(new JLabel("Số lượng đường đi:", SwingConstants.RIGHT), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(this.pathsField, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		infoPanel.add(new JLabel("Số trạng thái đã duyệt:", SwingConstants.RIGHT), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(this.expandedField, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		infoPanel.add(new JLabel("Số bước cần đi:", SwingConstants.RIGHT), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(this.movesField, gbc);
		gbc.insets = new Insets(1, 0, 10, 10);
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		infoPanel.add(new JLabel("Thứ tự đi:", SwingConstants.RIGHT), gbc);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(scrollPane, gbc);
		infoPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Solution"));
		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		centerPanel.add(puzzlePanel, gbc);
		gbc.gridx = 1;
		centerPanel.add(infoPanel, gbc);
		this.statusLabel = new JLabel(PuzzleConfiguration.stringRepresentation(),
				SwingConstants.RIGHT);
		final JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new GridBagLayout());
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		gbc.insets = new Insets(0, 0, 1, 3);
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		statusPanel.add(this.statusLabel, gbc);
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		contentPane.add(statusPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new ClosingWindowListener(this));
		validate();
		pack();
		setResizable(false);
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		final Point p = new Point((int) ((d.getWidth() - this.getWidth()) / 2),
				(int) ((d.getHeight() - this.getHeight()) / 2));
		setLocation(p);
		setVisible(true);
	}

	public void stop() {
		PuzzleConfiguration.getAlgorithm().stop();
		cancelGraphicsThread();
	}

	@Override
	public void run() {
		PuzzleConfiguration.getAlgorithm().solve(Utility.arrayToLong(this.initState),
				PuzzleConfiguration.getNumOfThreads(), new BitSet());
	}

	private void cancelGraphicsThread() {
		this.shouldRun = false;
		this.puzzle.stopAnimation();
		if (this.graphicsThread != null) {
			try {
				this.graphicsThread.join();
			} catch (final InterruptedException ie) {
			}
		}
		this.directionsList.clearSelection();
	}

	private JMenuBar getCreatedMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
		final JMenu fileMenu = new JMenu("Exit");
		fileMenu.setMnemonic('F');
		KeyStroke ks;
		final JMenuItem exitItem = new JMenuItem("Thoát");
		ks = KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK);
		exitItem.setAccelerator(ks);
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ExitActionListener(this));
		fileMenu.add(exitItem);
		this.optionsMenu = new JMenu("Lựa chọn");
		this.optionsMenu.setMnemonic('O');
		final JMenu puzzleTypeMenu = new JMenu("Kiểu puzzle");
		puzzleTypeMenu.setMnemonic('P');
		final ButtonGroup puzzleTypeGroup = new ButtonGroup();
		final JRadioButtonMenuItem eightPuzzleItem = new JRadioButtonMenuItem("8-Puzzle",
				this.puzzleType == PuzzleConfiguration.PUZZLE_8);
		eightPuzzleItem.setMnemonic('8');
		eightPuzzleItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.showButton.setEnabled(false);
				GUI.this.puzzleType = PuzzleConfiguration.PUZZLE_8;
				GUI.this.aStarItem.setEnabled(true);
				GUI.this.singleThreadedItem.setSelected(true);
				GUI.this.useThreads = false;
				GUI.this.multiThreadedItem.setEnabled(false);
				PuzzleConfiguration.setNumOfThreads(getNumberOfThreads());
				updatePuzzleConfiguration();
				GUI.this.puzzle
						.setNumOfTiles(GUI.this.puzzleType == PuzzleConfiguration.PUZZLE_15 ? 16
								: 9);
			}
		});
		puzzleTypeGroup.add(eightPuzzleItem);
		final JRadioButtonMenuItem fifteenPuzzleItem = new JRadioButtonMenuItem("15-Puzzle",
				this.puzzleType == PuzzleConfiguration.PUZZLE_15);
		fifteenPuzzleItem.setMnemonic('1');
		fifteenPuzzleItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.showButton.setEnabled(false);
				GUI.this.puzzleType = PuzzleConfiguration.PUZZLE_15;
				if (GUI.this.aStarItem.isSelected()) {
					GUI.this.idaStarItem.setSelected(true);
					GUI.this.algorithmType = PuzzleConfiguration.ALGORITHM_IDASTAR;
				}
				GUI.this.aStarItem.setEnabled(false);
				if (canRunThreads()) {
					GUI.this.multiThreadedItem.setEnabled(true);
				}
				PuzzleConfiguration.setNumOfThreads(getNumberOfThreads());
				updatePuzzleConfiguration();
				GUI.this.puzzle
						.setNumOfTiles(GUI.this.puzzleType == PuzzleConfiguration.PUZZLE_15 ? 16
								: 9);
			}
		});
		puzzleTypeGroup.add(fifteenPuzzleItem);
		puzzleTypeMenu.add(eightPuzzleItem);
		puzzleTypeMenu.add(fifteenPuzzleItem);
		final JMenu algorithmTypeMenu = new JMenu("Thuật toán");
		algorithmTypeMenu.setMnemonic('A');
		final ButtonGroup algorithmTypeGroup = new ButtonGroup();
		this.idaStarItem = new JRadioButtonMenuItem("IDA*",
				this.algorithmType == PuzzleConfiguration.ALGORITHM_IDASTAR);
		this.idaStarItem.setMnemonic('I');
		this.idaStarItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.showButton.setEnabled(false);
				GUI.this.algorithmType = PuzzleConfiguration.ALGORITHM_IDASTAR;
				updatePuzzleConfiguration();
			}
		});
		algorithmTypeGroup.add(this.idaStarItem);
		this.aStarItem = new JRadioButtonMenuItem("A*",
				this.algorithmType == PuzzleConfiguration.ALGORITHM_ASTAR);
		this.aStarItem.setMnemonic('A');
		this.aStarItem.setEnabled(false);
		this.aStarItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.showButton.setEnabled(false);
				GUI.this.algorithmType = PuzzleConfiguration.ALGORITHM_ASTAR;
				updatePuzzleConfiguration();
			}
		});
		algorithmTypeGroup.add(this.aStarItem);
		algorithmTypeMenu.add(this.idaStarItem);
		algorithmTypeMenu.add(this.aStarItem);
		final JMenu heuristicTypeMenu = new JMenu("Hàm heuristic");
		heuristicTypeMenu.setMnemonic('H');
		final ButtonGroup heuristicTypeGroup = new ButtonGroup();
		final JRadioButtonMenuItem patternDatabaseItem = new JRadioButtonMenuItem(
				"Mô hình cơ sở dữ liệu", this.heuristicType == PuzzleConfiguration.HEURISTIC_PD);
		patternDatabaseItem.setMnemonic('P');
		patternDatabaseItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.showButton.setEnabled(false);
				GUI.this.heuristicType = PuzzleConfiguration.HEURISTIC_PD;
				updatePuzzleConfiguration();
			}
		});
		heuristicTypeGroup.add(patternDatabaseItem);
		final JRadioButtonMenuItem linearConflictItem = new JRadioButtonMenuItem(
				"Khoảng cách Manhattan + Xung đột tuyến tính",
				this.heuristicType == PuzzleConfiguration.HEURISTIC_LC);
		linearConflictItem.setMnemonic('L');
		linearConflictItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.showButton.setEnabled(false);
				GUI.this.heuristicType = PuzzleConfiguration.HEURISTIC_LC;
				updatePuzzleConfiguration();
			}
		});
		heuristicTypeGroup.add(linearConflictItem);
		final JRadioButtonMenuItem manhattanDistanceItem = new JRadioButtonMenuItem(
				"Khoảng cách Manhattan", this.heuristicType == PuzzleConfiguration.HEURISTIC_MD);
		manhattanDistanceItem.setMnemonic('M');
		manhattanDistanceItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelGraphicsThread();
				GUI.this.showButton.setEnabled(false);
				GUI.this.heuristicType = PuzzleConfiguration.HEURISTIC_MD;
				updatePuzzleConfiguration();
			}
		});
		heuristicTypeGroup.add(manhattanDistanceItem);
		heuristicTypeMenu.add(patternDatabaseItem);
		heuristicTypeMenu.add(linearConflictItem);
		heuristicTypeMenu.add(manhattanDistanceItem);
		final JMenu threadingTypeMenu = new JMenu("Kiểu chuỗi lệnh");
		threadingTypeMenu.setMnemonic('T');
		final ButtonGroup threadingTypeGroup = new ButtonGroup();
		this.singleThreadedItem = new JRadioButtonMenuItem("Đơn chuỗi lệnh", false);
		this.singleThreadedItem.setMnemonic('S');
		this.singleThreadedItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GUI.this.useThreads = false;
				PuzzleConfiguration.setNumOfThreads(getNumberOfThreads());
				GUI.this.statusLabel.setText(PuzzleConfiguration.stringRepresentation());
			}
		});
		this.multiThreadedItem = new JRadioButtonMenuItem("Đa chuỗi lệnh", true);
		this.multiThreadedItem.setMnemonic('M');
		this.multiThreadedItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GUI.this.useThreads = true;
				PuzzleConfiguration.setNumOfThreads(getNumberOfThreads());
				GUI.this.statusLabel.setText(PuzzleConfiguration.stringRepresentation());
			}
		});
		if (!canRunThreads()) {
			this.multiThreadedItem.setEnabled(false);
			this.singleThreadedItem.setSelected(true);
		}
		threadingTypeGroup.add(this.singleThreadedItem);
		threadingTypeGroup.add(this.multiThreadedItem);
		threadingTypeMenu.add(this.singleThreadedItem);
		threadingTypeMenu.add(this.multiThreadedItem);
		final JMenu shuffleTypeMenu = new JMenu("Kiểu tráo ô");
		shuffleTypeMenu.setMnemonic('S');
		final ButtonGroup shuffleTypeGroup = new ButtonGroup();
		final JRadioButtonMenuItem shuffleAllItem = new JRadioButtonMenuItem("Ngẫu nhiên", true);
		shuffleAllItem.setMnemonic('A');
		shuffleAllItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GUI.this.keepZeroInCorner = false;
			}
		});
		final JRadioButtonMenuItem shuffleExcludeSpaceItem = new JRadioButtonMenuItem(
				"Giữ ô trong ở dưới cùng bên phải", false);
		shuffleExcludeSpaceItem.setMnemonic('K');
		shuffleExcludeSpaceItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GUI.this.keepZeroInCorner = true;
			}
		});
		shuffleTypeGroup.add(shuffleAllItem);
		shuffleTypeGroup.add(shuffleExcludeSpaceItem);
		shuffleTypeMenu.add(shuffleAllItem);
		shuffleTypeMenu.add(shuffleExcludeSpaceItem);
		this.optionsMenu.add(puzzleTypeMenu);
		this.optionsMenu.add(algorithmTypeMenu);
		this.optionsMenu.add(heuristicTypeMenu);
		this.optionsMenu.add(threadingTypeMenu);
		this.optionsMenu.add(new JSeparator());
		this.optionsMenu.add(shuffleTypeMenu);
		final JMenuItem aboutItem = new JMenuItem("Author");
		aboutItem.setMnemonic('A');
		aboutItem.addActionListener(new AboutActionListener(this));
		final JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		helpMenu.add(aboutItem);
		menuBar.add(fileMenu);
		menuBar.add(this.optionsMenu);
		menuBar.add(helpMenu);
		return menuBar;
	}

	private void updatePuzzleConfiguration() {
		PuzzleConfiguration.initialize(this.puzzleType, this.algorithmType, this.heuristicType,
				getNumberOfThreads());
		this.statusLabel.setText(PuzzleConfiguration.stringRepresentation());
	}

	private void highlightInput() {
		this.configurationField.requestFocus();
		this.configurationField.selectAll();
	}

	private void solvePuzzle() {
		try {
			cancelGraphicsThread();
			this.showButton.setEnabled(false);
			final int numOfTiles = this.puzzleType == PuzzleConfiguration.PUZZLE_15 ? 16 : 9;
			this.initState = Utility.getArray(this.configurationField.getText(), numOfTiles);
			final String initialOrder = Utility.byteArrayToString(this.initState);
			this.configurationField.setText(initialOrder);
			this.puzzle.setOrder(this.initState);
			if (!Utility.isValidPermutation(this.initState)) {
				clearFields();
				new MessageBox(this, "Thông tin", "Không giải được puzzle.",
						MessageBox.INFORM);
				highlightInput();
				return;
			}
			this.configurationField.setEnabled(false);
			this.shuffleButton.setEnabled(false);
			this.optionsMenu.setEnabled(false);
			clearFields();
			this.solveButton.setText("Stop");
			this.statusField.setText("Đang tìm lời giải...");
			this.initialOrderField.setText(initialOrder);
			this.initialOrderField.setCaretPosition(0);
			PuzzleConfiguration.getAlgorithm().start();
			final Thread t = new Thread(this);
			t.setPriority(Thread.MAX_PRIORITY);
			t.start();
			this.statusUpdateTimer.restart();
		} catch (final IllegalArgumentException iae) {
			new MessageBox(this, "Lỗi", iae.getMessage(), MessageBox.EXCLAIM);
			highlightInput();
		}
	}

	private void clearFields() {
		this.statusField.setText("");
		this.initialOrderField.setText("");
		this.initialMovesEstimateField.setText("");
		this.timeField.setText("");
		this.movesField.setText("");
		this.pathsField.setText("");
		this.expandedField.setText("");
		this.model.clear();
	}

	private void doApplicationClosing(final JFrame parent) {
		if (this.applicationStarter != null) {
			this.applicationStarter.close();
		} else {
			System.exit(0);
		}
	}

	private boolean canRunThreads() {
		if (this.applicationStarter != null && Utility.getHasFullPermission()
				|| this.applicationStarter == null) {
			return true;
		}
		return false;
	}

	private int getNumberOfThreads() {
		if (this.algorithmType == PuzzleConfiguration.ALGORITHM_IDASTAR) {
			if (this.puzzleType == PuzzleConfiguration.PUZZLE_8) {
				return 1;
			} else {
				if (canRunThreads() && this.useThreads) {
					return Utility.getDefaultNumOfThreads();
				} else {
					return 1;
				}
			}
		}
		return 1;
	}

	class GraphicsThread extends Thread {
		@Override
		public void run() {
			GUI.this.puzzle.setOrder(GUI.this.initState);
			GUI.this.shouldRun = true;
			while (GUI.this.shouldRun && GUI.this.stepNumber < Algorithm.shortestPath.length()) {
				Utility.generateNextState(Algorithm.shortestPath.charAt(GUI.this.stepNumber),
						GUI.this.graphicsState);
				GUI.this.directionsList.setSelectedIndex(GUI.this.stepNumber);
				GUI.this.directionsList.ensureIndexIsVisible(GUI.this.stepNumber);
				GUI.this.directionsList.repaint();
				GUI.this.puzzle.animatePuzzleTo(GUI.this.graphicsState);
				++GUI.this.stepNumber;
			}
			GUI.this.directionsList.clearSelection();
		}
	}

	class ExitActionListener implements ActionListener {
		private final JFrame parent;

		public ExitActionListener(final JFrame parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			doApplicationClosing(this.parent);
		}
	}

	class AboutActionListener implements ActionListener {
		private final JFrame parent;

		public AboutActionListener(final JFrame parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final String[] data = { "\u00a9 Bài toán ghép tranh 8/15-puzzle",
                                         "Nguyễn Tri Bằng - VUWIT14 - HUST" };
			new AboutDialog(this.parent, APP_NAME, data, "/images/information.gif");
		}
	}

	class ClosingWindowListener implements WindowListener {
		private final JFrame parent;

		public ClosingWindowListener(final JFrame parent) {
			this.parent = parent;
		}

		@Override
		public void windowClosing(final WindowEvent e) {
			doApplicationClosing(this.parent);
		}

		@Override
		public void windowDeactivated(final WindowEvent e) {
		}

		public void windowActivated(final WindowEvent e) {
		}

		public void windowDeiconified(final WindowEvent e) {
		}

		public void windowIconified(final WindowEvent e) {
		}

		public void windowClosed(final WindowEvent e) {
		}

		public void windowOpened(final WindowEvent e) {
		}
	}
}
