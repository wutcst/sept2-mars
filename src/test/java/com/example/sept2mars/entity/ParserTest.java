package com.example.sept2mars.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void showCommands() {
        Parser parser=new Parser();
        parser.showCommands();
    }

    @Test
    void getCommand(){
        Parser parser=new Parser();
        boolean flag=false;
        Command command=parser.testCommand("go west"); //应当输出完整的指令
        Command command1=parser.testCommand("go");
        Command command2=parser.testCommand("make west"); //应当输出空指令+第二个词汇
    }
}