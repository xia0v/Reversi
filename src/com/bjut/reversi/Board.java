package com.bjut.reversi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 棋盘 界面类
 * @author liwei
 *
 */
public class Board extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int WHITE=1;//1表示白方
	public static final int BLACK=-1;//-1表示黑方
	public static final int SPACE=0;//0表示空
	private int playColor = BLACK;//选手颜色
	
	private boolean end;//是否已结束
	private boolean isBVB;//是否AI对决
	private IPlayer player1;
	private IPlayer player2;
	private String myMessage = "NO";//点击输出的内容
	private JPanel boradLayout;//棋板布局
	private JButton jpNowchess;//当前棋手颜色
	private JLabel jpResult;//当前棋数
	private JLabel jpResult2;//当前棋数
	
	private Component[][] boardView = new Component[9][9];//棋盤
	private int board[][] = new int[8][8];//所有棋子
	
	
	
	public Board() {
		 this.setLayout(new BorderLayout());
		 this.setTitle("黑白棋");
		 this.setBounds(100, 100, 780, 600);  
		 boradLayout = new JPanel(new GridLayout(9, 9));
		 initBoard();
		 initBoardView();
		 initMenu();
		 this.add(boradLayout,"Center");
		 this.setVisible(true); 
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	}
	
	public void setPlayer1(IPlayer player){
		this.player1 = player;
	}
	public void setPlayer2(IPlayer player){
		this.player2 = player;
	}
	
	
