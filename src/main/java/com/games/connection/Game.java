package com.games.connection;

import com.games.battleship.BattleshipGame;
import com.games.battleship.Direction;
import com.games.battleship.GameParams;
import com.user.User;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private final String gameId;
    private final User firstPlayer;
    private User secondPlayer;
    public BattleshipGame game;
    public final HashMap<String, Direction> direction;
    private boolean isFirstPlayerTurn;

    public Game(User firstPlayer, String gameId) {
        this.firstPlayer = firstPlayer;
        this.gameId = gameId;
        this.direction = new HashMap<>();
        direction.put("U", Direction.Left);
        direction.put("D", Direction.Right);
        direction.put("R", Direction.Down);
        direction.put("L", Direction.Up);
    }

    public void ConnectUser(User secondPlayer) {
        this.secondPlayer = secondPlayer;
        var params = new GameParams(3,2,1,0,6);
        this.game = new BattleshipGame(params, firstPlayer.getUserName(), this.secondPlayer.getUserName());
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
