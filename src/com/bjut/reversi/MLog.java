package com.bjut.reversi;

/**
 * 日志
 * @author liwei
 *
 */
public class MLog {
	
//    public static final boolean DEBUG = true;//日志开关
    public static  boolean DEBUG = false;//日志开关
	
	public static void i(String s){
		if(DEBUG)
		System.out.println(s);
	}
}
