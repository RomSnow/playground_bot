package com.games.connection;

public class TTTConnGameFactory implements ConnectGameFactory{
    @Override
    public ConnectGame createConnectGame() {
        return new TTTConnGame();
    }
}
