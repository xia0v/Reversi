package com.bjut.reversi;

public class MainClass {

	public static void main(String[] args) {
		Board board  = new Board();
		Player player1 = new Player();
		Player2 player2 = new Player2();
		board.setPlayer1(player2);
		board.setPlayer2(player1);
	}
}
