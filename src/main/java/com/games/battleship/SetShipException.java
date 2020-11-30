package com.games.battleship;

public class SetShipException extends Exception{
    private final String msg;
    public SetShipException(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
