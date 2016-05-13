package com.bjut.reversi;

public interface IPlayer {
	public static final int WHITE=1;//1表示白方
	public static final int BLACK=-1;//-1表示黑方
	public static final int SPACE=0;//0表示空
	
	public String readMessage(String message);
	
	public boolean isBlack();

	public void boardInit();
}
