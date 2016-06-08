package com.bjut.reversi;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * AI程序
 * 
 * @author liwei
 *
 */
public class Player6 implements IPlayer {

	private int board[][] = new int[8][8];// 所有棋子
	private ArrayList<int[]> check = new ArrayList<int[]>();// 可下棋子
	private int myColor = WHITE;

	private int[][] cellpoints = {
			{ 100, -5, 10, 5, 5, 10, -5, 100 },
			{ -5, -45, 1, 1, 1, 1, -45, -5 },
			{ 10, 1, 3, 2, 2, 3, 1, 10 },
			{ 5, 1, 2, 1, 1, 2, 1, 5 },
			{ 5, 1, 2, 1, 1, 2, 1, 5 },
			{ 10, 1, 3, 2, 2, 3, 1, 10 },
			{ -5, -45, 1, 1, 1, 1, -45, -5 },
			{ 100, -5, 10, 5, 5, 10, -5, 100 } };

	private int INF = 10000000;

	public Player6() {
		boardInit();
	}

	@Override
	public String readMessage(String message) {
		String myMessage = "NO";

		if (message.equals("BLACK")) {// 如果是BLACK，则表示是开局执黑
			this.myColor = BLACK;
		} else if (message.equals("WHITE")) {// 如果是WHITE，则表示是开局执白
			this.myColor = WHITE;
			return "NO";
		} else if (message.equals("NO")) {
			// 对方上一步无棋可走
		} else {// 普通坐标
			int xOpp = message.charAt(0) - '1', yOpp = message.charAt(1) - 'A';
			this.pieceLegalJudge(xOpp, yOpp, -myColor, true);// 判断对方，处理棋盘
		}

		// 处理完对方的情况，再处理自己的下一步棋的下法
		myMessage = botJudge();
		return myMessage;
	}

	@Override
	public boolean isBlack() {
		return myColor == BLACK;
	}

	@Override
	public void setIsBlack(boolean isBlack) {
		myColor = (isBlack ? BLACK : WHITE);
	}

	@Override
	public void boardInit() {
		myColor = WHITE;
		for (int i = 0; i < 8; i++) {// 棋盘初始化，0表示空，1表示白，-1表示黑
			for (int j = 0; j < 8; j++) {
				board[i][j] = SPACE;
			}
		}
		board[4][4] = WHITE;
		board[3][3] = WHITE;
		board[4][3] = BLACK;
		board[3][4] = BLACK;
	}

	public boolean pieceLegalJudge(int x, int y, int color, boolean modifyOrNot) {
		return pieceLegalJudge(x, y, color, modifyOrNot, board);
	}

	/**
	 * @description 判断某个位置落某种颜色的棋子是否合法，同时能够选择性修改棋盘
	 * @param x
	 *            被判断位置的行坐标
	 * @param y
	 *            被判断位置的列坐标
	 * @param color
	 *            判断方颜色
	 * @param modifyOrNot
	 *            表示在判断过程中是否同时进行棋盘的修改
	 * @param board
	 * @return 合法则返回true，否则返回false
	 */
	public boolean pieceLegalJudge(int x, int y, int color, boolean modifyOrNot, int[][] board) {
		boolean flag = false;
		if (isInBounds(x, y) && SPACE == board[x][y]) {// 不越界且为空
			int xStep = 0, yStep = 0;
			for (int i = 0; i < 8; i++) {// 遍历八个方向，确定该位置是否合法
				switch (i)// 首先确定方向
				{
				case 0: {
					xStep = -1;
					yStep = 0;
				}
					;
					break;// 上
				case 1: {
					xStep = -1;
					yStep = 1;
				}
					;
					break;// 右上
				case 2: {
					xStep = 0;
					yStep = 1;
				}
					;
					break;// 右
				case 3: {
					xStep = 1;
					yStep = 1;
				}
					;
					break;// 右下
				case 4: {
					xStep = 1;
					yStep = 0;
				}
					;
					break;// 下
				case 5: {
					xStep = 1;
					yStep = -1;
				}
					;
					break;// 左下
				case 6: {
					xStep = 0;
					yStep = -1;
				}
					;
					break;// 左
				case 7: {
					xStep = -1;
					yStep = -1;
				}
					;
					break;// 左上
				default:
					;
				}
				if (oneDirectionJudge(x, y, color, xStep, yStep, modifyOrNot, board)) {
					flag = true;
				}
			}
		}
		if (flag && modifyOrNot) {// 如果合法且需要修改棋盘，则将这一点也修改
			board[x][y] = color;
		}
		return flag;
	}

