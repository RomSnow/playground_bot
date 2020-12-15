package com.games.connection;

public class GameDidntRight extends Exception {
    private final String msg;
    public GameDidntRight(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
