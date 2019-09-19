package knightstour.Chess;

// Biểu diễn bàn cờ vua.
public class ChessBoard {
	private Square[][] playingBoard; //Mảng 2 chiều, đại diện cho bàn cờ.									
	private Knight currentKnight; //Tham chiếu đến Knight hiện tại trên 
                                      //bàn cờ.
	private final int BOARD_SIZE; //Kích thước của bàn cờ, (2x2, 3x3, ... ).

        //Tạo bàn cờ vua.
	public ChessBoard(Knight knight) {
		BOARD_SIZE = 8; // bàn cờ 8x8
		playingBoard = new Square[BOARD_SIZE][BOARD_SIZE];
		currentKnight = knight;
		createSquares();
		createHeuristics();
	}

	public ChessBoard(Knight knight, int dimension) {
		BOARD_SIZE = dimension; //bàn cờ kích thước bất kì.
		playingBoard = new Square[BOARD_SIZE][BOARD_SIZE];
		currentKnight = knight;
		createSquares();
		createHeuristics();
	}

        //tạo các ô trong bàn cờ.
	public void createSquares() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				playingBoard[row][col] = new Square();
			}
		}
	}

	public void addKnight(Knight knight) {
		currentKnight = knight;
	}

	public int getBoardSize() {
		return BOARD_SIZE;
	}

        //Trả về đối tượng hình vuông tại một hàng và cột xác định.
	public Square getSquareAt(int row, int col) {
		return playingBoard[row][col];
	}

        //Đặt giá trị 'true' trên một hình vuông để chỉ ra rằng nó đã được 
        //truy cập.
	public void setSquareVisited(int row, int col) {
		playingBoard[row][col].setVisited(true);
	}

        //Trả lại giá trị 'true' trên một hình vuông để chỉ ra rằng nó đã 
        //được truy cập.
	public boolean isSquareVisited(int row, int col) {
		return playingBoard[row][col].isVisited();
	}

        //Thiết lập mức truy cập của một hình vuông nằm ở các tham số 
        // hàng và cột.
	public void setSquareAccessibility(int row, int col, int accessibility) {
		playingBoard[row][col].setAccessibility(accessibility);
	}

        //Trả lại mức truy cập của một hình vuông nằm ở hàng và cột 
        //được xác định bởi các tham số.
	public int getSquareAccessibility(int row, int col) {
		return playingBoard[row][col].getAccessibility();
	}

        //Giảm khả năng tiếp cận của hình vuông bằng 1 hàng và cột 
        //được xác định bởi các tham số.
	public void decrSquareAccessibility(int row, int col) {
		playingBoard[row][col].decrAccessibility();
	}

        //Thiết lập một hình vuông cho giá trị truy cập di chuyển hiện tại 
        //của Knight tại vị trí được chỉ định bởi các tham số.
	public void setSquareMoveNumber(int row, int col, int moveCounter) {
		playingBoard[row][col].setMoveNumber(moveCounter);
	}

        //Trả lại giá trị bộ đếm truy cập tại vị trí 
        //được chỉ định bởi các tham số.
	public int getSquareMoveNumber(int row, int col) {
		return playingBoard[row][col].getMoveNumber();
	}

        //Đánh dấu hình vuông của bảng ở hàng và cột được cung cấp 
        //bởi các tham số với số biến số truy cập của Knight.
	public void markBoardSquare(int curRow, int curCol, int moveCounter) {
		setSquareVisited(curRow, curCol);
		setSquareMoveNumber(curRow, curCol, moveCounter);
	}
        
        //In bảng trò chơi tất cả các trạng thái có thể.
	public void showGameBoard() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
		            System.out.printf("%3d", playingBoard[row][col].getMoveNumber());
				if (col == BOARD_SIZE - 1) {
					System.out.println();
				}
			}
		}
	}

        //In các heuristics có khả năng truy cập.
	public void showHeuristics() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				System.out.printf("%3d", getSquareAccessibility(row, col));
				if (col == BOARD_SIZE - 1) {
					System.out.println();
				}
			}
		}
	}
        
        //Sử dụng một đối tượng knight dummy để chơi qua tất cả các di chuyển 
        //có thể cho mỗi hình vuông trên bàn cờ.
        //Số lượng di chuyển có thể từ mỗi hình vuông được ghi lại trong mảng.
	private void createHeuristics() {
		int linkCount = 0, //Lưu trữ số ô liên kết cho một hình vuông cụ thể
		testRow, //Hàng hiện tại của Knight.
		testCol; //Cột hiện tại của Knight.

		//Thêm giá trị số di chuyển vào hàng hiện tại của Knight
                //và kiểm tra xem đó có phải là ứng cử viên di chuyển hay không.
                //Nếu vị trí nằm trên bàn cờ, nó sẽ tăng số liên kết.
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				for (int moveNum = 0; moveNum < Knight.NUM_ALLOWED_MOVES; moveNum++) {
					testRow = row + currentKnight.getVerticalMoveValue(moveNum);
					testCol = col
							+ currentKnight.getHorizontalMoveValue(moveNum);

					if (checkSquareExistsAndIsUnVisted(testRow, testCol) == true) {
						linkCount++;
					}
				}

				//Đặt tính truy cập và đặt lại số liên kết cho cột tiếp theo.
				setSquareAccessibility(row, col, linkCount);
				linkCount = 0;
			}
		}
	}

        //Sử dụng một hàng và cột hiện tại của bàn cờ để xác định xem hàng 
        //và cột nằm trong giới hạn của bàn cờ và chưa được truy cập. 
        //Trả về true, nếu không gian là hợp lệ, nếu không thì trả về false.
	public boolean checkSquareExistsAndIsUnVisted(int testRow, int testCol) {
		if ((testRow >= 0 && testRow < BOARD_SIZE)
				&& (testCol >= 0 && testCol < BOARD_SIZE)) {
			if (isSquareVisited(testRow, testCol) == false) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
        
        //Kiểm tra tất cả các ô vuông có thể truy cập từ ô vuông hiện tại 
        //và giảm khả năng tiếp cận của chúng xuống 1.
	public void lowerAccessibility() {
		int[] possibleMoves;
		int numOfPossibleMoves, curRow = currentKnight.getCurrentRow(), curCol = currentKnight
				.getCurrentCol(), changeRow, changeCol;

		//số  trạng thái di chuyển có thể.
		numOfPossibleMoves = currentKnight.findNumOfPossibleMoves(this);

		//Tạo một mảng với kích thước của số di chuyển có thể
		possibleMoves = new int[numOfPossibleMoves];

		//số di chuyển của các trạng thái có thể.
		possibleMoves = currentKnight.findPossibleMoves(this, numOfPossibleMoves);

		for (int moveNum = 0; moveNum < possibleMoves.length; moveNum++) {
			changeRow = curRow + currentKnight.getVerticalMoveValue(possibleMoves[moveNum]);
			changeCol = curCol + currentKnight.getHorizontalMoveValue(possibleMoves[moveNum]);

			if (isSquareVisited(changeRow, changeCol) == false) {
				decrSquareAccessibility(changeRow, changeCol);
			}
		}
	}
}
