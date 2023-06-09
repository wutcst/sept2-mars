/**
 * 该类为语法分析器。
 * 从终端读入一行输入，并将其解析为命令，并据此创建Command类的对象。
 */
package com.example.sept2mars.entity;

import java.util.Scanner;

public class Parser
{
    private CommandWords commands;
    private Scanner reader;

    /**
     * 该类的无参构造器
     */
    public Parser()
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     *解析用户的输入
     * @return 根据命令创建的Command类的对象
     */
    public Command getCommand()
    {
        String inputLine;
        String word1 = null;
        String word2 = null;

        System.out.print("> ");

        //读入下一行输入
        inputLine = reader.nextLine();

        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next();   
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next();
            }
        }

        //判断输入是否为有效的
        if(commands.isCommand(word1)) {
            return new Command(word1, word2);
        }
        else {
            return new Command(null, word2);
        }
    }

    /**
     * 显示所有有效命令
     */
    public void showCommands()
    {
        commands.showAll();
    }

    /**
     * 只用于测试
     * @param aString
     */
    public Command testCommand(String aString)
    {
        String inputLine;   //保留完整的输入行
        String word1 = null;
        String word2 = null;
        //判断输入的前两个单词
        inputLine = aString;
        //将输入信息传递至tokenizer
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next();   //获取第一个单词
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next();   //获取第二个单词
            }
        }
        //判断前一个词汇是否为已知指令词,如果是，用以创建指令
        if(commands.isCommand(word1)) {
            System.out.println("当前获取的指令："+word1+"\t"+word2);
            return new Command(word1, word2);
        }
        else {
            System.out.println("当前获取的指令："+"空指令"+"\t"+word2);
            return new Command(null, word2);
        }
    }
}
