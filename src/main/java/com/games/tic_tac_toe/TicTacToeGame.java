package com.games.tic_tac_toe;

import com.games.battleship.Point;

import java.util.HashMap;

public class TicTacToeGame {
    private boolean isGameEnd = false;
    private String winPlayerName = "";
    private final HashMap<String, CharType> playerCharMap;
    private final TicTacField field;

    public TicTacToeGame(String oPlayerName, String xPlayerName){
        playerCharMap = new HashMap<>();
        playerCharMap.put(oPlayerName, CharType.O);
        playerCharMap.put(xPlayerName, CharType.X);

        field = new TicTacField();
    }

    public boolean setCharOnPosition(String playerName, Point point)
            throws GameIsEndException {

        var charType = playerCharMap.get(playerName);
        var isCorrect = field.setCharOnPosition(charType, point);

        if (field.checkOnCharWin(charType)){
            isGameEnd = true;
            winPlayerName = playerName;
        }
        if (isGameEnd)
            throw new GameIsEndException();
        return isCorrect;
    }

    public CharType getPlayerCharType(String playerName){
        return playerCharMap.get(playerName);
    }

    public CharType getCharTypeOnPosition(Point point){
        return field.getCharOnPosition(point);
    }

    public String getWinPlayerName(){
        return winPlayerName;
    }

    public boolean isGameEnd() {
        return isGameEnd;
    }
}
