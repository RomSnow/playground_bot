package com.games.battleship;

import org.junit.Test;

import static org.junit.Assert.*;

public class BattleShipTests {

    @Test
    public void testSetShip() {
        var field = new Field(5);

        //creation test
        field.setFieldOnPosition(new Ship(3), Direction.Down, new Point(0, 0));
        for (var i = 0; i < 3; i++) {
            assertEquals(field.getCellTypeOnPosition(new Point(0, i)), CellType.Ship);
        }

        //test crossing
        assertFalse(field.setFieldOnPosition(new Ship(4), Direction.Left, new Point(3, 1)));
    }

    @Test
    public void testFire() {
        var field = new Field(5);
        var ship = new Ship(3);
        field.setFieldOnPosition(ship, Direction.Down, new Point(0, 0));
        for (var i = 0; i < 3; i++)
            field.fire(new Point(0, i));

        assertTrue(ship.isShipDestroy());

        for (var i = 0; i < 3; i++)
            assertEquals(field.getCellTypeOnPosition(new Point(0, i)), CellType.Hit);

    }

}
