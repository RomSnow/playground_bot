package com.games.battleship;

public class GameParams {
    private final int oneSizeShipsCount;
    private final int twoSizeShipsCount;
    private final int threeSizeShipsCount;
    private final int fourSizeShipsCount;
    private final int fieldSize;

    public GameParams(int oneSizeShipsCount,
                      int twoSizeShipsCount,
                      int threeSizeShipsCount,
                      int fourSizeShipsCount,
                      int fieldSize){
        this.fieldSize = fieldSize;
        this.oneSizeShipsCount = oneSizeShipsCount;
        this.twoSizeShipsCount = twoSizeShipsCount;
        this.threeSizeShipsCount = threeSizeShipsCount;
        this.fourSizeShipsCount = fourSizeShipsCount;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public int getSizeShipCount(ShipType shipType){
        switch (shipType){
            case oneSize:
                return oneSizeShipsCount;
            case twoSize:
                return twoSizeShipsCount;
            case threeSize:
                return threeSizeShipsCount;
            case fourSize:
                return fourSizeShipsCount;
            default:
                throw new IllegalStateException();
        }
    }
}
