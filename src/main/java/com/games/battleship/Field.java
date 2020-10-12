package com.games.battleship;

public class Field {
    private final Cell[][] field;
    private int AliveCells;

    public Field() {
        field = new Cell[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                field[i][j] = Cell.Empty;
    }

    //Добавить метод постановки кораблей

    public boolean Fire(int i, int j) {
        if (field[i][j] == Cell.Empty) {
            field[i][j] = Cell.Miss;
            return false;
        }
        else if (field[i][j] == Cell.Ship) {
            field[i][j] = Cell.Hit;
            AliveCells--;
            return true;
        }
        return false;
    }

    public int getAliveCells() {
        return AliveCells;
    }

}
