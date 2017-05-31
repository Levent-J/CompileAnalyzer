package com.levent_j;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner in = new Scanner(System.in);
        while (true){
            System.out.println("输入 ： 1-词法分析  2-语法分析 3-结束");
            String input = in.nextLine();
            if (input.equals("1")){
                System.out.println("词法分析");
                LexcialAnalyzer.getsInstance().loadText().analyzer().outputText().outputError();
            }else if (input.equals("2")){
                System.out.println("语法分析");
            }else if (input.equals("3")){
                break;
            }else {
                System.out.println("错误输入");
            }
        }
    }
}
