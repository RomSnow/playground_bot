package com.games.connection;

public class AvailableGame {
    final String gameId;
    final Long firstPlayerChatId;
    private Long secondPlayerChatId;
    final String game;

    public AvailableGame(Long firstPlayerChatId, String gameId, String game) {
        this.firstPlayerChatId = firstPlayerChatId;
        this.gameId = gameId;
        this.game = game;
    }

    public void ConnectUser(Long secondPlayerChatId) {
        this.secondPlayerChatId = secondPlayerChatId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getGame() {
        return game;
    }

    public Long getFirstPlayerChatId() {
        return firstPlayerChatId;
    }

    public Long getSecondPlayerChatId() {
        return secondPlayerChatId;
    }
}
