package com.user;

import com.games.connection.GameInfo;
import com.games.connection.GameType;

public class User {
    private final String userName;
    private final Long userChatId;
    private String userLastRequest;
    private String userLastResponse;
    private final GameInfo gameInfo;
    private boolean isHeFindGame;
    private boolean isHasKB;

    public User(String name, Long chatId) {
        userName = name;
        userChatId = chatId;
        userLastRequest = "";
        userLastResponse = "";
        gameInfo = new GameInfo(GameType.Undefined, "null");
        isHasKB = false;
        isHeFindGame = false;
    }

    public String getUserName() {
        return userName;
    }

    public Long getChatId() {
        return userChatId;
    }

    public void setLastReq(String message) {
        userLastRequest = message;
    }

    public void setLastResp(String message) {
        userLastResponse = message;
    }

    public String getLastReq() {
        return userLastRequest;
    }

    public String getLastResp() {
        return userLastResponse;
    }

    public String getGameId() {
        return gameInfo.getGameId();
    }

    public void setGameId(String gameId) {
        gameInfo.setGameId(gameId);
    }

    public void exitFromGame() {
        gameInfo.setGameId("null");
        gameInfo.setGameType(GameType.Undefined);
    }

    public void setGameType(GameType type) {
        gameInfo.setGameType(type);
    }

    public GameType getGameType() {
        return gameInfo.getGameType();
    }

    public boolean getIsHasKB() {
        return isHasKB;
    }

    public void setIsHasKB(boolean isHas) {
        isHasKB = isHas;
    }

    public boolean getIsHeFindGame() {
        return isHeFindGame;
    }

    public void heIsFindGame() {
        isHeFindGame = true;
    }

    public void heIsNotFindGame() {
        isHeFindGame = false;
    }
}
