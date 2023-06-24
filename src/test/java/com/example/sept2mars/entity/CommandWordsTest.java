package com.example.sept2mars.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandWordsTest {

    @Test
    void isCommand() {
        String aString=new String("这是一段字符");
        String bSTring=new String("go");
        CommandWords commandWords=new CommandWords();
        System.out.println(commandWords.isCommand(aString)); //应当返回false
        System.out.println(commandWords.isCommand(bSTring)); //应当返回true
    }

    @Test
    void showAll() {
        CommandWords commandWords=new CommandWords();
        commandWords.showAll(); //应当输出所有已知指令
    }
}