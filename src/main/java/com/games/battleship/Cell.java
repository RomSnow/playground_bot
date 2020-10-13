package com.games.battleship;

public class Cell {
    private CellType type;
    private Ship ship;

    public Cell(){
        this.type = CellType.Empty;
    }

    public CellType getType(){
        return this.type;
    }

    public void setShip(Ship ship)
    {
        this.ship = ship;
        this.type = CellType.Ship;
    }

    public void setMiss(){
        this.type = CellType.Miss;
    }

    public void setHit(){
        this.ship.makeHit();
        this.type = CellType.Hit;
    }

    public boolean isEmpty(){
        return this.type == CellType.Empty;
    }

    public boolean isShip(){
        return this.type == CellType.Ship;
    }

}

enum CellType{
    Miss,
    Hit,
    Ship,
    Empty
}
