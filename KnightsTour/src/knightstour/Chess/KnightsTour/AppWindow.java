package knightstour.Chess.KnightsTour;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Application cho Knight's tour.

public class AppWindow extends JFrame {
	private Tour knightsTour; //đối tượng đi tuần.
	private int boardSize; //kích thước của bàn cờ.
	private GUISquare guiSquares[][]; //mảng của JPanels đại diện cho ô.
	private boolean isTourRunning = false; //true nếu tour đang chạy, không thì fale
	private boolean isTourFinished = false; //true nếu tour đã hoàn thành, không thì fale.
	private boolean useNewGameParams = true; //true nếu người dùng muốn tham số tour mới, 
                                           //false nếu người dùng muốn giống như tour trước đó
	private Timer timer; //số lần di chuyển của knight.
	private final int MAX_BOARD_SIZE = 100; //kích thước lớn nhất của bàn cờ.

	//cài đặt hoạt động đóng cửa mặc định cho cửa sổ và khởi chạy một tour mới.
	public AppWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		newGame();
		createTimer();
	}

	//Thiết lập các thông số cho tour mới của knight.
	private void newGame() {
		//Nếu kích thước của bàn cờ >0, thì bàn cờ phải được xóa trước khi bắt đầu lại.
		if (boardSize > 0) {
			//Loại bỏ các ô hiện tại khỏi bàn cờ.
			removeSquares();

			//khởi động lại cửa sổ.
			repaint();
		}

		//Nếu người dùng lựa chọn để nhập các thông số trò chơi mới 
                //nhắc người sử dụng cho các kích thước bàn cờ mới 
		if (useNewGameParams == true) {
			//Nhắc nhở người sử dụng kích thước bàn cờ.
			boardSize = promptBoardSize();
		}

		//Nếu người dùng muốn tham số trò chơi mới,
                //hãy thay đổi kích thước cửa sổ 
                //nếu không để cửa sổ có cùng kích thước
		if (useNewGameParams == true) {
			// Set size of window
			setSize(boardSize * 35, boardSize * 35);
		}

		//Đặt bố trí lưới, các hàng và cột bằng kích thước của bảng, 
		setLayout(new GridLayout(boardSize, boardSize, boardSize / boardSize,
				boardSize / boardSize));

		//tạo đối tượng mới.
		knightsTour = new Tour(boardSize);

		//tạo các ô
		drawSquares();
	}

	//tạo các ô lên màn hình.
	private void drawSquares() {
		//Tạo mảng mới với kích thước của bảng
		guiSquares = new GUISquare[boardSize][boardSize];

		//Lồng nhau lặp đi lặp lại qua tất cả các ô vuông
		for (int row = 0; row < guiSquares.length; row++) {
			for (int col = 0; col < guiSquares.length; col++) {
				//tạo mới đối tượng GUISquare
				guiSquares[row][col] = new GUISquare();

				//Thêm sự kiện Mousse vào trong mảng
				guiSquares[row][col].addMouseListener(new SquareClickHandler());

				//màu các ô vuông.
				setCorrectSquareColor(row, col);

				//thêm ô vuông vào Frame
				add(guiSquares[row][col]);
			}
		}
                //Nên được viện dẫn khi các thành phần con của vùng chứa này được sửa đổi 
                //(thêm vào hoặc xoá khỏi vùng chứa, hoặc thông tin về bố cục đã thay đổi) 
                //sau khi vùng chứa được hiển thị.
		validate();
	}

	//Xác định màu của ô dựa trên vị trí của nó trên bàn cờ.
	private void setCorrectSquareColor(int row, int col) {
		if ((row + col) % 2 == 0) {
			guiSquares[row][col].setBackground(Color.RED);
		} else {
			guiSquares[row][col].setBackground(Color.WHITE);
		}
	}

	//Loại bỏ các ô vuông khỏi container.
	private void removeSquares() {
		for (int row = 0; row < guiSquares.length; row++) {
			for (int col = 0; col < guiSquares.length; col++) {
				//gọi hàm xóa các ô khỏi Frame
				remove(guiSquares[row][col]);
			}
		}
	}
        
	private int promptBoardSize() {
		String userBoardSize; //Chuỗi giá trị để giữ đầu vào của người dùng
		int boardSize = 0; //Giá trị số nguyên cần trả
		boolean isUserInputInvalid = true; // True cho biết đầu vào không hợp lệ

		//Thực hiện các thao tác sau khi đầu vào của người dùng không hợp lệ
		do {
			//Nhắc người dùng nhập và lưu trữ trong biến String
			userBoardSize = JOptionPane
					.showInputDialog("Nhập một số nguyên để đặt số hàng và số cột trên bảng:");

			//Nếu đầu vào của người dùng là một giá trị null,
                        //người dùng nhấp vào hủy bỏ hoặc đóng cửa sổ thoát khỏi chương trình.
			if (userBoardSize == null) {
				System.exit(0);
			} else {
				try {
					//Kiểm tra nếu chuỗi trống và gán giá trị boolean cho chuỗi không hợp lệ
					isUserInputInvalid = userBoardSize.isEmpty();

					boardSize = Integer.parseInt(userBoardSize);

					//Nếu không ngoại lệ, chương trình sẽ tiếp tục ở đây. 
                                        //Nếu kích thước bảng <= 0, chuỗi vẫn không hợp lệ do đó hiển thị lỗi
					if (boardSize <= 0 || boardSize >= MAX_BOARD_SIZE) {
						String message;

						isUserInputInvalid = true;

						message = boardSize <= 0 ? "Bạn phải nhập một giá trị số nguyên hợp lệ!!!"
								: String.format("Kích thước của bàn cờ không thể vượt quá %d",
										MAX_BOARD_SIZE);

						JOptionPane.showMessageDialog(this, message,
								"Giá trị không hợp lệ", JOptionPane.WARNING_MESSAGE);
					}
				}
				//xử lý NumberFormatException từ parseInt
				catch (NumberFormatException numberFormatException) {
					//hiện thị thông báo lỗi
					JOptionPane.showMessageDialog(this,
							"Bạn phải nhập một giá trị nguyên hợp lệ!!!",
							"Giá trị không hợp lệ!!!", JOptionPane.WARNING_MESSAGE);
				}
			}

		} while (isUserInputInvalid == true);

		//đầu vào là hợp lệ, thiết lập kích thước bàn cờ.
		boardSize = Integer.parseInt(userBoardSize);

		return boardSize;
	}

	//Tạo hành động thực hiện di chuyển của một knight ở một khoảng thời gian
	private void createTimer() {
		int delay = 50; //thời gian trễ.

		//thiết lập tour đã kết thúc thành false.
		isTourFinished = false;

		//Tạo biến danh sách tác vụ vô danh và 
                //ghi đè lên phương thức actionPerformed để gọi moveKnight
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//đặt màu cho ô.
				setCorrectSquareColor(knightsTour.getKnight().getCurrentRow(),
						knightsTour.getKnight().getCurrentCol());

				moveKnight();
				flashColor();
			}
		};
		timer = new Timer(delay, taskPerformer);
	}

	//Thay đổi màu sắc của ô hiện tại
	private void flashColor() {
		int currentRow = knightsTour.getKnight().getCurrentRow();
		int currentCol = knightsTour.getKnight().getCurrentCol();
		GUISquare currentSquare = guiSquares[currentRow][currentCol];
		Color flashColor = Color.GREEN;

		currentSquare.setBackground(flashColor);
	}
        
        //bắt đầu bộ đếm thời gian.
	private void startTimer() {
		timer.start();
	}

	//Ngừng bộ đếm thời gian đang chạy
	private void stopTimer() {
		timer.stop();
	}

	//Di chuyển knight và đặt text trên nhãn của ô hiện tại
	private void moveKnight() {
		GUISquare currentSquare; //lưu vị trí hiện tại của knight
		int currentRow; // Vị trí hàng hiện tại của knight
		int currentCol; // Vị trí cột hiện tại của knight

		//nhận giá trị hàng và cột hiện tại của knight.
		currentRow = knightsTour.getKnight().getCurrentRow();
		currentCol = knightsTour.getKnight().getCurrentCol();

		//đặt ô hiện tại.
		currentSquare = guiSquares[currentRow][currentCol];

		//đặt nhãn trên ô.
		currentSquare.setText(Integer.toString(knightsTour.getKnight()
				.getMoveCounter()));

		//di chuyển knights
		knightsTour.move();

		//Nếu hiệp sĩ không có động tác nào khác, dừng đồng hồ đếm ngược
                //thiết lập tour đang chạy thành fale và tour kết thúc thành true.
		if (knightsTour.hasMove() == false) {
			timer.stop();
			isTourRunning = false;
			isTourFinished = true;
		}
	}

	//Yêu cầu người dùng nếu họ muốn có các thông số mới cho tour tiếp theo.
	public void askNewParam() {
		int newParamAnswer; //Giá trị số nguyên của câu trả lời hộp thoại 
                                    //(Có = 0, Không = 1, Hủy = 3)

		//Lưu phản hồi từ hộp thoại
		newParamAnswer = JOptionPane.showConfirmDialog(this,
				"Bạn có muốn sử dụng cùng một bàn cờ?", "Tạm dừng",
				JOptionPane.YES_NO_CANCEL_OPTION);

		//Nếu có, người dùng không nhận được thông số tour mới
		if (newParamAnswer == 0) {
			useNewGameParams = false;
		}
		//Nếu không, người dùng sẽ có thông số tour mới
		else if (newParamAnswer == 1) {
			useNewGameParams = true;
		}
		//nếu hủy, thoát chương trình
		else {
			System.exit(0);
		}
	}

	//Yêu cầu người dùng nếu họ muốn có một tour mới 
        //nếu người dùng nhấp vào một hình vuông trong khi tour đang chạy.
	public void tourPaused() {
		int newGameAnswer;

		//Ngừng thời gian, tạm dừng tour
		stopTimer();

		//thiết lập TourRunning là fale.
		isTourRunning = false;

		//phản hồi từ hộp thoại
		newGameAnswer = JOptionPane
				.showConfirmDialog(this, "Bắt đầu một tour mới?", "Tạm dừng",
						JOptionPane.YES_NO_CANCEL_OPTION);

		// nếu có.
		if (newGameAnswer == 0) {
			askNewParam();

			//đặt lại knightstour, xóa trạng thái hiện tại.
			knightsTour.resetTour();

			//tạo tour mới.
			newGame();
		}

		else if (newGameAnswer == 1) {
                    //Nếu người dùng không muốn một trò chơi mới đặt tour lại
                    //và khởi động lại bộ đếm thời gian.

			isTourRunning = true;
			startTimer();
		}
		//nếu hủy, thoát chương trình
		else {
			System.exit(0);
		}
	}

	public void showHeuristics() {
		for (int rowNumber = 0; rowNumber < boardSize; rowNumber++) {
			for (int columnNumber = 0; columnNumber < boardSize; columnNumber++) {
				guiSquares[rowNumber][columnNumber].setText(Integer
						.toString(knightsTour
								.getSquare(rowNumber, columnNumber)
								.getAccessibility()));
			}
		}
	}

	//sự kiện kích chuột.
	class SquareClickHandler extends MouseAdapter {
		
                //xử lý sự kiện kích chuột.
		@Override
		public void mouseClicked(MouseEvent meEvent) {
			//nếu TourRunning = fale, chạy khi chuột được kích.
			if (isTourRunning == false) {
				//Nếu rour đã kết thúc, 
                                //nghĩa là người dùng đã không làm gián đoạn tour
                                //với các nhấp chuột tiếp theo và 
                                //chuyến tham quan được phép hoàn tất
				if (isTourFinished == true) {
					//đặt tourfinished trở lại fale
					isTourFinished = false;

					askNewParam();
					newGame();
				}

				for (int rowNumber = 0; rowNumber < boardSize; rowNumber++) {
					for (int columnNumber = 0; columnNumber < boardSize; columnNumber++) {
						//So sánh ô với nguồn sự kiện, để tìm ra ô nào đã được nhấp vào
						if (meEvent.getSource() == guiSquares[rowNumber][columnNumber]) {

							//đặt vị trí bắt đầu
							knightsTour.setStartPosition(rowNumber,
									columnNumber);
							isTourRunning = true;
							startTimer();
							break;
						}
					}
				}
                        }
			else {
				tourPaused();
			}
		}
	}
}