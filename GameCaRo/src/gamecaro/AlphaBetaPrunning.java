package gamecaro;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;


public class AlphaBetaPrunning {

    /**
     * X = 1: Human, O = 2: AI
     */
    private String[] caseHuman = {"0110", "01112", "0110102", "21110", "010110",
        "011010", "01110", "011112", "211110", "2111010", "011110", "11111",
        "0111012", "10101011", "0101110", "0111010", "0111102", "110110",
        "011011", "0101"
            + "112", "11110",";11110","01111;"};
    private String[] caseAI = {"0220", "02221", "0220201", "12220", "020220",
        "022020", "02220", "022221", "122220", "1222020", "022220", "22222",
        "0222021", "20202022", "0202220", "0222020", "0222201", "220220",
        "022022", "0202221", "22220",";22220","02222;"};
    private int[] point = {6, 4, 4, 4, 12, 12, 12, 1000, 1000, 1000, 3000, 10000,
        1000, 3000, 1000, 1000, 1000, 1000, 1000,1000, 1000,1000,1000};
    int n;                               //12
    Random rand;
    int[] defenseScore = {0, 1, 9, 85, 769};
//  int[] attackScore = {0, 1, 9, 85, 769};
    int[] attackScore = {0, 4, 28, 256, 2308};
    int[][] evaluateSquare;
    private int maxDepth;
    int ai = 2;
    int human = 1;
    int maxSquare;

    /**
     *
     * @param size is size of board
     * @param maxDepth is the search depth
     */
    public AlphaBetaPrunning(int size, int maxDepth) {
        n = size;
        rand = new Random();
        evaluateSquare = new int[n][n];
        maxSquare = 4;
        this.maxDepth = maxDepth;
    }

