package com.user;

public class User {
    private final String userName;
    private final Long userChatId;
    private String userLastRequest;
    private String userLastResponse;
    private String inTheGameById;
    private boolean isHeFindGame;
    private boolean isHasKB;

    public User(String name, Long chatId) {
        userName = name;
        userChatId = chatId;
        userLastRequest = "";
        userLastResponse = "";
        inTheGameById = "null";
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
        return inTheGameById;
    }

    public void setGameId(String gameId) {
        inTheGameById = gameId;
    }

    public void exitFromGame() {
        inTheGameById = "null";
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