	/**
	 * @description 判断某个位置的某个方向是否能够翻转对手棋子，同时能够选择性地修改棋盘
	 * @param x
	 *            基础棋点行坐标
	 * @param y
	 *            基础棋点列坐标
	 * @param color
	 *            判断方的棋色
	 * @param xStep
	 *            行坐标在行方向上的单元增量
	 * @param yStep
	 *            列坐标在列方向上的单元增量
	 * @param modifyOrNot
	 *            表示在判断过程中是否同时进行棋盘的修改
	 * @param board
	 * @return 在此方向能够翻转对手棋子则返回true，否则返回false
	 */
	public boolean oneDirectionJudge(int x, int y, int color, int xStep, int yStep, boolean modifyOrNot,
			int[][] board) {
		int xMv, yMv;
		xMv = x + xStep;
		yMv = y + yStep;
		while (true) {
			if (!isInBounds(xMv, yMv)) {// 如果越界则停止，此位置不合法
				break;
			} else if (SPACE == board[xMv][yMv]) {// 如果为空则停止，此位置不合法
				break;
			} else if (color == board[xMv][yMv]) {// 如果同色
				if (xMv == x + xStep && yMv == y + yStep)
					break; // 如果是第一个就同色则停止，不合法
				else {
					if (modifyOrNot)
						lineModify(x + xStep, y + yStep, xMv - xStep, yMv - yStep, color, board);
					return true;
				}
			}
			xMv += xStep;
			yMv += yStep;
		}
		return false;
	}

	/**
	 * @description 将一条线上的棋子修改为指定颜色
	 * @param xS
	 *            起始位置行坐标
	 * @param yS
	 *            起始位置列坐标
	 * @param xE
	 *            终止位置行坐标
	 * @param yE
	 *            终止位置列坐标
	 * @param color
	 *            所要修改成的颜色
	 * @param board
	 */
	public void lineModify(int xS, int yS, int xE, int yE, int color, int[][] board) {// 给出要修改的起始和终止位置，以及要改成的颜色，这个函数可以完成修改一条线
		// 动态确定xMv和yMv的增量
		int xMoveUnit = xS > xE ? -1 : xS < xE ? 1 : 0;
		int yMoveUnit = yS > yE ? -1 : yS < yE ? 1 : 0;

		int xMv = xS, yMv = yS;// 起始位置为第一个要修改的位置
		while (board[xMv][yMv] != color) {// 当当前要修改的位置的颜色不是“终止颜色”时，循环继续
			board[xMv][yMv] = color;// 先把当前位置的颜色修改了
			xMv += xMoveUnit;// 位置指针移动到下一个位置
			yMv += yMoveUnit;
		}

	}

	public String changeCoordinateForm(int x, int y) {
		String str = "";
		str = Integer.toString(x + 1) + (char) (y + 'A');
		return str;
	}

	public boolean isInBounds(int x, int y) {// 是否在界限内部，true表示在内部，false表示出界

		if (x >= 0 && x < 8 && y >= 0 && y < 8) {
			return true;
		}
		return false;
	}

	/**
	 * AI判断
	 * 
	 * @return
	 */
	public String botJudge() {
		String myMessage = "NO";
		int x = -1, y = -1;
		int maxValue = -this.INF;
		int a = -this.INF;
		int b = this.INF;

		check = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (pieceLegalJudge(i, j, myColor, false)) {
					check.add(new int[] { i, j });
				}
		int branches = check.size();
		for (int i = 0; i < branches; i++) {
			int[][] tmpBoard = new int[8][8];
			for (int j = 0; j < 8; j++) {
				for (int k = 0; k < 8; k++)
					tmpBoard[j][k] = board[j][k];
			}
			pieceLegalJudge(check.get(i)[0], check.get(i)[1], myColor, true, tmpBoard);
			int temp = dfs(-myColor, INF, tmpBoard, false, a, b, myColor);
//			 MLog.i("1branches:"+i+" ans="+temp+" x:"+check.get(i)[0]+" y:"+check.get(i)[1]+" color:"+(myColor==BLACK?"黑":"白"));
			if (temp > a)
				a = temp;
			if (temp <= maxValue)
				continue;
			maxValue = temp;
			x = check.get(i)[0];
			y = check.get(i)[1];
		}

		if (branches != 0 && x != -1 && y != -1) {
			pieceLegalJudge(x, y, myColor, true);
			myMessage = this.changeCoordinateForm(x, y);
			// MLog.i("bot_judge= "+x + " " + y);
		}
		return myMessage;
	}

