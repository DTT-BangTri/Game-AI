package knightstour.Chess;

public class Knight {
	private int[] horizontalMovesAvail; //Giá trị di chuyển ngang có sẵn cho knight.
	private int[] verticalMovesAvail; //Giá trị di chuyển dọc có sẵn cho knight.
	private int currentRow; //Vị trí hàng hiện tại của knight.
	private int currentCol; //Vị trí cột hiện tại củaknight
	private int previousRow; //Hàng của knight trước khi di chuyển.
	private int previousCol; //cột của knight trước khi di chuyển.
	private int moveCounter; //Theo dõi số lần di chuyển của hiệp sỹ (1-64)
	public static final int NUM_ALLOWED_MOVES = 8; //Số di chuyển mà hiệp sĩ được phép thực hiện

	public Knight() {
		horizontalMovesAvail = new int[NUM_ALLOWED_MOVES];
		verticalMovesAvail = new int[NUM_ALLOWED_MOVES];
		moveCounter = 1;
		setCurrentRow(0);
		setCurrentCol(0);
		setPreviousRow(0);
		setPreviousCol(0);
		fillMoves();
	}

	public Knight(int startRow, int startCol) {
		horizontalMovesAvail = new int[NUM_ALLOWED_MOVES];
		verticalMovesAvail = new int[NUM_ALLOWED_MOVES];
		moveCounter = 1;
		setCurrentRow(startRow);
		setCurrentCol(startCol);
		setPreviousRow(startRow);
		setPreviousCol(startCol);
		fillMoves();
	}

        //Đặt hàng và cột trước đó, và tăng bộ đếm di chuyển.
	public void move(int moveNumber) {
		if (moveNumber < 0 || moveNumber > NUM_ALLOWED_MOVES - 1) {
			System.out.println("ERROR: Invalid move: " + moveNumber);
			return;
		} else {
			setPreviousRow(currentRow);
			setPreviousCol(currentCol);
			setCurrentRow(currentRow + verticalMovesAvail[moveNumber]);
			setCurrentCol(currentCol + horizontalMovesAvail[moveNumber]);
			incMoveCounter();
		}
	}
        
        //Tìm thấy số lần di chuyển có thể trên bàn cờ từ hàng và cột bắt đầu.
	public int findNumOfPossibleMoves(ChessBoard chessBoard) {
		int possibleMoveCounter = 0, //Đếm số lần di chuyển có thể
		startRow = getCurrentRow(), startCol = getCurrentCol(), testRow, testCol;

                //thêm một giá trị số di chuyển vào hàng hiện tại của knight và 
                //kiểm tra xem nó có phải là một ứng cử viên di chuyển hay không
                //nếu vị trí nằm trên bàn cờ và không được báo trước, 
                //bộ đếm di chuyển có thể được tăng lên.
		for (int moveNum = 0; moveNum < Knight.NUM_ALLOWED_MOVES; moveNum++) {
			testRow = startRow + getVerticalMoveValue(moveNum);
			testCol = startCol + getHorizontalMoveValue(moveNum);

			if (chessBoard.checkSquareExistsAndIsUnVisted(testRow, testCol) == true) {
				possibleMoveCounter++;
			}
		}

		return possibleMoveCounter;
	}

        //Tìm các chuyển động trên bàn cờ từ vị trí hiện tại của knight
        //và trả về một loạt các trạng thái có thể.
	public int[] findPossibleMoves(ChessBoard chessBoard, int numPossibleMoves) {
		int[] possibleMoves = new int[numPossibleMoves];
		int storeCurRow = getCurrentRow(), //lưu hàng hiện tại của knight
		storeCurCol = getCurrentCol(), //lưu cột hiện tại của knight
		testRow,
		testCol,
		goodMoveCount = 0;
                
		for (int moveNum = 0; moveNum < Knight.NUM_ALLOWED_MOVES; moveNum++) {
			testRow = storeCurRow + getVerticalMoveValue(moveNum);
			testCol = storeCurCol + getHorizontalMoveValue(moveNum);

			if (chessBoard.checkSquareExistsAndIsUnVisted(testRow, testCol) == true) {
				possibleMoves[goodMoveCount++] = moveNum;
			}
		}

		return possibleMoves;
	}
        
