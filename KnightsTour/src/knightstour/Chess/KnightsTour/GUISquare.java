package knightstour.Chess.KnightsTour;

import java.awt.*;
import javax.swing.*;

//JPanel đại diện cho một hình vuông trên một bảng cờ vua. 
//Panel chứa một nhãn có văn bản có thể được thay đổi theo chương trình.

public class GUISquare extends JPanel {
	private JLabel label; //JLabel trong Panel.

	//khởi tạo hình vuông.
	public GUISquare() {
		//tạo đối tượng JLabel
		label = new JLabel();

		//đặt màu nhãn trong ô.
		label.setForeground(Color.BLUE);

		// Add label vào Panel.
		add(label);
	}

	//đặt nhãn ô bằng tham số chuỗi.
	public void setText(String text) {
		label.setText(text);
	}

	//Đặt nhãn của ô không hợp lệ, nhãn này sẽ xóa.
	public void resetText() {
		label.setText(null);
	}
}
