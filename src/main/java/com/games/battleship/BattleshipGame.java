package com.games.battleship;

import java.util.HashMap;

/**
 * Класс создания и управления игрой "Морской бой"
 */
public class BattleshipGame {
    private final HashMap<String, Player> playersMap;

    public BattleshipGame(GameParams gameParams, String firstPlayerName, String secondPlayerName) {
        var firstPlayer = new Player(gameParams);
        var secondPlayer = new Player(gameParams);

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
                           int size, Point startPoint, Direction direction) throws Exception {

        return playersMap.get(playerName).setShip(startPoint, direction,
                getShipType(size));
    }

    /**
     * Выстрелл в выбранную точку карты противника
     */
    public boolean makeHit(String playerName, Point hitPoint) throws Exception {
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

    public HashMap<String, Integer> getRemainingShipPlayerMap(String playerName){
        var player = playersMap.get(playerName);
        var playersMap = new HashMap<String, Integer>();
        playersMap.put("1", player.getRemainingCountOfShip(ShipType.oneSize));
        playersMap.put("2", player.getRemainingCountOfShip(ShipType.twoSize));
        playersMap.put("3", player.getRemainingCountOfShip(ShipType.threeSize));
        playersMap.put("4", player.getRemainingCountOfShip(ShipType.fourSize));
        return playersMap;
    }

    private ShipType getShipType(int shipSize){
        switch (shipSize){
            case 1:
                return ShipType.oneSize;
            case 2:
                return ShipType.twoSize;
            case 3:
                return ShipType.threeSize;
            case 4:
                return ShipType.fourSize;
            default:
                throw new IllegalStateException("Unexpected value: " + shipSize);
        }
    }
}