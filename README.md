# Reversi
黑白棋

* Board.java 棋盘
* IPlayer.java 选手类接口
* Player.java  大赛自动基础AI，只能实现从左上检索
* Player2.java AI 自己实现的
* JarMainClass.java  打包程序主入口。
* MLog.java 		日志类
* MyMainTest.java  自己测试类
* TestPlatform.java 模拟大赛评判程序类，通过进程调用jar

输入输出的格式需要通过 System.in 和 System.out

打包时只需要 IPlayer ,Player2,MLog JarMainClass 四个类就可以，并且选定JarMainClass为主入口