package com.games.battleship;

class Cell {
    private CellType type;
    private Ship ship;

    public Cell() {
        this.type = CellType.Empty;
    }

    public CellType getType() {
        return this.type;
    }

    public boolean setShip(Ship ship) {
        if (this.ship != null)
            return false;
        this.ship = ship;
        this.type = CellType.Ship;
        return true;
    }

    public void setMiss() {
        this.type = CellType.Miss;
    }

    public void setEmpty() {
        type = CellType.Empty;
    }

    public void setHit() {
        this.ship.makeHit();
        this.type = CellType.Hit;
    }

    public boolean isEmpty() {
        return this.type == CellType.Empty;
    }

    public boolean isShip() {
        return this.type == CellType.Ship;
    }

}

