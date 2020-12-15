package com.games.connection;

import com.games.battleship.BattleshipGame;
import com.games.battleship.Direction;
import com.games.battleship.GameParams;
import com.games.tic_tac_toe.TicTacToeGame;
import com.user.User;

import java.util.HashMap;

public class Game {
    private final User firstPlayer;
    private User secondPlayer;
    private String gameQueue;
    private TicTacToeGame ticTacToeGame;
    private BattleshipGame battleshipGame;
    public final HashMap<String, Direction> direction;
    private final GameType gameType;

    public Game(User firstPlayer, String gameType) {
        this.firstPlayer = firstPlayer;
        this.gameQueue = firstPlayer.getUserName();
        this.direction = new HashMap<>();
        direction.put("U", Direction.Left);
        direction.put("D", Direction.Right);
        direction.put("R", Direction.Down);
        direction.put("L", Direction.Up);

        if (gameType.toLowerCase().equals("battleship")) {
            this.gameType = GameType.BattleShip;
        } else if (gameType.toLowerCase().equals("tictactoe")) {
            this.gameType = GameType.TicTacToe;
        } else {
            this.gameType = GameType.Undefined;
        }
    }

    public void nextQueue(String username) {
        gameQueue = username;
    }

    public String getGameQueue() {
        return gameQueue;
    }

    //дописать создание игры в зависимости от типа игры
    public void ConnectUser(User secondPlayer) {
        this.secondPlayer = secondPlayer;
        if (gameType == GameType.BattleShip) {
            var params = new GameParams(3, 2, 1, 0, 6);
            this.battleshipGame = new BattleshipGame(params, firstPlayer.getUserName(),
                    this.secondPlayer.getUserName());
        } else if (gameType == GameType.TicTacToe) {
            this.ticTacToeGame = new TicTacToeGame(firstPlayer.getUserName(), secondPlayer.getUserName());
        }
    }

    public BattleshipGame getBSGame() {
        return battleshipGame;
    }

    public TicTacToeGame getTTTGame() {
        return ticTacToeGame;
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

    public GameType getGameType() {
        return gameType;
    }
}
