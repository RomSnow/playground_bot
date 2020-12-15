package com.games.battleship;

import com.games.IGame;

import java.util.HashMap;

/**
 * Класс создания и управления игрой "Морской бой"
 */
public class BattleshipGame implements IGame {
    private final HashMap<String, Player> playersMap;
    private final HashMap<String, Boolean> readyMap;

    public BattleshipGame(GameParams gameParams, String firstPlayerName, String secondPlayerName) {
        var firstPlayer = new Player(gameParams);
        var secondPlayer = new Player(gameParams);

        playersMap = new HashMap<>();
        playersMap.put(firstPlayerName, firstPlayer);
        playersMap.put(secondPlayerName, secondPlayer);

        readyMap = new HashMap<>();
        readyMap.put(firstPlayerName, false);
        readyMap.put(secondPlayerName, false);

        firstPlayer.setEnemy(secondPlayer);
        secondPlayer.setEnemy(firstPlayer);
    }

    /**
     * Установка корабля на поле текущего игрока
     */
    public boolean setShip(String playerName,
                           int size, Point startPoint, Direction direction) throws SetShipException {
        var isCorrect = playersMap.get(playerName).setShip(startPoint, direction,
                getShipType(size));
        readyMap.put(playerName, playersMap.get(playerName).getIsAllShipsSet());
        return isCorrect;
    }

    /**
     * Выстрелл в выбранную точку карты противника
     */
    public boolean makeHit(String playerName, Point hitPoint) throws NotAllShipsSetException {
        for (String currentPlayer: readyMap.keySet())
            if (!readyMap.get(currentPlayer))
                throw new NotAllShipsSetException("Игрок " + currentPlayer + " не выставил все корабли!");

        var currentPlayer = playersMap.get(playerName);
        var isCorrect = currentPlayer.makeHit(hitPoint);

        if (isCorrect){
            var moveType = getEnemyField(playerName)
                    .getCellTypeOnPosition(hitPoint);

            switch (moveType){
                case Hit:
                    currentPlayer.addScore(2);
                case Miss:
                    currentPlayer.addScore(-1);
            }
        }

        return isCorrect;
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

    public int getPlayerScore(String playerName) {
        return playersMap.get(playerName).getScore();
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

    public int getShipCount(String playerName){
        return playersMap.get(playerName).getShipsCount();
    }

    private ShipType getShipType(int shipSize) {
        return switch (shipSize) {
            case 1 -> ShipType.oneSize;
            case 2 -> ShipType.twoSize;
            case 3 -> ShipType.threeSize;
            case 4 -> ShipType.fourSize;
            default -> throw new IllegalStateException("Unexpected value: " + shipSize);
        };
    }
}