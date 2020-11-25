package com.games.battleship;

import java.util.HashMap;

/**
 * Класс создания и управления игрой "Морской бой"
 */
public class BattleshipGame {
    private HashMap<String, Player> playersMap;

    public BattleshipGame(int fieldSize, String firstPlayerName, String secondPlayerName) {
        var firstPlayer = new Player(fieldSize);
        var secondPlayer = new Player(fieldSize);

        playersMap = new HashMap<String, Player>();
        playersMap.put(firstPlayerName, firstPlayer);
        playersMap.put(secondPlayerName, secondPlayer);

        firstPlayer.setEnemy(secondPlayer);
        secondPlayer.setEnemy(firstPlayer);
    }

    /**
     * Установка корабля на поле текущего игрока
     */
    public boolean setShip(String playerName,
                           int size, Point startPoint, Direction direction) {
        return playersMap.get(playerName).setShip(startPoint, direction, size);
    }

    /**
     * Выстрелл в выбранную точку карты противника
     */
    public boolean makeHit(String playerName, Point hitPoint) {
        return playersMap.get(playerName).makeHit(hitPoint);
    }

    /**
     * Получение Field текущего игрока
     */
    public Field getCurrentPlayerField(String playerName) {
        return playersMap.get(playerName).getPlayerField();
    }

    /**
     * Получение Field противника
     */
    public Field getEnemyField(String playerName) {
        return playersMap.get(playerName).getEnemy().getPlayerField();
    }
    /**
     * Получение количества кораблей у текущего игрока
     */
    public int getPlayersShipsCount(String playerName) {
        return playersMap.get(playerName).getShipsCount();
    }
}