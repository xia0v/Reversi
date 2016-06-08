package com.bjut.reversi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 模拟大赛的评判工具
 * @author icmonkey
 *
 */
public class TestPlatform {

	public static void main(String[] args) {
		try{
		String path1= "E:\\项目\\黑白棋\\10005-李伟_v1.4.jar";//测试程序路径
		String path2= "E:\\项目\\黑白棋\\测试程序及使用说明\\测试程序及使用说明\\FoolPlayer.jar";//测试程序路径
		Runtime runtime = Runtime.getRuntime();  
		Process process = runtime.exec("java -jar "+path1);
		//取得命令结果的输出流 
		InputStream fis = process.getInputStream(); 
		//用一个读输出流类去读    
		BufferedReader br1 = new BufferedReader(new InputStreamReader(fis));   
		String message = "BLACK";
		BufferedWriter fout1 = new BufferedWriter(new OutputStreamWriter(process.getOutputStream())); 
		writeMessage(fout1, message);
		
		runtime = Runtime.getRuntime();  
		process = runtime.exec("java -jar "+path2);
		//取得命令结果的输出流 
		fis = process.getInputStream(); 
		//用一个读输出流类去读    
		BufferedReader br2 = new BufferedReader(new InputStreamReader(fis));   
		message = "WHITE";
		BufferedWriter fout2 = new BufferedWriter(new OutputStreamWriter(process.getOutputStream())); 
		writeMessage(fout2, message);
		br2.readLine();
		BufferedReader tBr = br1;
		BufferedWriter tFout = fout2;
		String line;
		String lastLine="";
		boolean black = true;
		Board board  = new Board();
		while(true){
			//逐行读取输出到控制台    
			
			 line = tBr.readLine();
				 System.out.println((black?"黑：":"白：")+line); 
			 if(!"NO".equals(line)){
			 int x=line.charAt(0)-'1', y = line.charAt(1)-'A';
				 if(!board.pieceLegalJudge(x, y, black?IPlayer.BLACK:IPlayer.WHITE, true)){
					 System.out.println("不符合"); 
					 break;
				 }
			 }else{
				 boolean flag = false;
				 for(int i=0;i<8;i++){
						for(int j=0;j<8;j++){
							if(board.pieceLegalJudge(i, j, black?IPlayer.BLACK:IPlayer.WHITE, false)){
								flag = true;
								System.out.println("可用点："+board.changeCoordinateForm(i, j)); 
							}
						}
					}
				 if(flag){
					 System.out.println("不符合"); 
					 break; 
				 }
			 }
			 if("NO".equals(lastLine)&&"NO".equals(line))break;
			 writeMessage(tFout,line);
			 tBr = tBr==br1?br2:br1;
			 tFout = tFout == fout1?fout2:fout1;
			 black = !black;
			 lastLine = line;
			 Thread.sleep(500);
		}
		board.showResult();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static void writeMessage(BufferedWriter fout,String s){
		try {
			fout.write(s);
			fout.newLine();
			fout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