    /**
     * Set all value of square = 0
     */
    void resetValue() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                evaluateSquare[i][j] = 0;
            }
        }
    }

    /**
     * This method to evaluate each square on board
     *
     * @param b is Board
     * @param Player is current player(Human:1, AI:2)
     *
     */
    public void evaluateEachSquare(CaroBoard b, int Player) {
        n = b.getSize();
        resetValue();
        int row, colum, i;
        int countAI, countHuman;
        /**
         * Check in rows
         */
        for (row = 0; row < n; row++) {
            for (colum = 0; colum < n - 4; colum++) {
                countAI = 0;
                countHuman = 0;
                for (i = 0; i < 5; i++) {
                    if (b.getSquare()[row][colum + i] == 2) {
                        countAI++;
                    }
                    if (b.getSquare()[row][colum + i] == 1) {
                        countHuman++;
                    }
                }
                if (countAI * countHuman == 0 && countAI != countHuman) {
                    for (i = 0; i < 5; i++) {
                        if (b.getSquare()[row][colum + i] == 0) {
                            if (countAI == 0) {
                                if (Player == 2) {
                                    evaluateSquare[row][colum + i] += defenseScore[countHuman];
                                } else {
                                    evaluateSquare[row][colum + i] += attackScore[countHuman];
                                }
                                if (CheckPoint(row, colum - 1) && CheckPoint(row, colum + 5) && b.getSquare()[row][colum - 1] == 2 && b.getSquare()[row][colum + 5] == 2) {
                                    evaluateSquare[row][colum + i] = 0;
                                }
                            }
                            if (countHuman == 0) {
                                if (Player == 1) {
                                    evaluateSquare[row][colum + i] += defenseScore[countAI];
                                } else {
                                    evaluateSquare[row][colum + i] += attackScore[countAI];
                                }
                                if (CheckPoint(row, colum - 1) && CheckPoint(row, colum + 5) && b.getSquare()[row][colum - 1] == 1 && b.getSquare()[row][colum + 5] == 1) {
                                    evaluateSquare[row][colum + i] = 0;
                                }
                            }
                            if ((countAI == 4 || countHuman == 4) && ((CheckPoint(row, colum + i - 1) && b.getSquare()[row][colum + i - 1] == 0) || (CheckPoint(row, colum + i + 1) && b.getSquare()[row][colum + i + 1] == 0))) {
                                evaluateSquare[row][colum + i] *= 2;
                            } else if (countAI == 4 || countHuman == 4) {
                                evaluateSquare[row][colum + i] *= 2;
                            }
                        }
                    }
                }
            }
        }
        /**
         * check in colums
         */
        for (row = 0; row < n - 4; row++) {
            for (colum = 0; colum < n; colum++) {
                countAI = 0;
                countHuman = 0;
                for (i = 0; i < 5; i++) {
                    if (b.getSquare()[row + i][colum] == 2) {
                        countAI++;
                    }
                    if (b.getSquare()[row + i][colum] == 1) {
                        countHuman++;
                    }
                }
                if (countAI * countHuman == 0 && countAI != countHuman) {
                    for (i = 0; i < 5; i++) {
                        if (b.getSquare()[row + i][colum] == 0) {
                            if (countAI == 0) {
                                if (Player == 2) {
                                    evaluateSquare[row + i][colum] += defenseScore[countHuman];
                                } else {
                                    evaluateSquare[row + i][colum] += attackScore[countHuman];
                                }
                                if (CheckPoint(row - 1, colum) && CheckPoint(row + 5, colum) && b.getSquare()[row - 1][colum] == 2 && b.getSquare()[row + 5][colum] == 2) {
                                    evaluateSquare[row + i][colum] = 0;
                                }
                            }
                            if (countHuman == 0) {
                                if (Player == 1) {
                                    evaluateSquare[row + i][colum] += defenseScore[countAI];
                                } else {
                                    evaluateSquare[row + i][colum] += attackScore[countAI];
                                }
                                if (CheckPoint(row - 1, colum) && CheckPoint(row + 5, colum) && b.getSquare()[row - 1][colum] == 1 && b.getSquare()[row + 5][colum] == 1) {
                                    evaluateSquare[row + i][colum] = 0;
                                }
                            }
                            if ((countAI == 4 || countHuman == 4) && ((CheckPoint(row + i - 1, colum) && b.getSquare()[row + i - 1][colum] == 0) || (CheckPoint(row + i + 1, colum) && b.getSquare()[row + i + 1][colum] == 0))) {
                                evaluateSquare[row + i][colum] *= 2;
                            } else if (countAI == 4 || countHuman == 4) {
                                evaluateSquare[row + i][colum] *= 2;
                            }
                        }
                    }
                }
            }
        }
        /**
         * Check in diagonal line
         */
        for (row = 0; row < n - 4; row++) {
            for (colum = 0; colum < n - 4; colum++) {
                countAI = 0;
                countHuman = 0;
                for (i = 0; i < 5; i++) {
                    if (b.getSquare()[row + i][colum + i] == 2) {
                        countAI++;
                    }
                    if (b.getSquare()[row + i][colum + i] == 1) {
                        countHuman++;
                    }
                }
                if (countAI * countHuman == 0 && countAI != countHuman) {
                    for (i = 0; i < 5; i++) {
                        if (b.getSquare()[row + i][colum + i] == 0) {
                            if (countAI == 0) {
                                if (Player == 2) {
                                    evaluateSquare[row + i][colum + i] += defenseScore[countHuman];
                                } else {
                                    evaluateSquare[row + i][colum + i] += attackScore[countHuman];
                                }
                                if (CheckPoint(row - 1, colum - 1) && CheckPoint(row + 5, colum + 5) && b.getSquare()[row - 1][colum - 1] == 2 && b.getSquare()[row + 5][colum + 5] == 2) {
                                    evaluateSquare[row + i][colum + i] = 0;
                                }
                            }
                            if (countHuman == 0) {
                                if (Player == 1) {
                                    evaluateSquare[row + i][colum + i] += defenseScore[countAI];
                                } else {
                                    evaluateSquare[row + i][colum + i] += attackScore[countAI];
                                }
                                if (CheckPoint(row - 1, colum - 1) && CheckPoint(row + 5, colum + 5) && b.getSquare()[row - 1][colum - 1] == 1 && b.getSquare()[row + 5][colum + 5] == 1) {
                                    evaluateSquare[row + i][colum + i] = 0;
                                }
                            }
                            if ((countAI == 4 || countHuman == 4) && ((CheckPoint(row + i - 1, colum + i - 1) && b.getSquare()[row + i - 1][colum + i - 1] == 0) || (CheckPoint(row + i + 1, colum + i + 1) && b.getSquare()[row + i + 1][colum + i + 1] == 0))) {
                                evaluateSquare[row + i][colum + i] *= 2;
                            } else if (countAI == 4 || countHuman == 4) {
                                evaluateSquare[row + i][colum + i] *= 2;
                            }
                        }
                    }
                }
            }
        }
        /**
         * Check in diagonal line
         */
        for (row = 4; row < n; row++) {
            for (colum = 0; colum < n - 4; colum++) {
                countAI = 0;
                countHuman = 0;
                for (i = 0; i < 5; i++) {
                    if (b.getSquare()[row - i][colum + i] == 2) {
                        countAI++;
                    }
                    if (b.getSquare()[row - i][colum + i] == 1) {
                        countHuman++;
                    }
                }
                if (countAI * countHuman == 0 && countAI != countHuman) {
                    for (i = 0; i < 5; i++) {
                        if (b.getSquare()[row - i][colum + i] == 0) {
                            if (countAI == 0) {
                                if (Player == 2) {
                                    evaluateSquare[row - i][colum + i] += defenseScore[countHuman];
                                } else {
                                    evaluateSquare[row - i][colum + i] += attackScore[countHuman];
                                }
                                if (CheckPoint(row + 1, colum - 1) && CheckPoint(row - 5, colum + 5) && b.getSquare()[row + 1][colum - 1] == 2 && b.getSquare()[row - 5][colum + 5] == 2) {
                                    evaluateSquare[row - i][colum + i] = 0;
                                }
                            }
                            if (countHuman == 0) {
                                if (Player == 1) {
                                    evaluateSquare[row - i][colum + i] += defenseScore[countAI];
                                } else {
                                    evaluateSquare[row - i][colum + i] += attackScore[countAI];
                                }
                                if (CheckPoint(row + 1, colum - 1) && CheckPoint(row - 5, colum + 5) && b.getSquare()[row + 1][colum - 1] == 1 && b.getSquare()[row - 5][colum + 5] == 1) {
                                    evaluateSquare[row + i][colum + i] = 0;
                                }
                            }
                            if ((countAI == 4 || countHuman == 4) && ((CheckPoint(row - i + 1, colum + i - 1) && b.getSquare()[row - i + 1][colum + i - 1] == 0) || (CheckPoint(row - i - 1, colum + i + 1) && b.getSquare()[row - i - 1][colum + i + 1] == 0))) {
                                evaluateSquare[row - i][colum + i] *= 2;
                            } else if (countAI == 4 || countHuman == 4) {
                                evaluateSquare[row - i][colum + i] *= 2;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method return a State with the highest value in Board
     *
     * @return a State
     */
    public State getMaxSquare() {
        Point p = new Point(0, 0);
        ArrayList<State> list = new ArrayList();
        int t = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (t < evaluateSquare[i][j]) {
                    t = evaluateSquare[i][j];
                    p.setLocation(i, j);
                    list.clear();
                    list.add(new State(p, t));
                } else if (t == evaluateSquare[i][j]) {
                    p.setLocation(i, j);
                    list.add(new State(p, t));
                }
            }
        }

//        for (int i = 0; i < list.size(); i++) {
//            evaluateSquare[list.get(i).getP().x][list.get(i).getP().y] = 0;
//        }
        int x = rand.nextInt(list.size());
        evaluateSquare[list.get(x).getP().x][list.get(x).getP().y] = 0;
        return list.get(x);
    }

    /**
     * Evaluation of the board
     */
    private int evaluationBoard(CaroBoard b) {
        String s = ";";
        //check in row and colum (|,__)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s += b.getSquare()[i][j];
            }
            s += ";";
            for (int j = 0; j < n; j++) {
                s += b.getSquare()[j][i];
            }
            s += ";";
        }
        // check on diagonally ( \ )
        for (int i = 0; i < n - 4; i++) {
            for (int j = 0; j < n - i; j++) {
                s += b.getSquare()[j][i + j];
            }
            s += ";";
        }
        // check bottom diagonally ( \ )
        for (int i = n - 5; i > 0; i--) {
            for (int j = 0; j < n - i; j++) {
                s += b.getSquare()[i + j][j];
            }
            s += ";";
        }
        // check on diagonally ( / )
        for (int i = 4; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                s += b.getSquare()[i - j][j];
            }
            s += ";";
        }
        // check bottom diagonally ( / )
        for (int i = n - 5; i > 0; i--) {
            for (int j = n - 1; j >= i; j--) {
                s += b.getSquare()[j][i + n - j - 1];
            }
            s += ";\n";
        }
        //X = 1 human, O = 2 AI; 
        String find1, find2;
        int diem = 0;
        for (int i = 0; i < caseHuman.length; i++) {
            find1 = caseAI[i];
            find2 = caseHuman[i];
            diem += point[i] * count(s, find1);
            diem -= point[i] * count(s, find2);
        }
        return diem;
    }

    /**
     * Count the number of "find" in "s"
     */
    public int count(String s, String find) {
        Pattern pattern = Pattern.compile(find);
        Matcher matcher = pattern.matcher(s);
        int i = 0;
        while (matcher.find()) {
            i++;
        }
        return i;
    }

    /**
     * Search moves
     */
    public Point search(CaroBoard bb) {
        CaroBoard b = new CaroBoard(bb.getSize());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b.getSquare()[i][j] = bb.getSquare()[i][j];
            }
        }
        evaluateEachSquare(b, 2);
        ArrayList<State> list = new ArrayList();
        for (int i = 0; i < maxSquare; i++) {
            list.add(getMaxSquare());
        }
        int maxp = Integer.MIN_VALUE;
        ArrayList<State> ListChoose = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 2;
            int t = MinVal(b, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            if (maxp < t) {
                maxp = t;
                ListChoose.clear();
                ListChoose.add(list.get(i));
            } else if (maxp == t) {
                ListChoose.add(list.get(i));
            }
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 0;
        }
        int x = rand.nextInt(ListChoose.size());
        return ListChoose.get(x).getP();
    }

    /**
     * Evaluation for MAX(AI)
     */
    private int MaxVal(CaroBoard b, int alpha, int beta, int depth) {
        int val = evaluationBoard(b);
        if (depth >= maxDepth || Math.abs(val) > 3000) {
            return val;
        }
        evaluateEachSquare(b, 2);
        ArrayList<State> list = new ArrayList();
        for (int i = 0; i < maxSquare; i++) {
            list.add(getMaxSquare());
        }
        for (int i = 0; i < list.size(); i++) {
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 2;
            alpha = Math.max(alpha, MinVal(b, alpha, beta, depth + 1));
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 0;
            if (alpha >= beta) {
                break;
            }
        }
        return alpha;
    }

    /**
     * Evaluation for MIN(Human)
     */
    private int MinVal(CaroBoard b, int alpha, int beta, int depth) {
        int val = evaluationBoard(b);
        if (depth >= maxDepth || Math.abs(val) > 3000) {
            return val;
        }
        evaluateEachSquare(b, 1);
        ArrayList<State> list = new ArrayList();
        for (int i = 0; i < maxSquare; i++) {
            list.add(getMaxSquare());
        }
        for (int i = 0; i < list.size(); i++) {
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 1;
            beta = Math.min(beta, MaxVal(b, alpha, beta, depth + 1));
            b.getSquare()[list.get(i).getP().x][list.get(i).getP().y] = 0;
            if (alpha >= beta) {
                break;
            }
        }
        return beta;
    }

    /**
     * Check valid
     */
    public boolean CheckPoint(int x, int y) {
        return (x >= 0 && y >= 0 && x < 20 && y < 20);
    }
}
