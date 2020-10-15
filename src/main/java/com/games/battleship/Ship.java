package com.games.battleship;

public class Ship {
    private int size;
    private boolean isDestroy;
    private final Player master;

    public Ship(int size, Player player){
        this.size = size;
        master = player;
    }

    public void makeHit(){
        size--;
        if (this.size == 0){
            isDestroy = true;
            master.destroyShip();
        }
    }

    public int getSize() {
        return size;
    }

    public boolean isShipDestroy(){
        return isDestroy;
    }
}
