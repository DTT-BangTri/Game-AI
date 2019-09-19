package knightstour.Chess;

//Biểu thị một hình vuông trên bảng cờ vua

public class Square {
	private boolean isVisited; //true nếu ô đã được truy cập, nếu không thì fale.
	private int moveNumber; //số lượng di chuyển đã được thực hiện.
	private int accessibility; //Số lượng di chuyển có thể từ ô.

        
	public Square() {
		isVisited = false;
		moveNumber = 0;
	}

	//Nhận giá trị boolean và đặt thuộc tính truy cập của ô.
	public void setVisited(boolean visited) {
		isVisited = visited;
	}

	//Trả về giá trị của thuộc tính đã truy cập.
	public boolean isVisited() {
		return isVisited;
	}

	//số di chuyển hiện tại của người chơi và đánh dấu ô.
	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	//Trả lại giá trị của số di chuyển của ô.
	public int getMoveNumber() {
		return moveNumber;
	}

	//đặt giá trị khả năng tiếp cận của ô.
	public void setAccessibility(int accessibility) {
		this.accessibility = accessibility;
	}

	//Trả lại giá trị khả năng truy cập của ô.
	public int getAccessibility() {
		return accessibility;
	}

	//Giảm giá trị khả năng truy cập của ô.
	public void decrAccessibility() {
		accessibility--;
	}
}
