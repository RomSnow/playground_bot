package com.games.battleship;


public class Field {
    private final Cell[][] field;

    public Field(int size) {
        field = new Cell[size][size];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                field[i][j] = new Cell();
    }

    public void setFieldOnPosition(Ship ship, Direction direction, Point startPoint) {
        int startX = startPoint.getX();
        int startY = startPoint.getY();

        var currentPoint = startPoint;
        for (int i = 0; i < ship.getSize(); i++) {
            field[currentPoint.getX()][currentPoint.getY()].setShip(ship);
            currentPoint = getNextPosition(currentPoint, direction);
        }
    }

    public boolean fire(Point point) {
        int x = point.getX();
        int y = point.getY();

        if (field[x][y].isEmpty()) {
            field[x][y].setMiss();
            return false;
        }
        else if (field[x][y].isShip()) {
            field[x][y].setHit();
            return true;
        }

        return false;
    }

    private Point getNextPosition(Point currentPoint, Direction direction){
        switch (direction){
            case Down:
                return new Point(currentPoint.getX(), currentPoint.getY() + 1);
            case Up:
                return new Point(currentPoint.getX(), currentPoint.getY() - 1);
            case Left:
                return new Point(currentPoint.getX() - 1, currentPoint.getY());
            case Right:
                return new Point(currentPoint.getX() + 1, currentPoint.getY());
            default:
                return currentPoint;
        }
    }
}