/*	public static void main(String[] args) {
		Board board = new Board();
		board.readMessage("");
	}*/
	
	public void readMessage(String message){
		MLog.i("player = "+message);
		if(message.equals("BLACK")){//如果是BLACK，则表示是开局执黑
			this.playColor = BLACK;
			showDialog("执黑棋先走！");
		}else if(message.equals("WHITE")){//如果是WHITE，则表示是开局执白
			this.playColor = WHITE;
			sendMessage("NO");
		}else if(message.equals("NO")){
			//对方上一步无棋可走
		}else{//普通坐标
			int xOpp=message.charAt(0)-'1', yOpp = message.charAt(1)-'A';
			if(this.player1.isBlack()){
				//player1.modifyBoard(xOpp, yOpp, 1);
				if(!this.pieceLegalJudge(xOpp, yOpp,BLACK, true))sendMessage("NO");
			}
			else{
				//player1.modifyBoard(xOpp, yOpp, -1);
				if(!this.pieceLegalJudge(xOpp, yOpp, WHITE, true))sendMessage("NO");
			}
		}
	}
	 

	/**
	 * 接受消息
	 * @return
	 */
	public String getMessage(String message){
	/*	String message = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try{
			message= br.readLine();
		}catch(IOException e){
			e.printStackTrace();//获得输入失败
		}*/
		readMessage(player1.readMessage(message));
		return message;
	}
	/**
	 * 输出消息
	 * @param message
	 */
	public void sendMessage(String message){
		/*try {
			System.in.read((message+"\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		MLog.i("board = "+message);//输出消息
		readMessage(player1.readMessage(message));
	}
	
	public void initBoard(){
		for(int i=0;i<8;i++){//棋盘初始化，0表示空，1表示白，-1表示黑
			for(int j=0;j<8;j++){
				board[i][j] = SPACE;
			}
		}
		board[4][4] = WHITE;
		board[3][3] = WHITE;
		board[4][3] = BLACK;
		board[3][4] = BLACK;
	}
	/**
	 * 初始化棋盤
	 */
	private void initBoardView(){
		Label space = new Label();
		boradLayout.add(space);
		boardView[0][0]=space;
		for(int i =1;i<9;i++){
			Label label = new Label("   "+(char)(i-1+'A'));  
			boradLayout.add(label);
			boardView[0][i]=label;
		}
		
		for(int i =1;i<9;i++){
		 for(int j =0;j<9;j++){
		  if(j==0){
			Label label = new Label(""+i); 
			boradLayout.add(label); 
			boardView[i][0]=label;
		  }else{
			JButton btn = new JButton("");  
			btn.setActionCommand(i+"_"+j);
			btn.setBackground(Color.GRAY);
			btn.addActionListener(this);
			boradLayout.add(btn);  
			boardView[i][j]=btn;
			}
		 }
		}
		boardView[4][4].setBackground(Color.WHITE);
		boardView[5][5].setBackground(Color.WHITE);
		boardView[4][5].setBackground(Color.BLACK);
		boardView[5][4].setBackground(Color.BLACK);
	}
	private void resetBoard(){
		initBoard();
		for(int i =1;i<9;i++){
			 for(int j =0;j<9;j++){
			  if(j!=0){
				boardView[i][j].setBackground(Color.GRAY);;
				}
			 }
		}
		boardView[4][4].setBackground(Color.WHITE);
		boardView[5][5].setBackground(Color.WHITE);
		boardView[4][5].setBackground(Color.BLACK);
		boardView[5][4].setBackground(Color.BLACK);
		player1.boardInit();
	}
	
	private void initMenu() {
		JPanel jpMenu = new JPanel();
		jpMenu.setBackground(new Color(160, 207, 230));
		jpMenu.setPreferredSize(new Dimension(150, 500));
		jpMenu.setLayout(null);
	    add(jpMenu, "East");
	    JLabel jlb = new JLabel("当 前 执 子");
	    jpMenu.add(jlb);
	    jlb.setBounds(48, 80, 70, 20);
	    jpNowchess = new  JButton("");  
	    jpNowchess.setBackground(Color.BLACK);
	    jpNowchess.setBounds(55, 110, 50, 50);
	    jpMenu.add(jpNowchess);
	    
	    jpResult = new JLabel();
	    jpResult.setBounds(20, 160, 100, 50);
	    jpMenu.add(jpResult);
	    jpResult2 = new JLabel();
	    jpResult2.setBounds(20, 180, 100, 50);
	    jpMenu.add(jpResult2);
	    
	    JButton restart = new JButton("重新开始");
	    restart.setBounds(40, 350, 70, 30);
	    jpMenu.add(restart);
	    restart.addActionListener(restartListener);
	    
	    JButton bvb = new JButton("BVB");
	    bvb.setBounds(40, 250, 70, 30);
	    jpMenu.add(bvb);
	    bvb.addActionListener(bvbListener);
	    
	    JButton pass = new JButton("PASS");
	    pass.setBounds(40, 300, 70, 30);
	    jpMenu.add(pass);
	    pass.addActionListener(passListener);
	}
	/*
	 * 重置按钮
	 */
	ActionListener restartListener  = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			showDialog("游戏重新开始");
			resetBoard();
//			playColor = BLACK;
			playColor = WHITE;
			readMessage(player1.readMessage("BLACK"));
			endGame =true;
		}
	};
	/*
	 * pass
	 */
	ActionListener passListener  = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			sendMessage("NO");
		}
	};
	/*
	 * bvb
	 */
	ActionListener bvbListener  = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			MLog.i("游戏开始");
			new Thread(new Runnable() {
				@Override
				public void run() {
					bvb();
				}
			}).start();
			
		}
	};
	private boolean endGame;
	
	/**
	 * 默认BVB player1 白棋后手
	 */
	private void bvb(){
		endGame =false;
		player1.boardInit();
		player2.boardInit();
		IPlayer playerC = player2;
		 player1.readMessage("WHITE");
		 String message = player2.readMessage("BLACK");
		while(!endGame){
			
			MLog.i((playerC.isBlack()?"黑方：":"白方：")+message);
			if(!"NO".equals(message)){
				int x=message.charAt(0)-'1', y = message.charAt(1)-'A';
				if(!this.pieceLegalJudge(x, y, playerC.isBlack()?BLACK:WHITE, true)){
					MLog.i("数据异常");
				}
			}
			playerC= playerC==player1?player2:player1;
			jpNowchess.setBackground(playerC.isBlack()?Color.BLACK:Color.WHITE);
			int[] result = resultCount(board);
			jpResult.setText("黑方:"+result[0]+" 白方:"+result[1]);
			jpResult2.setText("棋子:"+(64-result[2])+" 剩:"+result[2]);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String message2 = playerC.readMessage(message);
			if("NO".equals(message)&&"NO".equals(message2)){
				endGame =true;
				MLog.i("player "+(playerC.isBlack()?"黑方：":"白方：")+message);
			}
			message = message2;
		}
		MLog.i("游戏结束");
		this.showResult();
	}
	/**
	 * @description 判断某个位置落某种颜色的棋子是否合法，同时能够选择性修改棋盘
	 * @param x 被判断位置的行坐标
	 * @param y 被判断位置的列坐标
	 * @param color 判断方颜色
	 * @param modifyOrNot 表示在判断过程中是否同时进行棋盘的修改
	 * @return 合法则返回true，否则返回false
	 */
	public boolean pieceLegalJudge(int x, int y, int color,boolean modifyOrNot){
		boolean flag = false;
		
		if(isInBounds(x,y) && SPACE==board[x][y]){//不越界且为空
			int xStep=0,yStep=0;
			for(int i=0; i<8; i++){//遍历八个方向，确定该位置是否合法
				switch(i)//首先确定方向
				{
					case 0 : {xStep=-1; yStep= 0;}; break;//上
					case 1 : {xStep=-1; yStep= 1;}; break;//右上
					case 2 : {xStep= 0; yStep= 1;}; break;//右
					case 3 : {xStep= 1; yStep= 1;}; break;//右下
					case 4 : {xStep= 1; yStep= 0;}; break;//下
					case 5 : {xStep= 1; yStep=-1;}; break;//左下
					case 6 : {xStep= 0; yStep=-1;}; break;//左
					case 7 : {xStep=-1; yStep=-1;}; break;//左上
					default:;
				}
				if(oneDirectionJudge(x,y,color,xStep,yStep,modifyOrNot)){
					flag = true;
				}
			}
		}
		if(flag && modifyOrNot){//如果合法且需要修改棋盘，则将这一点也修改
			board[x][y] = color;
			boardView[x+1][y+1].setBackground(color==WHITE?Color.WHITE:Color.BLACK);
		}
		return flag;
	}

	/**
	 * @description 判断某个位置的某个方向是否能够翻转对手棋子，同时能够选择性地修改棋盘
	 * @param x 基础棋点行坐标
	 * @param y 基础棋点列坐标
	 * @param color 判断方的棋色
	 * @param xStep 行坐标在行方向上的单元增量
	 * @param yStep 列坐标在列方向上的单元增量
	 * @param modifyOrNot 表示在判断过程中是否同时进行棋盘的修改
	 * @return 在此方向能够翻转对手棋子则返回true，否则返回false
	 */
	public boolean oneDirectionJudge(int x, int y, int color, int xStep, int yStep,boolean modifyOrNot){
		int xMv,yMv;
		xMv = x + xStep; yMv = y +yStep;
		while(true){
			if(!isInBounds(xMv,yMv)){//如果越界则停止，此位置不合法
				break;
			}
			else if(0 == board[xMv][yMv]){//如果为空则停止，此位置不合法
				break;
			}
			else if(color == board[xMv][yMv]){//如果同色
				if(xMv==x+xStep && yMv==y+yStep)  break; //如果是第一个就同色则停止，不合法	
				else{
					if(modifyOrNot) lineModify(x+xStep,y+yStep,xMv-xStep,yMv-yStep,color);
					return true;
				}
			}
			xMv += xStep; yMv += yStep;
		}
		return false;
	}
	

	/**
	 * @description 将一条线上的棋子修改为指定颜色
	 * @param xS 起始位置行坐标
	 * @param yS 起始位置列坐标
	 * @param xE 终止位置行坐标
	 * @param yE 终止位置列坐标
	 * @param color 所要修改成的颜色
	 */
	public void lineModify(int xS,int yS,int xE,int yE,int color){//给出要修改的起始和终止位置，以及要改成的颜色，这个函数可以完成修改一条线
		//动态确定xMv和yMv的增量
		int xMoveUnit = xS>xE ? -1	:
						xS<xE ? 1	: 0;
		int yMoveUnit = yS>yE ? -1	:
						yS<yE ? 1	: 0;
		
		int xMv = xS, yMv = yS;//起始位置为第一个要修改的位置
		while(board[xMv][yMv] != color){//当当前要修改的位置的颜色不是“终止颜色”时，循环继续
			board[xMv][yMv] = color;//先把当前位置的颜色修改了
			boardView[xMv+1][yMv+1].setBackground(color==WHITE?Color.WHITE:Color.BLACK);
			xMv += xMoveUnit;//位置指针移动到下一个位置
			yMv += yMoveUnit;
		}
		
	}
	
	public String changeCoordinateForm(int x, int y){
		String str="";
		str = Integer.toString(x+1) + (char)(y+'A');
		return str;
	}
	

	public boolean isInBounds(int x, int y){//是否在界限内部，true表示在内部，false表示出界
		
		if(x>=0&&x<8 && y>=0&&y<8){
			return true;
		}
		return false;
	}
	
	//点击事件
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		String[] xyStr = actionCommand.split("_");
		int x = Integer.valueOf(xyStr[0])-1;
		int y = Integer.valueOf(xyStr[1])-1;
		
		if(pieceLegalJudge(x, y, playColor, true)){
			board[x][y] = playColor;
			boardView[x+1][y+1].setBackground(playColor==WHITE?Color.WHITE:Color.BLACK);
			String outMessage = changeCoordinateForm(x, y);
			sendMessage(outMessage);
		}
		
	}

	public Component[][] getBoard() {
		return boardView;
	}

	public void setBoard(Component[][] board) {
		this.boardView = board;
	}
	/**
	 * 获取当前黑白棋数
	 * @return
	 */
	public int[] resultCount(int[][] board){
		int bCount =0;
		int wCount =0;
		int sCount =0;
		for(int i=0;i<8;i++){ 
			for(int j=0;j<8;j++){
				if(board[i][j] == BLACK){
					bCount++;
				}else if(board[i][j]==WHITE){
					wCount++;
				}else{
					sCount++;
				}
			}
		}
		return new int[]{bCount,wCount,sCount};
	}
	public void showResult(){
		int[] result = resultCount(board);
		String winner ="";
		int winnerColor=SPACE;
		String winnerPlayer="";
		if(result[0]>result[1]){
			winnerColor =BLACK;
			winner="黑方";
		}else{
			winnerColor =WHITE;
			winner ="白方";
		}
		if(winnerColor==BLACK&&player1.isBlack()||winnerColor!=BLACK&&!player1.isBlack()){
				winnerPlayer = player1.getClass().getSimpleName();
		}else if(player2!=null){
				winnerPlayer = player2.getClass().getSimpleName();
		}
		MLog.i("黑方："+result[0]+"，白方："+result[1]+"，胜利："+winner+" winnerPlayer:"+winnerPlayer);
		showDialog("黑方："+result[0]+"，白方："+result[1]+"，胜利："+winner);
	}
	
	/**
	 * 彈出框
	 * @param content
	 */
	public void showDialog(String content){
		/*JDialog dialog = new JDialog(this);
		dialog.setTitle("提示");
		dialog.getContentPane().add(new Label(content));
		dialog.setLocation((int)(this.getLocation().getX()+this.getWidth())/2, (int)(getLocation().getY()+getHeight())/2);
		dialog.setSize(200, 200);
		dialog.setVisible(true);*/
		JOptionPane.showMessageDialog(null, content);
	}

	public int getPlayColor() {
		return playColor;
	}

	public void setPlayColor(int playColor) {
		this.playColor = playColor;
	}
	
}
