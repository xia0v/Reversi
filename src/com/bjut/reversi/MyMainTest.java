package com.bjut.reversi;

/**
 * 自己的测试工具
 * @author liwei
 *
 */
public class MyMainTest {

	public static void main(String[] args) {
		MLog.DEBUG =true;
			Board board  = new Board();
			Player player1 = new Player();
			Player2 player2 = new Player2();
			board.setPlayer1(player2);
			board.setPlayer2(player1);
//			board.readMessage("BLACK");
//			board.readMessage("WHITE");
//			board.readMessage(player2.readMessage("BLACK"));
		
	}

}
