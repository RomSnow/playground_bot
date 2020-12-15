package com.games.gamehandlers;

import com.buttons.Buttons;
import com.games.battleship.CellType;
import com.games.battleship.Point;
import com.games.connection.Game;
import com.games.score_sheet_db.ScoreSheetConnector;
import com.games.tic_tac_toe.CharType;
import com.games.tic_tac_toe.GameIsEndException;
import com.phrases.Phrases;
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
                return Phrases.gameIsOver();
            }
            return Phrases.getWaitStr();
        }

        if (request.equals(Buttons.WHAT)) {
            return Phrases.getTTTInfo();
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
                return Phrases.getFaultInCommand();
            }
        }
    }

    private String setChar(String username, Game game, String request) throws SQLException {
        if (!username.equals(game.getGameQueue()))
            return Phrases.getNotYourQueue();
        var currentUser = registeredUsers.get(username);
        var gameId = currentUser.getGameId();
        var enemyUsername = startedGames.get(gameId).getEnemyName(currentUser);
        var ttt = game.getTTTGame();
        var horizontal = request.substring(3, 4);
        var vertical = request.substring(5, 6);
        try {
            ttt.setCharOnPosition(username, new Point(Integer.parseInt(vertical), Integer.parseInt(horizontal)));
        } catch (GameIsEndException e) {
            finishGame(gameId, username, enemyUsername);
            return Phrases.getBoom();
        }
        game.nextQueue(enemyUsername);
        tgBot.sendMessageToUser(registeredUsers.get(enemyUsername).getChatId(), "Твой ход!", false);
        return "Хороший ход!";
    }

    private String getMap(Game game) {
        StringBuilder result = new StringBuilder("- 0 1 2 \n");
        for (var i = 0; i < 3; i++) {
            result.append(i).append(" ");
            for (var j = 0; j < 3; j++) {
                var ttt = game.getTTTGame();
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
        return result.toString();
    }

    private void finishGame(String gameId, String winnerName, String loserName) throws SQLException {
        var winner = registeredUsers.get(winnerName);
        var loser = registeredUsers.get(loserName);
        winner.exitFromGame();
        loser.exitFromGame();
        ScoreSheetConnector.setPlayersScore(winnerName, 10);
        startedGames.remove(gameId);
        tgBot.sendMessageToUser(winner.getChatId(), "Победа!", false);
        tgBot.sendMessageToUser(loser.getChatId(), "Поражение.", false);
    }

    private String getDefaultAnswer() {
        return Phrases.getCommandIsntFound();
    }
}
