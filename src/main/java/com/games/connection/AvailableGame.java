package com.games.connection;

import com.games.battleship.BattleshipGame;
import com.user.User;

public class AvailableGame {
    final String gameId;
    final User firstPlayer;
    private User secondPlayer;
    final BattleshipGame game;
    private boolean isFirstPlayerTurn;

    public AvailableGame(User firstPlayer, String gameId) {
        this.firstPlayer = firstPlayer;
        this.gameId = gameId;
        this.game = new BattleshipGame(10);
    }

    public void ConnectUser(User secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public boolean isFirstPlayerTurn() {
        return isFirstPlayerTurn;
    }

    public void setSecondPlayerTurn() {
        isFirstPlayerTurn = false;
    }

    public void setFirstPlayerTurn() {
        isFirstPlayerTurn = true;
    }

    public String getGameId() {
        return gameId;
    }

    public BattleshipGame getGame() {
        return game;
    }

    public Long getEnemyChatId(User you) {
        var yourChatId = you.getChatId();
        var firstPlayerChatId = firstPlayer.getChatId();
        var secondPlayerChatId = secondPlayer.getChatId();

        if (yourChatId.equals(firstPlayerChatId)) {
            return secondPlayerChatId;
        }
        else {
            return firstPlayerChatId;
        }
    }

    public String getEnemyName(User you) {
        var yourName = you.getUserName();
        var firstPlayerName = firstPlayer.getUserName();
        var secondPlayerName = secondPlayer.getUserName();

        if (yourName.equals(firstPlayerName)) {
            return secondPlayerName;
        }
        else {
            return firstPlayerName;
        }
    }

    public Long getFirstPlayerChatId() {
        return firstPlayer.getChatId();
    }

    public String getFirstPlayerName() {
        return firstPlayer.getUserName();
    }

    public Long getSecondPlayerChatId() {
        return secondPlayer.getChatId();
    }
}
