package com.games.battleship;

import java.util.HashMap;

class Player {
    private int shipsCount = 0;
    private GameParams gameParams;
    private HashMap<ShipType, Integer> shipsMap;
    private final Field playerField;
    private boolean isAllShipsSet = false;

    private Player enemy;
    private Field enemyField;

    public Player(GameParams gameParams) {
        this.gameParams = gameParams;

        playerField = new Field(gameParams.getFieldSize(), this);
        shipsMap = new HashMap<ShipType, Integer>();
        shipsMap.put(ShipType.oneSize, 0);
        shipsMap.put(ShipType.twoSize, 0);
        shipsMap.put(ShipType.threeSize, 0);
        shipsMap.put(ShipType.fourSize, 0);
    }

    public int getShipsCount() {
        return shipsCount;
    }

    public Field getPlayerField() {
        return playerField;
    }

    public Player getEnemy() {
        return enemy;
    }

    public boolean setShip(Point startPoint, Direction direction, ShipType shipType)
            throws SetShipException {
        if (shipsMap.get(shipType) == gameParams.getSizeShipCount(shipType))
            throw new SetShipException("Все доступные корабли размера "
                    + shipType.getIntSizeShip() + " выставленны!");

        var isCorrect = playerField.setShipOnPosition(shipType.getIntSizeShip(),
                direction, startPoint);

        if (isCorrect)
        {
            shipsCount++;
            shipsMap.put(shipType, shipsMap.get(shipType) + 1);
        }

        isAllShipsSet = true;
        for (ShipType ship: shipsMap.keySet())
            if (shipsMap.get(ship) != gameParams.getSizeShipCount(shipType))
            {
                isAllShipsSet = false;
                break;
            }

        return isCorrect;
    }

    public void destroyShip() {
        shipsCount--;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
        enemyField = this.enemy.getPlayerField();
    }

    public int getRemainingCountOfShip(ShipType shipType){
        return shipsMap.get(shipType);
    }

    public boolean makeHit(Point hitPoint){
        return enemyField.fire(hitPoint);
    }

    public boolean getIsAllShipsSet() {
        return isAllShipsSet;
    }
}
