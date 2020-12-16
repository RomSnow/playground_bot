package com.games.gamehandlers;

import com.buttons.Buttons;
import com.games.battleship.Point;
import com.games.connection.Game;
import com.games.score_sheet_db.ScoreSheetConnector;
import com.games.tic_tac_toe.CharType;
import com.games.tic_tac_toe.GameIsEndException;
import com.games.tic_tac_toe.TicTacToeGame;
import com.phrases.MainPhrases;
import com.phrases.TTTPhrases;
import com.playgroundbot.PlaygroundBot;
import com.user.User;

import java.sql.SQLException;
import java.util.HashMap;

public class TTTGameHandler {
    private final HashMap<String, Game> availableGames;
    private final HashMap<String, Game> startedGames;
    private final HashMap<String, User> registeredUsers;
    private final PlaygroundBot tgBot;

    public TTTGameHandler(HashMap<String, Game> availableGames,
                          HashMap<String, Game> startedGames, HashMap<String, User> registeredUsers,
                          PlaygroundBot main) {
        this.availableGames = availableGames;
        this.startedGames = startedGames;
        this.registeredUsers = registeredUsers;
        this.tgBot = main;
    }

    public String handleGame(String request, String username) throws SQLException {
        var currentUser = registeredUsers.get(username);
        var gameId = currentUser.getGameId();
        var game = startedGames.get(gameId);
        if (availableGames.containsKey(gameId)) {
            if (request.equals(Buttons.CANCEL)) {
                currentUser.exitFromGame();
                availableGames.remove(gameId);
                return MainPhrases.gameIsOver();
            }
            return MainPhrases.getWaitStr();
        }

        if (request.equals(Buttons.WHAT)) {
            return TTTPhrases.getInfo();
        }
        else {
            try {
                var command = request.substring(0, 2);
                return switch (command) {
                    case "-s" -> setChar(username, game, request);
                    case "-m" -> getMap(game);
                    default -> getDefaultAnswer();
                };
            } catch (StringIndexOutOfBoundsException e) {
                return MainPhrases.getFaultInCommand();
            }
        }
    }

    private String setChar(String username, Game game, String request) throws SQLException {
        if (!username.equals(game.getGameQueue()))
            return MainPhrases.getNotYourQueue();
        var currentUser = registeredUsers.get(username);
        var gameId = currentUser.getGameId();
        var enemyUsername = startedGames.get(gameId).getEnemyName(currentUser);
        var ttt = (TicTacToeGame) game.getGame();
        var horizontal = request.substring(3, 4);
        var vertical = request.substring(5, 6);
        var type = ttt.getCharTypeOnPosition(new Point(Integer.parseInt(vertical), Integer.parseInt(horizontal)));
        if (type != CharType.Empty) {
            return TTTPhrases.fieldAlready();
        }
        try {
            ttt.setCharOnPosition(username, new Point(Integer.parseInt(vertical), Integer.parseInt(horizontal)));
        } catch (GameIsEndException e) {
            finishGame(gameId, username, enemyUsername);
            return MainPhrases.getBoom();
        }
        game.nextQueue(enemyUsername);
        tgBot.sendMessageToUser(registeredUsers.get(enemyUsername).getChatId(), TTTPhrases.nextTurn(), false);
        return TTTPhrases.endTurn();
    }

    private String getMap(Game game) {
        StringBuilder result = new StringBuilder("- 0 1 2 \n");
        for (var i = 0; i < 3; i++) {
            result.append(i).append(" ");
            for (var j = 0; j < 3; j++) {
                var ttt = (TicTacToeGame) game.getGame();
                var type = ttt.getCharTypeOnPosition(new Point(i, j));
                if (type == CharType.Empty) {
                    result.append("  ");
                } else if (type == CharType.X) {
                    result.append("X ");
                } else if (type == CharType.O) {
                    result.append("O ");
                }
            }
            result.append("\n");
        }
        return TTTPhrases.getMap(result.toString());
    }

    private void finishGame(String gameId, String winnerName, String loserName) throws SQLException {
        var winner = registeredUsers.get(winnerName);
        var loser = registeredUsers.get(loserName);
        winner.exitFromGame();
        loser.exitFromGame();
        ScoreSheetConnector.setPlayersScore(winnerName, 10);
        startedGames.remove(gameId);
        tgBot.sendMessageToUser(winner.getChatId(), MainPhrases.getWin(), false);
        tgBot.sendMessageToUser(loser.getChatId(), MainPhrases.getLose(), false);
    }

    private String getDefaultAnswer() {
        return MainPhrases.getCommandIsntFound();
    }
}
