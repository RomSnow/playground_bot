package com.games.battleship;

public class Ship {
    private int size;
    private boolean isDestroy;

    public Ship(int size){
        this.size = size;
    }

    public void makeHit(){
        size--;
        if (this.size == 0)
            isDestroy = true;
    }

    public int getSize() {
        return size;
    }

    public boolean isShipDestroy(){
        return isDestroy;
    }
}
