package com.games.battleship;

enum ShipType {
    oneSize, twoSize, threeSize, fourSize;

    public int getIntSizeShip(){
        switch (this) {
            case oneSize:
                return 1;
            case twoSize:
                return 2;
            case threeSize:
                return 3;
            case fourSize:
                return 4;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

}
