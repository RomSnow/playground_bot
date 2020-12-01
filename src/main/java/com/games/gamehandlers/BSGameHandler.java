package com.games.gamehandlers;

import com.buttons.Buttons;
import com.games.battleship.*;
import com.games.connection.Game;
import com.phrases.Phrases;
import com.user.User;
import com.playgroundbot.PlaygroundBot;

import java.util.HashMap;

public class BSGameHandler {
    private final Phrases phrases;
    private final HashMap<String, Game> availableGames;
    private final HashMap<String, Game> startedGames;
    private final HashMap<String, User> registeredUsers;
    private final PlaygroundBot tgBot;

    public BSGameHandler(Phrases phrases, HashMap<String, Game> availableGames,
                         HashMap<String, Game> startedGames, HashMap<String, User> registeredUsers,
                         PlaygroundBot main) {
        this.phrases = phrases;
        this.availableGames = availableGames;
        this.startedGames = startedGames;
        this.registeredUsers = registeredUsers;
        this.tgBot = main;
    }

    public String handleGame(String request, String userName) {
        var currentUser = registeredUsers.get(userName);
        var gameId = currentUser.getGameId();
        var game = startedGames.get(gameId);
        if (availableGames.containsKey(gameId)) {
            if (request.equals(Buttons.CANCEL)) {
                currentUser.exitFromGame();
                availableGames.remove(gameId);
                return phrases.gameIsOver();
            }
            return phrases.getWaitStr();
        }

        if (request.equals(Buttons.WHAT)) {
            return phrases.getBSInfo();
        }
        else {
            var command = request.substring(0, 2);
            switch (command) {
                case "-f":
                    try {
                        if (!userName.equals(game.getGameQueue()))
                            return phrases.getNotYourQueue();
                        var enemyUsername = startedGames.get(gameId).getEnemyName(currentUser);
                        var horizontal = request.substring(3, 4);
                        var vertical = request.substring(5, 6);
                        var hit = game.getGame().makeHit(userName,
                                new Point(Integer.parseInt(vertical), Integer.parseInt(horizontal)));
                        var ships = game.getGame().getShipCount(enemyUsername);
                        if (ships == 0) {
                            finishGame(gameId, userName, enemyUsername);
                            return phrases.getBoom();
                        }
                        if (!hit)
                            return phrases.getFaultInCommand();
                        game.nextQueue(enemyUsername);
                        tgBot.sendMessageToUser(registeredUsers.get(enemyUsername).getChatId(), phrases.getLetShoot(), false);
                        return phrases.getShootStat(horizontal, vertical);
                    } catch (StringIndexOutOfBoundsException e) {
                        return phrases.getFaultInCommand();
                    } catch (NotAllShipsSetException e) {
                        return e.getMsg();
                    }
                case "-s":
                    try {
                        var horizontal = request.substring(3, 4);
                        var vertical = request.substring(5, 6);
                        var size = request.substring(7, 8);
                        var direction = request.substring(9, 10);
                        var set = game.getGame().setShip(userName,
                                Integer.parseInt(size),
                                new Point(Integer.parseInt(vertical), Integer.parseInt(horizontal)),
                                game.direction.get(direction));
                        if (!set)
                            return phrases.getFaultInCommand();
                        return phrases.getSetShipStat(horizontal, vertical, size, direction);
                    } catch (StringIndexOutOfBoundsException e) {
                        return phrases.getFaultInCommand();
                    } catch (SetShipException e) {
                        return e.getMsg();
                    }
                case "-r":
                    var enemyUsername = startedGames.get(gameId).getEnemyName(currentUser);
                    finishGame(gameId, enemyUsername, userName);
                    return phrases.getWhiteFlag();
                case "-m":
                    var ownMap = getOwnMap(userName, game);
                    var enemyMap = getEnemyMap(userName, game);
                    return phrases.getMaps(ownMap, enemyMap);
                default:
                    return phrases.getCommandIsntFound();
            }
        }
    }

    private void finishGame(String gameId, String winnerName, String loserName) {
        var winner = registeredUsers.get(winnerName);
        var loser = registeredUsers.get(loserName);
        winner.exitFromGame();
        loser.exitFromGame();
        startedGames.remove(gameId);
        tgBot.sendMessageToUser(winner.getChatId(), "Победа!", false);
        tgBot.sendMessageToUser(loser.getChatId(), "Поражение.", false);
    }

    private String getOwnMap(String username, Game game) {
        var field = game.getGame().getCurrentPlayerField(username);
        return fieldToString(field, false);
    }

    private String getEnemyMap(String username, Game game) {
        var field = game.getGame().getEnemyField(username);
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
