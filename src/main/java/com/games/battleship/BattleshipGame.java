package com.games.battleship;

import java.util.ArrayList;

/** Класс создания и управления игрой "Морской бой" */
public class BattleshipGame {
    private Player currentPlayer;

    public BattleshipGame(int fieldSize) {
        var firstPlayer = new Player(fieldSize);
        var secondPlayer = new Player(fieldSize);

        firstPlayer.setEnemy(secondPlayer);
        secondPlayer.setEnemy(firstPlayer);

        currentPlayer = firstPlayer;
    }

    /** Установка корабля на поле текущего игрока */
    public boolean setShip(int size, Point startPoint, Direction direction) {
        return currentPlayer.setShip(startPoint, direction, size);
    }

    /** Выстрелл в выбранную точку карты противника*/
    public boolean makeHit(Point hitPoint){
        return currentPlayer.makeHit(hitPoint);
    }

    /** Получение Field текущего игрока */
    public Field getCurrentPlayerField(){
        return currentPlayer.getPlayerField();
    }

    /** Получение Field противника */
    public Field getEnemyField(){
        return currentPlayer.getEnemy().getPlayerField();
    }

    /** Смена текущего игрока */
    public void switchPlayer() {
        currentPlayer = currentPlayer.getEnemy();
    }

    /** Получение количества кораблей у текущего игрока */
    public int getPlayersShipsCount() {
        return currentPlayer.getShipsCount();
    }
}

class Player {
    private int shipsCount = 0;
    private final Field playerField;

    private Player enemy;
    private Field enemyField;

    public Player(int fieldSize){
        playerField = new Field(fieldSize, this);
    }

    public int getShipsCount() {
        return shipsCount;
    }

    public Field getPlayerField() {
        return playerField;
    }

    public Player getEnemy(){
        return enemy;
    }

    public boolean setShip(Point startPoint, Direction direction, int size) {
        var isCorrect = playerField.setShipOnPosition(size, direction, startPoint);

        if (isCorrect)
            shipsCount++;

        return isCorrect;
    }

    public void destroyShip(){
        shipsCount--;
    }

    public void setEnemy(Player enemy){
        this.enemy = enemy;
        enemyField = this.enemy.getPlayerField();
    }

    public boolean makeHit(Point hitPoint){
        return enemyField.fire(hitPoint);
    }
}