package com.games.battleship;

import org.junit.Test;

import static org.junit.Assert.*;

public class BattleShipTests {

    @Test
    public void testSetShip() {
        var player = new Player(5);
        var field = player.getPlayerField();

        //creation test
        field.setShipOnPosition(3, Direction.Down,
                new Point(0, 0));
        for (var i = 0; i < 3; i++) {
            assertEquals(field.getCellTypeOnPosition(new Point(0, i)), CellType.Ship);
        }

        //test crossing
        assertFalse(field.setShipOnPosition(4, Direction.Left, new Point(3, 1)));
    }

    @Test
    public void testDirectionsSetShip() {
        var player = new Player(20);
        var field = player.getPlayerField();

        player.setShip(new Point(0, 0), Direction.Down, 2);
        assertEquals(CellType.Ship, field.getCellTypeOnPosition(new Point(0, 1)));

        player.setShip(new Point(19, 0), Direction.Left, 2);
        assertEquals(CellType.Ship, field.getCellTypeOnPosition(new Point(18, 0)));

        player.setShip(new Point(0, 19), Direction.Right, 2);
        assertEquals(CellType.Ship, field.getCellTypeOnPosition(new Point(1, 19)));

        player.setShip(new Point(19, 19), Direction.Up, 2);
        assertEquals(CellType.Ship, field.getCellTypeOnPosition(new Point(19, 18)));
    }

    @Test
    public void testFire() {
        var player = new Player(5);
        var field = player.getPlayerField();

        player.setShip(new Point(0, 0), Direction.Down, 3);
        for (var i = 0; i < 3; i++)
            field.fire(new Point(0, i));

        assertEquals(player.getShipsCount(), player.getShipsCount());

        for (var i = 0; i < 3; i++)
            assertEquals(field.getCellTypeOnPosition(new Point(0, i)), CellType.Hit);

    }

    @Test
    public void testSwitchPlayer() {
        var game = new BattleshipGame(5);
        var currentField = game.getCurrentPlayerField();
        game.switchPlayer();
        var newField = game.getEnemyField();

        assertEquals(currentField, newField);
    }

    @Test
    public void testSetShipAndHit(){
        var game = new BattleshipGame(5);
        var zeroPoint = new Point(0, 0);
        game.setShip(3, zeroPoint, Direction.Right);
        game.makeHit(zeroPoint);

        assertSame(game.getCurrentPlayerField().
                getCellTypeOnPosition(zeroPoint), CellType.Ship);
        assertSame(game.getEnemyField().getCellTypeOnPosition(zeroPoint),
                CellType.Miss);

        game.switchPlayer();
        game.makeHit(zeroPoint);

        assertSame(game.getEnemyField().getCellTypeOnPosition(zeroPoint),
                CellType.Hit);
    }

    @Test
    public void testIncorrectMove(){
        var game = new BattleshipGame(5);

        assertFalse(game.makeHit(new Point(6,6)));
        assertFalse(game.setShip(5, new Point(0, 0), Direction.Up));
        assertFalse(game.setShip(5, new Point(-1, -1), Direction.Down));

    }

}



