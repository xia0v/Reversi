package com.bjut.reversi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Player{
	
	public boolean amIBlack;
	int board[][];
	
	public Player(){
		board = new int[9][9];//9 X 9数组，含有8 X 8棋盘
		boardInit();
	}
		
	public String readMessage(String message){
		String myMessage = "NO";
		
			if(message.equals("BLACK")){//如果是BLACK，则表示是开局执黑
				this.amIBlack = true;
			}
			else if(message.equals("WHITE")){//如果是WHITE，则表示是开局执白
				this.amIBlack = false;
				return "NO";
			}
			else if(message.equals("NO")){
				//对方上一步无棋可走
			}
			else{//普通坐标
				int xOpp=message.charAt(0)-'1'+1, yOpp = message.charAt(1)-'A'+1;
				if(this.amIBlack){
					//this.modifyBoard(xOpp, yOpp, 1);
					this.pieceLegalJudge(xOpp, yOpp, 1, true);
				}
				else{
					//this.modifyBoard(xOpp, yOpp, -1);
					this.pieceLegalJudge(xOpp, yOpp, -1, true);
				}
			}
			
			//处理完对方的情况，再处理自己的下一步棋的下法
			myMessage = "NO";
			boolean flag = true;
			for(int x=1; x<=8 && flag; x++){//这里采用最简单策略，即遍历，遇到第一个合法落棋位置则输出
				for(int y=1; y<=8 && flag; y++){
					if(this.amIBlack){
						if(this.pieceLegalJudge(x, y, -1, true)){//当前位置合法
							flag = false;
							//this.modifyBoard(x, y, -1);//下一颗黑子
							myMessage = this.changeCoordinateForm(x,y);
						}
					}
					else{
						if(this.pieceLegalJudge(x, y, 1, true)){//当前位置合法
							flag = false;
							//this.modifyBoard(x, y, 1);//下一颗白子
							myMessage = this.changeCoordinateForm(x,y);
						}
					}
				}
			}
			return myMessage;
	}
	/**
	 * 输出消息
	 * @param message
	 */
	public void sendMessage(String message){
	/*	try {
			System.in.read((message+"\n").getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println(message);//输出消息
	}
	
	public String getMessage(){
		String message = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try{
			message= br.readLine();
		}catch(IOException e){
			e.printStackTrace();//获得输入失败
		}
		return message;
	}

	public void boardInit(){
		amIBlack = false;
		for(int i=0;i<9;i++){//棋盘初始化，0表示空，1表示白，-1表示黑
			for(int j=0;j<9;j++){
				board[i][j] = 0;
			}
		}
		board[4][4] = 1;
		board[5][5] = 1;
		board[4][5] = -1;
		board[5][4] = -1;
	}

	/**
	 * @description 判断当前棋盘是否能够继续
	 * @return 能够继续返回true，否则返回false
	 */
	public boolean contJudge(){
		return (oneColorContJudge(-1) || oneColorContJudge(1));
	}
	
	/**
	 * @description 判断某方是否能够继续
	 * @param color 所要判断方的棋子颜色，1表示白方，-1表示黑方
	 * @return 能够继续返回true，否则返回false
	 */
	public boolean oneColorContJudge(int color){//判断某一个颜色在当前状态下是否能继续,返回true表示能继续，false表示不能继续	
		for(int x=1; x<=8; x++){
			for(int y=1; y<=8; y++){
				if(pieceLegalJudge(x,y,color,false)){
					return true;
				}
			}
		}
		return false;
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
	 * @description 判断某个位置落某种颜色的棋子是否合法，同时能够选择性修改棋盘
	 * @param x 被判断位置的行坐标
	 * @param y 被判断位置的列坐标
	 * @param color 判断方颜色
	 * @param modifyOrNot 表示在判断过程中是否同时进行棋盘的修改
	 * @return 合法则返回true，否则返回false
	 */
	public boolean pieceLegalJudge(int x, int y, int color,boolean modifyOrNot){
		boolean flag = false;
		if(isInBounds(x,y) && 0==board[x][y]){//不越界且为空
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
		}
		return flag;
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
			xMv += xMoveUnit;//位置指针移动到下一个位置
			yMv += yMoveUnit;
		}
		
	}
	
	
	public int getNumberOfOneColor(int color){
		int result = 0;
		for(int x=1; x<=8; x++){
			for(int y=1; y<=8; y++){
				if(color == board[x][y]){
					result++;
				}
			}
		}	
		return result;
	}
	
	
	public String changeCoordinateForm(int x, int y){
		String str="";
		str = Integer.toString(x) + (char)(y+'A'-1);
		return str;
	}
	

	public boolean isInBounds(int x, int y){//是否在界限内部，true表示在内部，false表示出界
		
		if(x>=1&&x<=8 && y>=1&&y<=8){
			return true;
		}
		return false;
	}

}
