package com.games.battleship;

public class NotAllShipsSetException extends Exception{
    private String msg;
    public NotAllShipsSetException(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
