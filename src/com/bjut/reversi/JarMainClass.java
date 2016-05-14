package com.bjut.reversi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 参数入口类
 * @author liwei
 *
 */
public class JarMainClass {


	public static void main(String[] args){
		MLog.DEBUG = false;
		Player2 player = new Player2();
		String message = "";
		String myMessage = "NO";
		
		while(true){
			message =  getMessage();//获得一个消息
			 
			//处理完对方的情况，再处理自己的下一步棋的下法
			myMessage = player.readMessage(message);
			System.out.println(myMessage);//输出消息
		}//end while(true)
	}
	public static String getMessage(){
		String message = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try{
			message= br.readLine();
		}catch(IOException e){
			e.printStackTrace();//获得输入失败
		}
		return message;
	}
}