	/**
	 * 关键方法<br>
	 * 判断所下棋的质量<br>
	 * 根据公式 ans = p1 * a1 + p2 * a2;<br>
	 * p1 = 3; p2 = 7;<br>
	 * a1 每个位置的权值和<br>
	 * a2 边缘子的数量<br>
	 * 
	 * @param color
	 *            下棋颜色
	 * @param branches
	 *            分支数
	 * @param chessboard
	 *            棋盘
	 * @param stop
	 *            是否跳过
	 * @param a
	 *            最小值
	 * @param b
	 *            最大值
	 * @param fa
	 *            下步的颜色
	 * @return 估值
	 */
	public int dfs(int color, int branches, int[][] chessboard, boolean stop, int a, int b, int fa) {
		if (branches == 0) {// 分支数为0 根据 公式计算 估值
			int a1 = 0;// 每个位置的权值和，权重
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (chessboard[i][j] == myColor)
						a1 += this.cellpoints[i][j];
					else if (chessboard[i][j] == -myColor) {
						a1 -= this.cellpoints[i][j];
					}
				}
			}
			int[] bwCount = resultCount(chessboard);
			int a2 = judgeStatic(chessboard);// 边缘子的数量，行动力
			int a3 = myColor==BLACK?bwCount[0]:bwCount[1];//棋子数
//			int p1 = 3;//权重
//			int p2 = 7;//行动力
			int p1 = 128/ (Math.abs(40 - bwCount[2])+1)+10;//权重
			int p2 = 64-p1+60;//行动力
			int p3 = 64/ (64 - bwCount[2]+1);//棋子数
			int ans = p1 * a1 + p2 * a2 + p3*a3;
//			MLog.i(this.getClass().getSimpleName()+" branches "+a1+"*"+p1+" + "+a2+"*"+p2+"="+ans);
			return ans;
		}
		// 剪枝算法
		int min = this.INF;
		int max = -this.INF;

		ArrayList<int[]> check = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieceLegalJudge(i, j, color, false, chessboard)) {
					check.add(new int[] { i, j });
				}
			}
		}
		int rear = check.size();// 可以下的棋数
		for (int i = 0; i < rear; i++) {
			int[][] tmpBoard = new int[8][8];
			for (int j = 0; j < 8; j++) {
				for (int k = 0; k < 8; k++)
					tmpBoard[j][k] = chessboard[j][k];
			}
			pieceLegalJudge(check.get(i)[0], check.get(i)[1], color, true, tmpBoard);
			int temp = dfs(-color, branches / rear, tmpBoard, false, a, b, color);
			if (fa == myColor) {
				if (color == -myColor) {
					if (temp < b) {
						if (temp <= a)
							return temp;
						b = temp;
					}
					if (temp < min)
						min = temp;
				} else {
					if (color != myColor)
						continue;
					if (temp > a) {
						if (temp > b)
							b = temp;
						a = temp;
					}
					if (temp > max)
						max = temp;
				}
			} else {
				if (fa != -myColor)
					continue;
				if (color == myColor) {
					if (temp > a) {
						if (temp >= b)
							return temp;
						a = temp;
					}
					if (temp > max)
						max = temp;
				} else {
					if (color != -myColor)
						continue;
					if (temp < b) {
						if (temp < a)
							a = temp;
						b = temp;
					}
					if (temp < min) {
						min = temp;
					}
				}
			}
		}
		// 如果无棋可下
		if (rear == 0) {
			// 判断对方的棋
			if (!stop) {
				return dfs(fa, branches, chessboard, true, a, b, fa);
			}
			// 无棋可下时，判断双方棋子数
			int bot_chess = 0;
			int player_chess = 0; 
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					if (chessboard[i][j] == myColor)
						bot_chess++;
					else if (chessboard[i][j] == -myColor)
						player_chess++;
				}
			if (bot_chess > player_chess) {
				return this.INF / 10;
			} else {
				return -this.INF / 10;
			}
		}

		// 如果自己的棋取最大，对方棋取最小估值
		int ans;
		if (color == -myColor)
			ans = min;
		else
			ans = max;
		return ans;
	}

	/**
	 * 判断局势<br/>
	 * 根据自己棋上下左右是否可下棋减，对方棋上下左右是否可下棋加<br>
	 * 此处是否可优化为 周围8个位置都判断，因为斜角也是可以下棋
	 * 
	 * @param chessboard
	 * @return 返回局势
	 */
	public int judgeStatic(int[][] chessboard) {
		int ans = 0;
		int xStep = 0;
		int yStep = 0;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chessboard[i][j] == myColor||chessboard[i][j] == -myColor)
				for (int k = 0; k < 8; k++) {// 遍历八个方向，确定该位置是否合法
					switch (k)// 首先确定方向
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
					if (isInBounds(i + xStep, j + yStep) && chessboard[i + xStep][j + yStep] == SPACE) {
						if (chessboard[i][j] == myColor)
							ans--;
						else
							ans++;
					}
				}
		return ans;
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
}
