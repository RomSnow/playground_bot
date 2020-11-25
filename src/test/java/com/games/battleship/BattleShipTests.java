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
        var game = new BattleshipGame(5,
                "first", "second");
        var currentField = game.getCurrentPlayerField("first");
        var newField = game.getEnemyField("second");

        assertEquals(currentField, newField);
    }

    @Test
    public void testSetShipAndHit(){
        var game = new BattleshipGame(5,
                "first", "second");
        var zeroPoint = new Point(0, 0);
        game.setShip("first",3, zeroPoint, Direction.Right);
        game.makeHit("first", zeroPoint);

        assertSame(game.getCurrentPlayerField("first").
                getCellTypeOnPosition(zeroPoint), CellType.Ship);
        assertSame(game.getEnemyField("first").getCellTypeOnPosition(zeroPoint),
                CellType.Miss);

        game.makeHit("second", zeroPoint);

        assertSame(game.getEnemyField("second").getCellTypeOnPosition(zeroPoint),
                CellType.Hit);
    }

    @Test
    public void testIncorrectMove(){
        var game = new BattleshipGame(5, "first", "second");

        assertFalse(game.makeHit("first", new Point(6,6)));
        assertFalse(game.setShip("first", 5, new Point(0, 0), Direction.Up));
        assertFalse(game.setShip("first", 5, new Point(-1, -1), Direction.Down));

    }

}



