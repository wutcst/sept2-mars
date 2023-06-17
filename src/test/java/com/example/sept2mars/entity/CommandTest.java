package com.example.sept2mars.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void isUnknown() {
        Command commandWord=new Command("go","west");
        System.out.println(commandWord.isUnknown()); //false
        Command commandWord1=new Command(null,"west");
        System.out.println(commandWord1.isUnknown()); //true
    }

    @Test
    void hasSecondWord() {
        Command commandWord=new Command("go","west");
        System.out.println(commandWord.hasSecondWord()); //true
        Command commandWord1=new Command("go",null);
        System.out.println(commandWord1.hasSecondWord()); //false
    }
}