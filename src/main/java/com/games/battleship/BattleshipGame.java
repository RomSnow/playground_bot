package com.games.battleship;
/**
 * Класс создания и управления игрой "Морской бой"
 */
public class BattleshipGame {
    private Player currentPlayer;

    public BattleshipGame(int fieldSize) {
        var firstPlayer = new Player(fieldSize);
        var secondPlayer = new Player(fieldSize);

        firstPlayer.setEnemy(secondPlayer);
        secondPlayer.setEnemy(firstPlayer);

        currentPlayer = firstPlayer;
    }

    /**
     * Установка корабля на поле текущего игрока
     */
    public boolean setShip(int size, Point startPoint, Direction direction) {
        return currentPlayer.setShip(startPoint, direction, size);
    }

    /**
     * Выстрелл в выбранную точку карты противника
     */
    public boolean makeHit(Point hitPoint) {
        return currentPlayer.makeHit(hitPoint);
    }

    /**
     * Получение Field текущего игрока
     */
    public Field getCurrentPlayerField() {
        return currentPlayer.getPlayerField();
    }

    /**
     * Получение Field противника
     */
    public Field getEnemyField() {
        return currentPlayer.getEnemy().getPlayerField();
    }

    /**
     * Смена текущего игрока
     */
    public void switchPlayer() {
        currentPlayer = currentPlayer.getEnemy();
    }

    /**
     * Получение количества кораблей у текущего игрока
     */
    public int getPlayersShipsCount() {
        return currentPlayer.getShipsCount();
    }
}