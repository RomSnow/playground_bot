package com.games.gamehandlers;

import com.buttons.Buttons;
import com.games.battleship.*;
import com.games.connection.Game;
import com.games.score_sheet_db.ScoreSheetConnector;
import com.phrases.BSPhrases;
import com.phrases.MainPhrases;
import com.user.User;
import com.playgroundbot.PlaygroundBot;

import java.sql.SQLException;
import java.util.HashMap;

public class BSGameHandler {
    private final HashMap<String, Game> availableGames;
    private final HashMap<String, Game> startedGames;
    private final HashMap<String, User> registeredUsers;
    private final PlaygroundBot tgBot;

    public BSGameHandler(HashMap<String, Game> availableGames,
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
                tgBot.remAvailableGame(gameId);
                return MainPhrases.gameIsOver();
            }
            return MainPhrases.getWaitStr();
        }

        if (request.equals(Buttons.WHAT)) {
            return BSPhrases.getInfo();
        }
        else {
            try {
                var command = request.substring(0, 2);
                return switch (command) {
                    case "-f" -> makeHit(username, game, request);
                    case "-s" -> setShip(username, game, request);
                    case "-r" -> surrender(username);
                    case "-m" -> getMap(username, game);
                    default -> getDefaultAnswer();
                };
            } catch (StringIndexOutOfBoundsException e) {
                return MainPhrases.getFaultInCommand();
            }
        }
    }

    private String makeHit(String username, Game game, String request) throws SQLException {
        try {
            if (!username.equals(game.getGameQueue()))
                return MainPhrases.getNotYourQueue();
            var currentUser = registeredUsers.get(username);
            var gameId = currentUser.getGameId();
            var enemyUsername = startedGames.get(gameId).getEnemyName(currentUser);
            var horizontal = request.substring(3, 4);
            var vertical = request.substring(5, 6);
            var bsGame = (BattleshipGame) game.getGame();
            var hit = bsGame.makeHit(username,
                    new Point(Integer.parseInt(vertical), Integer.parseInt(horizontal)));
            var ships = bsGame.getShipCount(enemyUsername);
            if (ships == 0) {
                finishGame(gameId, username, enemyUsername);
                return MainPhrases.getBoom();
            }
            if (!hit)
                return MainPhrases.getFaultInCommand();
            game.nextQueue(enemyUsername);
            tgBot.sendMessageToUser(registeredUsers.get(enemyUsername).getChatId(), BSPhrases.getLetShoot(), false);
            return BSPhrases.getShootStat(horizontal, vertical);
        } catch (NotAllShipsSetException e) {
            return e.getMsg();
        }
    }

    private String setShip(String username, Game game, String request) {
        try {
            var horizontal = request.substring(3, 4);
            var vertical = request.substring(5, 6);
            var size = request.substring(7, 8);
            var direction = request.substring(9, 10);
            var bsGame = (BattleshipGame) game.getGame();
            var set = bsGame.setShip(username,
                    Integer.parseInt(size),
                    new Point(Integer.parseInt(vertical), Integer.parseInt(horizontal)),
                    game.direction.get(direction));
            if (!set)
                return MainPhrases.getFaultInCommand();
            return BSPhrases.getSetShipStat(horizontal, vertical, size, direction);
        } catch (SetShipException e) {
            return e.getMsg();
        }
    }

    private String surrender(String username) throws SQLException {
        var currentUser = registeredUsers.get(username);
        var gameId = currentUser.getGameId();
        var enemyUsername = startedGames.get(gameId).getEnemyName(currentUser);
        finishGame(gameId, enemyUsername, username);
        return MainPhrases.getWhiteFlag();
    }

    private String getMap(String username, Game game) {
        var ownMap = getOwnMap(username, game);
        var enemyMap = getEnemyMap(username, game);
        return BSPhrases.getMaps(ownMap, enemyMap);
    }

    private String getDefaultAnswer() {
        return MainPhrases.getCommandIsntFound();
    }

    private void finishGame(String gameId, String winnerName, String loserName) throws SQLException {
        var winner = registeredUsers.get(winnerName);
        var loser = registeredUsers.get(loserName);
        winner.exitFromGame();
        loser.exitFromGame();
        ScoreSheetConnector.setPlayersScore(winnerName, 20);
        tgBot.remStartedGame(gameId);
        tgBot.sendMessageToUser(winner.getChatId(), MainPhrases.getWin(), false);
        tgBot.sendMessageToUser(loser.getChatId(), MainPhrases.getLose(), false);
    }

    private String getOwnMap(String username, Game game) {
        var bsGame = (BattleshipGame) game.getGame();
        var field = bsGame.getCurrentPlayerField(username);
        return fieldToString(field, false);
    }

    private String getEnemyMap(String username, Game game) {
        var bsGame = (BattleshipGame) game.getGame();
        var field = bsGame.getEnemyField(username);
        return fieldToString(field, true);
    }

    private String fieldToString(Field field, boolean isEnemy) {
        StringBuilder result = new StringBuilder("- 0 1 2 3 4 5 \n");
        for (var i = 0; i < 6; i++) {
            result.append(i).append(" ");
            for (var j = 0; j < 6; j++) {
                var type = field.getCellTypeOnPosition(new Point(i, j));
                if (type.equals(CellType.Empty) || (type.equals(CellType.Ship) && isEnemy)) {
                    result.append("~ ");
                }
                else if (type.equals(CellType.Hit)) {
                    result.append("X ");
                }
                else if (type.equals(CellType.Miss)) {
                    result.append("0 ");
                }
                else if (type.equals(CellType.Ship)) {
                    result.append("☐ ");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }
}
