package com.playgroundbot;

import com.buttons.Buttons;
import com.games.connection.Game;
import com.games.connection.GameType;
import com.games.gamehandlers.BSGameHandler;
import com.games.gamehandlers.TTTGameHandler;
import com.games.score_sheet_db.ScoreSheetConnector;
import com.phrases.MainPhrases;
import com.reader.ConfigReader;
import com.user.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.*;

public class PlaygroundBot extends TelegramLongPollingBot {
    private final HashMap<String, Game> availableGames;
    private final HashMap<String, Game> startedGames;
    private final HashMap<String, User> registeredUsers;
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final String token;
    private final String username;
    private static final String emptyVariable = "null";
    public static final Logger logger = Logger.getLogger(PlaygroundBot.class.getName());

    public PlaygroundBot(Level loggingLevel) {
        registeredUsers = new HashMap<>();
        availableGames = new HashMap<>();
        startedGames = new HashMap<>();
        token = ConfigReader.getDataFromConfFile("token.conf");
        username = ConfigReader.getDataFromConfFile("username.conf");
        loggerPreSetup(loggingLevel);
    }

    @Override
    public void onUpdateReceived(Update update) {
        var currentMessage = update.getMessage();
        var userName = currentMessage.getFrom().getUserName();
        var chatId = currentMessage.getChatId();
        var request = currentMessage.getText();
        var response = MainPhrases.getMistake();
        logger.log(Level.INFO, "Message from " + userName);
        logger.log(Level.CONFIG, "Text: '" + request + "'");

        if (userName.equals(emptyVariable)) {
            response = MainPhrases.getLetSetUserName();
        } else if (!registeredUsers.containsKey(userName)) {
            registerNewUser(userName, chatId);
            try {
                response = getResponse(request, userName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (!registeredUsers.get(userName).getGameId().equals(emptyVariable)) {
            var gameType = registeredUsers.get(userName).getGameType();
            if (gameType == GameType.BattleShip) {
                var handler = new BSGameHandler(availableGames, startedGames, registeredUsers, this);
                try {
                    response = handler.handleGame(request, userName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (gameType == GameType.TicTacToe) {
                var handler = new TTTGameHandler(availableGames, startedGames, registeredUsers, this);
                try {
                    response = handler.handleGame(request, userName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                response = getResponse(request, userName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        registeredUsers.get(userName).setLastReq(request);
        registeredUsers.get(userName).setLastResp(response);
        var isHasKB = registeredUsers.get(userName).getIsHasKB();

        logger.log(Level.INFO, "Message to " + userName);
        logger.log(Level.CONFIG, "Text: '" + response + "'");
        sendMessageToUser(chatId, response, isHasKB);
    }

    public void sendMessageToUser(Long chatId, String message, boolean isHasKB) {
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
        logger.log(Level.INFO, "Register " + userName);
    }

    private String getResponse(String request, String userName) throws SQLException {
        var currentUser = registeredUsers.get(userName);
        var lastRequest = currentUser.getLastReq();
        var lastResponse = currentUser.getLastResp();

        switch (request) {
            case Buttons.BEGIN:
                return formKeyboardAndAnswer(new String[]{
                        Buttons.LETS_PLAY, Buttons.INFO, Buttons.LEADERBOARD
                        }, MainPhrases.getQuestion(), userName);
            case Buttons.LETS_PLAY:
                return formKeyboardAndAnswer(new String[]{Buttons.BATTLE_SHIP, Buttons.TIC_TAC_TOE, Buttons.CONNECT_GAME},
                        MainPhrases.getChooseGame(), userName);
            case Buttons.INFO:
                return formKeyboardAndAnswer(new String[]{Buttons.LETS_PLAY, Buttons.INFO, Buttons.LEADERBOARD},
                        MainPhrases.getInfo(), userName);
            case Buttons.LEADERBOARD:
                return formKeyboardAndAnswer(new String[] {},
                        getLeaderboard(userName), userName);
            case Buttons.BATTLE_SHIP:
                if (lastRequest.equals(Buttons.LETS_PLAY)) {
                    return formKeyboardAndAnswer(new String[]{Buttons.CREATE_GAME},
                            MainPhrases.getAnswer(), userName);
                }
            case Buttons.TIC_TAC_TOE:
                if (lastRequest.equals(Buttons.LETS_PLAY)) {
                    return formKeyboardAndAnswer(new String[]{Buttons.CREATE_GAME},
                            MainPhrases.getAnswer(), userName);
                }
            case Buttons.CONNECT_GAME:
                if (lastRequest.equals(Buttons.LETS_PLAY)) {
                    currentUser.heIsFindGame();
                    return formKeyboardAndAnswer(new String[] {},
                            MainPhrases.getConnectGame(), userName);
                }
            case Buttons.CREATE_GAME:
                if (lastRequest.equals(Buttons.BATTLE_SHIP)) {
                    var createdGameId = getGameId(userName);
                    putAvailableGame(createdGameId, new Game(currentUser, "battleship"));
                    currentUser.setGameId(createdGameId);
                    currentUser.setGameType(GameType.BattleShip);
                    return formKeyboardAndAnswer(new String[] {},
                            MainPhrases.getCreateGame(createdGameId), userName);
                } else if (lastRequest.equals(Buttons.TIC_TAC_TOE)) {
                    var createdGameId = getGameId(userName);
                    putAvailableGame(createdGameId, new Game(currentUser, "tictactoe"));
                    currentUser.setGameId(createdGameId);
                    currentUser.setGameType(GameType.TicTacToe);
                    return formKeyboardAndAnswer(new String[] {},
                            MainPhrases.getCreateGame(createdGameId), userName);
                }
            case Buttons.CANCEL:
                if (lastResponse.equals(MainPhrases.getGameDoesntExist()) || lastResponse.equals(MainPhrases.getConnectGame())) {
                    currentUser.heIsNotFindGame();
                    return formKeyboardAndAnswer(new String[]{Buttons.BEGIN},
                            MainPhrases.getConnectCanceled(), userName);
                }
            default:
                if ((lastRequest.equals(Buttons.CONNECT_GAME) || lastResponse.equals(MainPhrases.getGameDoesntExist()))
                        && isAvailableGameExist(request)) {
                    var enemyChatId = availableGames.get(request).getFirstPlayerChatId();
                    var enemyName = availableGames.get(request).getFirstPlayerName();
                    availableGames.get(request).connectUser(currentUser);
                    putStartedGame(request, availableGames.get(request));
                    remAvailableGame(request);

                    sendMessageToUser(enemyChatId, MainPhrases.getConnected(userName), false);
                    currentUser.heIsNotFindGame();
                    currentUser.setGameId(request);
                    currentUser.setGameType(startedGames.get(request).getGameType());

                    return formKeyboardAndAnswer(new String[] {},
                            MainPhrases.getFoundGame(enemyName),
                            userName);
                }
                else if (currentUser.getIsHeFindGame() && !isAvailableGameExist(request)) {
                    return MainPhrases.getGameDoesntExist();
                }
                else {
                    return formKeyboardAndAnswer(new String[]{Buttons.BEGIN},
                            MainPhrases.getReadiness(),
                            userName);
                }
        }
    }

    private void putStartedGame(String key, Game game) {
        startedGames.put(key, game);
        logger.log(Level.INFO, "Started games: " + startedGames.size());
    }

    private void putAvailableGame(String key, Game game) {
        availableGames.put(key, game);
        logger.log(Level.INFO, "Available games: " + availableGames.size());
    }

    public void remStartedGame(String key) {
        startedGames.remove(key);
        logger.log(Level.INFO, "Started games: " + startedGames.size());
    }

    public void remAvailableGame(String key) {
        availableGames.remove(key);
        logger.log(Level.INFO, "Available games: " + availableGames.size());
    }

    private String getLeaderboard(String username){
        var lb = ScoreSheetConnector.getGameScoreSheet(5);
        var yourPos = ScoreSheetConnector.getPlayerPosition(username);
        return MainPhrases.getLeaderboard(lb, yourPos);
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

    private void loggerPreSetup(Level level) {
        try {

            var sysOut = new ConsoleHandler();
            sysOut.setLevel(level);

            var currentDir = System.getProperty("user.dir");
            var currentPath = Paths.get(currentDir,"logs");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-");
            LocalDateTime now = LocalDateTime.now();
            var pattern = String.format("%s/%s%%u%%g.log", currentPath, dtf.format(now));
            var fh = new FileHandler(pattern, 10000, 10000);
            fh.setLevel(level);

            logger.addHandler(sysOut);
            logger.addHandler(fh);
            logger.setLevel(level);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}