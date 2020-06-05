package com.darkmind.partydrinkingcards;

public class Player {
    private String name;
    public static final int MAX_PLAYERS = 20;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
