package com.games.connection;

public class GameInfo {
    private GameType gameType;
    private String gameId;

    public GameInfo(GameType type, String id) {
        gameType = type;
        gameId = id;
    }

    public String getGameId() {
        return gameId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameId(String id) {
        gameId = id;
    }

    public void setGameType(GameType type) {
        gameType = type;
    }
}
