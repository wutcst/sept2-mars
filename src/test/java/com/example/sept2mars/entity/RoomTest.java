package com.example.sept2mars.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void getShortDescription() {
        Room outside = new Room("outside the main entrance of the university");
        System.out.println(outside.getShortDescription());
    }

    @Test
    void getLongDescription() {
        Room outside = new Room("outside the main entrance of the university");
        System.out.println(outside.getLongDescription());
    }

    @Test
    void getExit() {
        Room outside = new Room("outside the main entrance of the university");
        Room theater = new Room("in a lecture theater");
        outside.setExit("west",theater);
        System.out.println(outside.getExit("west"));
    }
}