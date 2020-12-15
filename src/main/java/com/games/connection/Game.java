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
    private ConnectGameFactory connectGameFactory;
    private ConnectGame connectGame;

    public Game(User firstPlayer, String gameType) {
        this.firstPlayer = firstPlayer;
        this.gameQueue = firstPlayer.getUserName();
        this.direction = new HashMap<>();
        direction.put("U", Direction.Left);
        direction.put("D", Direction.Right);
        direction.put("R", Direction.Down);
        direction.put("L", Direction.Up);

        connectGameFactory = switch (gameType.toLowerCase()) {
            case "battleship" -> new BSConnGameFactory();
            case "tictactoe" -> new TTTConnGameFactory();
            default -> throw new IllegalStateException("Unexpected gameType: " + gameType.toLowerCase());
        };
    }

    public void nextQueue(String username) {
        gameQueue = username;
    }

    public String getGameQueue() {
        return gameQueue;
    }

    public void connectUser(User secondPlayer) {
        this.secondPlayer = secondPlayer;
        connectGame = connectGameFactory.createConnectGame();
        connectGame.connectUser(firstPlayer, secondPlayer);
    }

    public BattleshipGame getBSGame() {
        return (BattleshipGame) connectGame.getGame();
    }

    public TicTacToeGame getTTTGame() {
        return (TicTacToeGame) connectGame.getGame();
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
        return connectGame.getType();
    }
}
