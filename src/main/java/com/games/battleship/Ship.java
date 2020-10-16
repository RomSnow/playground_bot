package com.games.battleship;

class Ship {
    private int size;
    private final Player master;

    public Ship(int size, Player player){
        this.size = size;
        master = player;
    }

    public void makeHit(){
        size--;
        if (this.size == 0){
            master.destroyShip();
        }
    }

    public int getSize() {
        return size;
    }
}
