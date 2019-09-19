package gamecaro;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class MainView extends JFrame {

    private JButton[][] bt;
    private int[][] matran;
    private JPanel p;
    private CaroBoard caro;
    private JMenuBar menuBar;
    private JMenu firstPlayer;
    private JMenu newGame;
    private JMenu gameMode;
    private boolean firstPlayerAI = true;
    private boolean checkWin = false;
    private int maxdepth = 4;
    private int dem = 2;

    public MainView() {
        System.out.println("Max depth:" + maxdepth);
        menuBar = new JMenuBar();

        newGame = new JMenu("New Game");
        menuBar.add(newGame);

        firstPlayer = new JMenu("First Player");
        JMenuItem ai = new JMenuItem("AI");
        firstPlayer.add(ai);
        JMenuItem player = new JMenuItem("Human");
        firstPlayer.add(player);

        ai.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                firstPlayerAI = true;
            }
        });

        player.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                firstPlayerAI = false;
            }
        });

        menuBar.add(firstPlayer);

        gameMode = new JMenu("Game Mode");

        JMenuItem easy = new JMenuItem("Easy");
        gameMode.add(easy);
        JMenuItem medium = new JMenuItem("Medium");
        gameMode.add(medium);
        JMenuItem hard = new JMenuItem("Hard");
        gameMode.add(hard);

        easy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                maxdepth = 1;
            }
        });

        medium.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                maxdepth = 4;
            }
        });

        hard.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                maxdepth = 6;
            }
        });

        menuBar.add(gameMode);
        setJMenuBar(menuBar);
        caro = new CaroBoard(20);
        p = new JPanel();
        p.setBounds(0, 0, 640, 640);
        p.setLayout(new GridLayout(20, 20));
        add(p);
        matran = new int[20][20];
        bt = new JButton[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                bt[i][j] = new JButton();
                bt[i][j].setBackground(Color.WHITE);
                final int a = i, b = j;
                bt[a][b].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!checkWin && dem == 1 && matran[a][b] == 0) {
                            matran[a][b] = 1;
                            caro.set(a, b, 1);
                            bt[a][b].setIcon(getIcon("cross"));
                            dem = 2;
                            if (checkWin(matran, a, b, 1) == 1) {
                                JOptionPane.showMessageDialog(null, "You Win");
                                checkWin = true;
                            }
                        }
                        // AI
                        if (!checkWin && dem == 2) {
                            AlphaBetaPrunning ai = new AlphaBetaPrunning(20, maxdepth);
                            Point p = ai.search(caro);
                            matran[p.x][p.y] = 2;
                            caro.set(p.x, p.y, 2);
                            bt[p.x][p.y].setIcon(getIcon("nought"));
                            dem = 1;
                            if (checkWin(matran, p.x, p.y, 2) == 1) {
                                JOptionPane.showMessageDialog(null, "You Lose");
                                checkWin = true;
                            }
                        }
                    }
                });
                p.add(bt[a][b]);
            }
        }
        newGame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
                    for (int i = 0; i < 20; i++) {
                        for (int j = 0; j < 20; j++) {
                            bt[i][j].setIcon(null);
                            matran[i][j] = 0;
                            caro.set(i, j, 0);
                        }
                    }
                    if (firstPlayerAI) {
                        matran[10][10] = 2;
                        caro.set(10, 10, 2);
                        bt[10][10].setIcon(getIcon("nought"));
                        dem = 1;
                    } else {
                        dem = 1;
                    }
                    checkWin = false;
                    System.out.println("Maxdepth :" + maxdepth);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        if (firstPlayerAI) {
            matran[10][10] = 2;
            caro.set(10, 10, 2);
            bt[10][10].setIcon(getIcon("nought"));
            dem = 1;
        }
        setLayout(null);
        setTitle("Game Caro code by bangtri. Mail: tribang.soict.hust@gmail.com");
        setSize(647, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    private Icon getIcon(String name) {
        Image image = new ImageIcon(getClass().getResource(
                "/gamecaro/" + name + ".gif")).getImage();
        Icon icon = new ImageIcon(image);
        return icon;

    }

    public int checkWin(int[][] mt, int a, int b, int c) {
        if (checkCheoPhai(mt, a, b, c) == 1 || checkCheoTrai(mt, a, b, c) == 1 || checkCot(mt, a, b, c) == 1 || checkHang(mt, a, b, c) == 1) {
            return 1;
        }
        return 0;
    }

    public int checkCheoTrai(int[][] mt, int a, int b, int c) {
        int dem1 = 0, dem2 = 0;
        int x1 = a, x2 = b;
        int x3 = a, x4 = b;
        while (x1 - 1 >= 0 && x2 - 1 >= 0) {
            x1--;
            x2--;
            dem1++;
            if (dem1 == 4) {
                break;
            }
        }
        while (x3 + 1 <= 19 && x4 + 1 <= 19) {
            x3++;
            x4++;
            dem2++;
            if (dem2 == 4) {
                break;
            }
        }
        if (dem1 + dem2 >= 5) {
            for (int i = x3, j = x4; i >= x1; i--, j--) {
                if (i - 4 >= 0 && j - 4 >= 0) {
                    if (mt[i][j] == c && mt[i - 1][j - 1] == c && mt[i - 2][j - 2] == c && mt[i - 3][j - 3] == c && mt[i - 4][j - 4] == c) {
                        if (CheckPoint(i + 1, j + 1) && CheckPoint(i - 5, j - 5)) {
                            if (mt[i + 1][j + 1] != c && mt[i + 1][j + 1] != 0 && mt[i - 5][j - 5] != c && mt[i - 5][j - 5] != 0) {
                                continue;
                            }
                        }
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public int checkCheoPhai(int[][] mt, int a, int b, int c) {
        int dem1 = 0, dem2 = 0;
        int x1 = a, x2 = b;
        int x3 = a, x4 = b;
        while (x1 - 1 >= 0 && x2 + 1 <= 19) {
            x1--;
            x2++;
            dem1++;
            if (dem1 == 4) {
                break;
            }
        }
        while (x3 + 1 <= 19 && x4 - 1 >= 0) {
            x3++;
            x4--;
            dem2++;
            if (dem2 == 4) {
                break;
            }
        }
        if (x3 - x1 >= 4) {
            for (int i = x3, j = x4; i >= x1; i--, j++) {
                if (i - 4 >= 0 && j + 4 <= 19) {
                    if (mt[i][j] == c && mt[i - 1][j + 1] == c && mt[i - 2][j + 2] == c && mt[i - 3][j + 3] == c && mt[i - 4][j + 4] == c) {
                        if (CheckPoint(i + 1, j - 1) && CheckPoint(i - 5, j + 5)) {
                            if (mt[i + 1][j - 1] != c && mt[i + 1][j - 1] != 0 && mt[i - 5][j + 5] != c && mt[i - 5][j + 5] != 0) {
                                continue;
                            }
                        }
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public int checkCot(int[][] mt, int a, int b, int c) {
        int dem1 = 0, dem2 = 0;
        int x1 = a, x2 = a;
        while (x1 - 1 >= 0) {
            x1--;
            dem1++;
            if (dem1 == 4) {
                break;
            }
        }
        while (x2 + 1 <= 19) {
            x2++;
            dem2++;
            if (dem2 == 4) {
                break;
            }
        }
        if (x2 - x1 >= 4) {
            for (int i = x1; i <= x2; i++) {
                if (i + 4 <= 19) {
                    if (mt[i][b] == c && mt[i + 1][b] == c && mt[i + 2][b] == c && mt[i + 3][b] == c && mt[i + 4][b] == c) {
                        if (CheckPoint(i - 1, b) && CheckPoint(i + 5, b)) {
                            if (mt[i - 1][b] != c && mt[i - 1][b] != 0 && mt[i + 5][b] != c && mt[i + 5][b] != 0) {
                                continue;
                            }
                        }
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public int checkHang(int[][] mt, int a, int b, int c) {
        int dem1 = 0, dem2 = 0;
        int x1 = b, x2 = b;
        while (x1 - 1 >= 0) {
            x1--;
            dem1++;
            if (dem1 == 4) {
                break;
            }
        }
        while (x2 + 1 <= 19) {
            x2++;
            dem2++;
            if (dem2 == 4) {
                break;
            }
        }
        if (x2 - x1 >= 4) {
            for (int i = x1; i <= x2; i++) {
                if (i + 4 <= 19) {
                    if (mt[a][i] == c && mt[a][i + 1] == c && mt[a][i + 2] == c && mt[a][i + 3] == c && mt[a][i + 4] == c) {
                        if (CheckPoint(a, i - 1) && CheckPoint(a, i + 5)) {
                            if (mt[a][i - 1] != c && mt[a][i - 1] != 0 && mt[a][i + 5] != c && mt[a][i + 5] != 0) {
                                continue;
                            }
                        }
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public boolean CheckPoint(int x, int y) {
        return (x >= 0 && y >= 0 && x < 20 && y < 20);
    }

    public static void main(String[] args) {
        new MainView();
    }
}
