package knightstour.Chess.KnightsTour;

import knightstour.Chess.ChessBoard;
import knightstour.Chess.Knight;
import knightstour.Chess.Square;

//Sử dụng bàn cờ vua và một knight để chơi trò chơi Knight's Tour.

public class Tour {
	private ChessBoard chessBoard; //bàn cờ.
	private Knight knightPiece; // Knight di chuyển.
	private boolean foundMove = false; //true nếu knight có thể di chuyển, 
                                            //không thì fale

	public Tour(ChessBoard chessBoard, Knight knight) {
		knightPiece = knight;
		this.chessBoard = chessBoard;
		this.chessBoard.addKnight(knightPiece);
	}

	public Tour() {
		knightPiece = new Knight();
		chessBoard = new ChessBoard(knightPiece);
	}

	public Tour(int dimension) {
		knightPiece = new Knight();
		chessBoard = new ChessBoard(knightPiece, dimension);
	}

	public Tour(int startRow, int startCol) {
		knightPiece = new Knight(startRow, startCol);
		chessBoard = new ChessBoard(knightPiece);
	}

	public Tour(int startRow, int startCol, int dimension) {
		knightPiece = new Knight(startRow, startCol);
		chessBoard = new ChessBoard(knightPiece, dimension);
	}

	public void resetTour() {
		knightPiece = null;
		chessBoard = null;
	}

	//Đặt vị trí bắt đầu của knight trên bảng cờ vua.
	public void setStartPosition(int startRow, int startCol) {
		knightPiece.setCurrentRow(startRow);
		knightPiece.setCurrentCol(startCol);
		knightPiece.setPreviousRow(startRow);
		knightPiece.setPreviousCol(startCol);
	}

	public ChessBoard getChessBoard() {
		return chessBoard;
	}

	public Knight getKnight() {
		return knightPiece;
	}

	public Square getSquare(int row, int col) {
		return chessBoard.getSquareAt(row, col);
	}

        //Nếu knight không có một động thái, sau đó các tour kết thúc.
	public boolean hasMove() {
		return foundMove;
	}

	//
	public void playGame() {
		do {
			move();

		} while (foundMove == true);

		System.out.println("Game Board");
		chessBoard.showGameBoard();

		System.out.printf("Number of moves: %d\n", knightPiece.getMoveCounter());

		System.out.println();
	}

	public void move() {
		int numPossibleMoves, bestMove;
		int[] possibleMoves;

		foundMove = false;
		//Đánh dấu trạng thái đã truy cập và di chuyển tại vị trí hiện tại
		chessBoard.markBoardSquare(knightPiece.getCurrentRow(),
				knightPiece.getCurrentCol(), knightPiece.getMoveCounter());

		//Giá trị tiếp cận thấp hơn của các ô vuông xung quanh.
		chessBoard.lowerAccessibility();

		//Tìm số di chuyển có thể từ vị trí hiện tại.
		numPossibleMoves = knightPiece.findNumOfPossibleMoves(chessBoard);

		//Nếu số lượng di chuyển có thể >0, có ít nhất một di chuyển có thể.
		if (numPossibleMoves > 0) {
			foundMove = true;

			//Tìm các chuyển động có thể từ vị trí hiện tại và trở lại thành một mảng
			possibleMoves = knightPiece.findPossibleMoves(chessBoard,
					numPossibleMoves);

			//Sử dụng mảng di chuyển có thể, tìm động thái tốt nhất dựa trên khả năng tiếp cận heuristic
			bestMove = knightPiece.findBestMove(chessBoard, possibleMoves);

			//Di chuyển knight tốt nhất
			knightPiece.move(bestMove);
		}
	}
}
