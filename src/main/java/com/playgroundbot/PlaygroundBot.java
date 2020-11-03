package com.playgroundbot;

import com.buttons.Buttons;
import com.games.battleship.BattleshipGame;
import com.games.battleship.CellType;
import com.games.battleship.Direction;
import com.games.battleship.Point;
import com.games.connection.AvailableGame;
import com.phrases.Phrases;
import com.user.User;
import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaygroundBot extends TelegramLongPollingBot {
    private final HashMap<String, AvailableGame> availableGames;
    private final HashMap<String, AvailableGame> startedGames;
    private final HashMap<String, User> registeredUsers;
    private final Phrases phrases;
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final String token;
    private final String username;

    public PlaygroundBot() {
        registeredUsers = new HashMap<>();
        availableGames = new HashMap<>();
        startedGames = new HashMap<>();
        phrases = new Phrases();
        token = getDataFromConfFile("token.conf");
        username = getDataFromConfFile("username.conf");
    }

    @Override
    public void onUpdateReceived(Update update) {
        var currentMessage = update.getMessage();
        var userName = currentMessage.getFrom().getUserName();
        var chatId = currentMessage.getChatId();
        var request = currentMessage.getText();
        var response = "Ошибка!";

        if (userName.equals("null")) {
            response = phrases.getLetSetUserName();
        } else if (!registeredUsers.containsKey(userName)) {
            registerNewUser(userName, chatId);
            response = phrases.getNewUserHello();
        } else if (!registeredUsers.get(userName).getGameId().equals("null")) {
            response = gameHandler(request, userName);
        } else {
            response = getResponse(request, userName);
        }


        System.out.println("Available games: " + availableGames.size());
        System.out.println("Started games: " + startedGames.size());
        System.out.println();

        registeredUsers.get(userName).setLastReq(request);
        registeredUsers.get(userName).setLastResp(response);
        var isHasKB = registeredUsers.get(userName).getIsHasKB();

        sendMessageToUser(chatId, response, isHasKB);
    }

    private void sendMessageToUser(Long chatId, String message, boolean isHasKB) {
        var sendMessage = new SendMessage(chatId, message);

        if (isHasKB) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void registerNewUser(String userName, Long chatId) {
        var user = new User(userName, chatId);
        registeredUsers.put(userName, user);
        System.out.println("Register " + userName);
    }

    private String getResponse(String request, String userName) {
        var currentUser = registeredUsers.get(userName);
        var lastRequest = currentUser.getLastReq();
        var lastResponse = currentUser.getLastResp();

        switch (request) {
            case Buttons.BEGIN:
                return formKeyboardAndAnswer(new String[]{Buttons.LETS_PLAY, Buttons.INFO},
                        phrases.getQuestion(), userName);
            case Buttons.LETS_PLAY:
                if (lastRequest.equals(Buttons.BEGIN) || lastRequest.equals(Buttons.INFO)) {
                    return formKeyboardAndAnswer(new String[]{Buttons.BATTLE_SHIP},
                            phrases.getChooseGame(), userName);
                }
            case Buttons.INFO:
                if (lastRequest.equals(Buttons.BEGIN) || lastRequest.equals(Buttons.INFO)) {
                    return formKeyboardAndAnswer(new String[]{Buttons.LETS_PLAY, Buttons.INFO},
                            phrases.getInfo(), userName);
                }
            case Buttons.BATTLE_SHIP:
                if (lastRequest.equals(Buttons.LETS_PLAY)) {
                    return formKeyboardAndAnswer(new String[]{Buttons.CREATE_GAME, Buttons.CONNECT_GAME},
                            phrases.getAnswer(), userName);
                }
            case Buttons.CREATE_GAME:
                if (lastRequest.equals(Buttons.BATTLE_SHIP)) {
                    var createdGameId = getGameId(userName);
                    availableGames.put(createdGameId, new AvailableGame(currentUser, createdGameId));
                    currentUser.setGameId(createdGameId);
                    return formKeyboardAndAnswer(new String[] {},
                            phrases.getCreateGame(createdGameId), userName);
                }
            case Buttons.CONNECT_GAME:
                if (lastRequest.equals(Buttons.BATTLE_SHIP)) {
                    currentUser.heIsFindGame();
                    return formKeyboardAndAnswer(new String[] {},
                            phrases.getConnectGame(), userName);
                }
            case Buttons.CANCEL:
                if (lastResponse.equals(phrases.getGameDoesntExist()) || lastResponse.equals(phrases.getConnectGame())) {
                    currentUser.heIsNotFindGame();
                    return formKeyboardAndAnswer(new String[]{Buttons.BEGIN},
                            phrases.getConnectCanceled(), userName);
                }
            default:
                if ((lastRequest.equals(Buttons.CONNECT_GAME) || lastResponse.equals(phrases.getGameDoesntExist()))
                        && isAvailableGameExist(request)) {
                    var enemyChatId = availableGames.get(request).getFirstPlayerChatId();
                    var enemyName = availableGames.get(request).getFirstPlayerName();
                    availableGames.get(request).ConnectUser(currentUser);
                    startedGames.put(request, availableGames.get(request));
                    availableGames.remove(request);

                    sendMessageToUser(enemyChatId, phrases.getConnected(userName), false);
                    currentUser.heIsNotFindGame();
                    currentUser.setGameId(request);

                    return formKeyboardAndAnswer(new String[] {},
                            phrases.getFoundGame(enemyName),
                            userName);
                }
                else if (currentUser.getIsHeFindGame() && !isAvailableGameExist(request)) {
                    return phrases.getGameDoesntExist();
                }
                else {
                    return formKeyboardAndAnswer(new String[]{Buttons.BEGIN},
                            phrases.getReadiness(),
                            userName);
                }
        }
    }

    private String gameHandler(String request, String userName) {
        var currentUser = registeredUsers.get(userName);
        var gameId = currentUser.getGameId();
        if (availableGames.containsKey(gameId)) {
            if (request.equals(Buttons.CANCEL)) {
                currentUser.exitFromGame();
                availableGames.remove(gameId);
                return "Игра завершена!";
            }
            return "Ожидание противника.";
        }
        var currentUserName = currentUser.getUserName();
        var enemyUserName = startedGames.get(gameId).getEnemyName(currentUser);
        var enemyUser = registeredUsers.get(enemyUserName);
        var enemyUserChatId = enemyUser.getChatId();
        var currentUserChatId = currentUser.getChatId();
        var game = startedGames.get(gameId).getGame();
        var thisPlayerIsFirst = currentUserName.equals(startedGames.get(gameId).getFirstPlayerName());

        if (request.equals(Buttons.CANCEL)) {
            currentUser.exitFromGame();
            enemyUser.exitFromGame();
            startedGames.remove(gameId);
            sendMessageToUser(enemyUserChatId, "Ваш противник отменил игру!", false);
            return "Игра завершена!";
        }
        else if (request.equals("Что?")) {
            return "Ставить корабли ты можешь вот так...";
        }
        else if (request.equals("Поле")) {
            game.setShip(5, new Point(0, 0), Direction.Down);
            game.switchPlayer();
            game.makeHit(new Point(0,0));
            game.switchPlayer();
            return getField(game, thisPlayerIsFirst);
        }

        var ships = numberOfShips(game, thisPlayerIsFirst);
        if (ships < 10) {
            return "Кораблей недостаточно!";
        }

        return "Твоё сообщение: '" + request + "'\nТы в игре!";
    }

    private String formKeyboardAndAnswer(String[] buttonsForKB, String outPhrase, String userName) {
        var user = registeredUsers.get(userName);
        user.setIsHasKB(buttonsForKB.length != 0);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        var keyboardFirstRow = new KeyboardRow();
        var keyboard = new ArrayList<KeyboardRow>();
        for (String button: buttonsForKB) {
            keyboardFirstRow.add(button);
        }
        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return outPhrase;
    }

    private static String getGameId(String creatorChatId) {
        try {
            var str = creatorChatId + Math.random();
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.substring(0, 6);
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAvailableGameExist(String gameId) {
        return availableGames.containsKey(gameId);
    }

    private int numberOfShips(BattleshipGame game, boolean isForFirstPlayer) {
        if (isForFirstPlayer) {
            return game.getPlayersShipsCount();
        }
        else {
            game.switchPlayer();
            var shipsSecondPlayer = game.getPlayersShipsCount();
            game.switchPlayer();
            return shipsSecondPlayer;
        }
    }

    private String getField(BattleshipGame game, boolean isForFirstPlayer) {
        if (!isForFirstPlayer)
            game.switchPlayer();
        var result = new StringBuilder("`* 0 1 2 3 4 5 6 7 8 9`\n");
        var field = game.getCurrentPlayerField();
        for (var i = 0; i < 10; i++) {
            var beg = "`" + i;
            result.append(beg);
            for (var j = 0; j < 10; j++) {
                var pos = new Point(i, j);
                var cell = field.getCellTypeOnPosition(pos);
                if (cell == CellType.Miss)
                    result.append(" O");
                else if (cell == CellType.Hit)
                    result.append(" X");
                else if (cell == CellType.Ship)
                    result.append(" ☐");
                else if (cell == CellType.Empty)
                    result.append(" ~");
            }
            result.append("`\n");
        }
        if (!isForFirstPlayer)
            game.switchPlayer();
        return result.toString();
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private String getDataFromConfFile(String name) {
        var result = "";
        var currentDir = System.getProperty("user.dir");
        var currentPath = Paths.get(currentDir, name);
        var file = new File(currentPath.toString());
        try {
            result = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}