	public int findBestMove(ChessBoard chessBoard, int[] possibleMoves) {
		int storeCurRow = getCurrentRow(),
		storeCurCol = getCurrentCol(),
		testRow,
		testCol,
		lowestAccessibility, testAccessibility, moveNumWithLowest;

		//Nếu mảng di chuyển có thể lớn hơn 1, thì có ít nhất 2 di 
                //chuyển để so sánh khả năng truy cập.
		if (possibleMoves.length > 1) {
			// Give iLowestAccessibility a starting value to compare the rest to
			testRow = storeCurRow + getVerticalMoveValue(possibleMoves[0]);
			testCol = storeCurCol + getHorizontalMoveValue(possibleMoves[0]);
			lowestAccessibility = chessBoard.getSquareAccessibility(testRow,
					testCol);
			moveNumWithLowest = possibleMoves[0];

			//Kiểm tra mỗi lần di chuyển trong mảng so với giá trị thấp nhất
                        //iMoveNum in loop header giá trị ban đầu thay đổi từ 0 đến 1 
                        //Lý do: Giá trị thấp nhất ban đầu được thiết lập để di chuyển 0, 
                        //nếu iMoveNum còn lại ở 0, thì nó không cần thiết phải 
                        //so sánh nó trong vòng lặp
			for (int moveNum = 1; moveNum < possibleMoves.length; moveNum++) {
				testRow = storeCurRow
						+ getVerticalMoveValue(possibleMoves[moveNum]);
				testCol = storeCurCol
						+ getHorizontalMoveValue(possibleMoves[moveNum]);

				testAccessibility = chessBoard.getSquareAccessibility(testRow,
						testCol);

				//Nếu giá trị được kiểm tra thấp hơn giá trị 
                                //thấp nhất hiện thời lưu trữ giá trị trợ năng 
                                //và lưu trữ số di chuyển với giá trị thấp nhất 
                                //được tìm thấy trong vị trí mảng đó

				if (testAccessibility < lowestAccessibility
						|| lowestAccessibility < 1) {
					lowestAccessibility = testAccessibility;
					moveNumWithLowest = possibleMoves[moveNum];
				}
			}

			return moveNumWithLowest;
		}
		//Trả lại giá trị đầu tiên trong mảng nếu chiều dài mảng là 1, 
                //bởi vì không có chuyển động nào khác để so sánh
		else
			return possibleMoves[0];
	}
        
        //đặt hàng hiện tại.
	public void setCurrentRow(int row) {
		currentRow = row;
	}

	//Trả lại giá trị của hàng hiện tại.
	public int getCurrentRow() {
		return currentRow;
	}

        //đặt cột hiện tại.
	public void setCurrentCol(int col) {
		currentCol = col;
	}

        //Trả lại giá trị của cột hiện tại
	public int getCurrentCol() {
		return currentCol;
	}

        //đặt hàng trước đó.
	public void setPreviousRow(int row) {
		previousRow = row;
	}

	//trả lại giá trị hàng trước đó.
	public int getPreviousRow() {
		return previousRow;
	}

        //đặt cột trước đó.
	public void setPreviousCol(int col) {
		previousCol = col;
	}

        //trả lại giá trị cột trước đó.
	public int getPreviousCol() {
		return previousCol;
	}

	//Tăng biến thành viên truy cập di chuyển bằng 1.
	public void incMoveCounter() {
		moveCounter++;
	}

	//Trả lại giá trị của bộ đếm di chuyển
	public int getMoveCounter() {
		return moveCounter;
	}

	//số di chuyển và trả về giá trị của hàng đang di chuyển.
	public int getHorizontalMoveValue(int moveNumber) {
		return horizontalMovesAvail[moveNumber];
	}

	//số di chuyển và trả về giá trị của cột đang di chuyển.
	public int getVerticalMoveValue(int moveNumber) {
		return verticalMovesAvail[moveNumber];
	}

	//Lập bản đồ di chuyển theo chiều ngang và chiều dọc của knight.
	private void fillMoves() {
		horizontalMovesAvail[0] = 2; //phải
		horizontalMovesAvail[1] = 1; //phải
		horizontalMovesAvail[2] = -1; //trái
		horizontalMovesAvail[3] = -2; //trái
		horizontalMovesAvail[4] = -2; //trái
		horizontalMovesAvail[5] = -1; //trái
		horizontalMovesAvail[6] = 1; //phải
		horizontalMovesAvail[7] = 2; //phải

		verticalMovesAvail[0] = -1; //lên
		verticalMovesAvail[1] = -2; //lên
		verticalMovesAvail[2] = -2; //lên
		verticalMovesAvail[3] = -1; //lên
		verticalMovesAvail[4] = 1; //xuống
		verticalMovesAvail[5] = 2; //xuống
		verticalMovesAvail[6] = 2; //xuống
		verticalMovesAvail[7] = 1; //xuống
	}
}
