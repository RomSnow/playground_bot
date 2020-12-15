package com.games.connection;

public class BSConnGameFactory implements ConnectGameFactory{
    @Override
    public ConnectGame createConnectGame() {
        return new BSConnGame();
    }
}
