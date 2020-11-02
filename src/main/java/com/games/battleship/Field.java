package com.games.battleship;


import java.util.ArrayList;
import java.util.List;

public class Field {
    private final Cell[][] field;
    private final Player master;

    public Field(int size, Player master) {
        this.master = master;
        field = new Cell[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                field[i][j] = new Cell();
    }

    /** Назначает корабль на указанную позицию
     * Не использовать вне пакета! */
    public boolean setShipOnPosition(int size, Direction direction,
                                     Point startPoint) {
        var ship = new Ship(size, master);
        var currentPoint = startPoint;
        var addedPoints = new ArrayList<Point>();

        for (int i = 0; i < ship.getSize(); i++) {
            try {
                var flag = field[currentPoint.getY()][currentPoint.getX()].setShip(ship);
                if (!flag) {
                    rollbackShip(addedPoints);
                    return false;
                }
                addedPoints.add(currentPoint);
            } catch (IndexOutOfBoundsException e) {
                rollbackShip(addedPoints);
                return false;
            }

            currentPoint = getNextPosition(currentPoint, direction);
        }

        return true;
    }

    /** Выстрел в указанную точку
     * Не использовать вне пакета! */
    public boolean fire(Point point) {
        int x = point.getX();
        int y = point.getY();

        try {
            if (field[y][x].isEmpty()) {
                field[y][x].setMiss();
            }

            if (field[y][x].isShip()) {
                field[y][x].setHit();
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }

    /** Получение типа выбранной клетки поля */
    public CellType getCellTypeOnPosition(Point point) {
        return field[point.getY()][point.getX()].getType();
    }

    private Point getNextPosition(Point currentPoint, Direction direction) {
        switch (direction) {
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

    private void rollbackShip(List<Point> pointsToRollback) {
        for (Point point : pointsToRollback) {
            field[point.getY()][point.getX()].setEmpty();
        }
    }
}
