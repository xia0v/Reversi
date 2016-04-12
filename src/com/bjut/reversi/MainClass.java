package com.bjut.reversi;

import java.io.IOException;

public class MainClass {

	public static void main(String[] args) {
		Board board  = new Board();
		Player player = new Player();
		board.setPlayer1(player);
		board.readMessage("BLACK");
		board.readMessage(player.readMessage("WHITE"));
//		System.out.println("BLACK");
	}
}
