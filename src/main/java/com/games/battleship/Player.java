package com.games.battleship;

class Player {
    private int shipsCount = 0;
    private final Field playerField;

    private Player enemy;
    private Field enemyField;

    public Player(int fieldSize) {
        playerField = new Field(fieldSize, this);
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

    public boolean setShip(Point startPoint, Direction direction, int size) {
        var isCorrect = playerField.setShipOnPosition(size, direction, startPoint);

        if (isCorrect)
            shipsCount++;

        return isCorrect;
    }

    public void destroyShip() {
        shipsCount--;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
        enemyField = this.enemy.getPlayerField();
    }

    public boolean makeHit(Point hitPoint) {
        return enemyField.fire(hitPoint);
    }
}
