package com.games.connection;

import com.games.battleship.BattleshipGame;
import com.games.battleship.Direction;
import com.games.battleship.GameParams;
import com.user.User;

import java.util.HashMap;

public class Game {
    private final User firstPlayer;
    private User secondPlayer;
    private String gameQueue;
    private BattleshipGame game;
    public final HashMap<String, Direction> direction;

    public Game(User firstPlayer) {
        this.firstPlayer = firstPlayer;
        this.gameQueue = firstPlayer.getUserName();
        this.direction = new HashMap<>();
        direction.put("U", Direction.Left);
        direction.put("D", Direction.Right);
        direction.put("R", Direction.Down);
        direction.put("L", Direction.Up);
    }

    public void nextQueue(String username) {
        gameQueue = username;
    }

    public String getGameQueue() {
        return gameQueue;
    }

    public void ConnectUser(User secondPlayer) {
        this.secondPlayer = secondPlayer;
        var params = new GameParams(3,2,1,0,6);
        this.game = new BattleshipGame(params, firstPlayer.getUserName(), this.secondPlayer.getUserName());
    }

    public BattleshipGame getGame() {
        return game;
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
}
