package com.playgroundbot;

import com.buttons.Buttons;
import com.games.battleship.BattleshipGame;
import com.games.battleship.CellType;
import com.games.battleship.Direction;
import com.games.battleship.Point;
import com.games.connection.AvailableGame;
import com.phrases.Phrases;
import com.user.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaygroundBot extends TelegramLongPollingBot {
    private final HashMap<String, AvailableGame> availableGames;
    private final HashMap<String, AvailableGame> startedGames;
    private final HashMap<String, User> registeredUsers;
    private final Phrases phrases;
    private final Buttons buttons;
    final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public PlaygroundBot() {
        registeredUsers = new HashMap<>();
        availableGames = new HashMap<>();
        startedGames = new HashMap<>();
        phrases = new Phrases();
        buttons = new Buttons();
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

        if (request.equals(buttons.getBegin())) {
            return formKeyboardAndAnswer(new String[]{buttons.getLetsPlay(), buttons.getInfo()},
                    phrases.getQuestion(),
                    userName);
        }
        else if (request.equals(buttons.getLetsPlay()) &&
                (lastRequest.equals(buttons.getBegin()) || lastRequest.equals(buttons.getInfo()))) {
            return formKeyboardAndAnswer(new String[]{buttons.getBattleShip()},
                    phrases.getChooseGame(),
                    userName);
        }
        else if (request.equals(buttons.getInfo()) && lastRequest.equals(buttons.getBegin())) {
            return formKeyboardAndAnswer(new String[]{buttons.getLetsPlay(), buttons.getInfo()},
                    phrases.getInfo(),
                    userName);
        }
        else if (request.equals(buttons.getBattleShip()) && lastRequest.equals(buttons.getLetsPlay())) {
            return formKeyboardAndAnswer(new String[]{buttons.getCreateGame(), buttons.getConnectGame()},
                    phrases.getAnswer(),
                    userName);
        }
        else if (request.equals(buttons.getCreateGame()) && lastRequest.equals(buttons.getBattleShip())) {
            var createdGameId = getGameId(userName);
            availableGames.put(createdGameId, new AvailableGame(currentUser, createdGameId));
            currentUser.setGameId(createdGameId);

            return formKeyboardAndAnswer(new String[] {},
                    phrases.getCreateGame(createdGameId),
                    userName);
        }
        else if (request.equals(buttons.getConnectGame()) && lastRequest.equals(buttons.getBattleShip())) {
            currentUser.heIsFindGame();
            return formKeyboardAndAnswer(new String[] {},
                    phrases.getConnectGame(),
                    userName);
        }
        else if ((lastRequest.equals(buttons.getConnectGame()) || lastResponse.equals(phrases.getGameDoesntExist()))
                && availableGameExist(request)) {
            var enemyChatId = availableGames.get(request).getFirstPlayerChatId();
            var enemyName = availableGames.get(request).getFirstPlayerName();
            availableGames.get(request).ConnectUser(currentUser);
            startedGames.put(request, availableGames.get(request));
            availableGames.remove(request);

            sendMessageToUser(enemyChatId, "К игре подключился " + userName + ".\nДля помощи введи 'Что?'.",
                    false);
            currentUser.heIsNotFindGame();
            currentUser.setGameId(request);

            return formKeyboardAndAnswer(new String[] {},
                    phrases.getFoundGame(enemyName),
                    userName);
        }
        else if (request.equals(buttons.getCancel()) &&
                (lastResponse.equals(phrases.getGameDoesntExist()) || lastResponse.equals(phrases.getConnectGame()))) {
            currentUser.heIsNotFindGame();
            return formKeyboardAndAnswer(new String[]{buttons.getBegin()},
                    phrases.getConnectCanceled(),
                    userName);
        }
        else if (currentUser.getIsHeFindGame() && !availableGameExist(request)) {
            return phrases.getGameDoesntExist();
        }
        else {
            return formKeyboardAndAnswer(new String[]{buttons.getBegin()},
                    phrases.getReadiness(),
                    userName);
        }
    }

    private String gameHandler(String request, String userName) {
        var currentUser = registeredUsers.get(userName);
        var gameId = currentUser.getGameId();
        if (availableGames.containsKey(gameId)) {
            if (request.equals(buttons.getCancel())) {
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

        if (request.equals(buttons.getCancel())) {
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

    private String getGameId(String creatorChatId) {
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

    private boolean availableGameExist(String gameId) {
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

        game.switchPlayer();
        return result.toString();
    }

    @Override
    public String getBotUsername() {
        return "PlaygroundMasterBot";
    }

    @Override
    public String getBotToken() {
        return "1179723857:AAGNIjsk9XvwrC0mBg6-c4nti8AGuCZeBso";
    }
}