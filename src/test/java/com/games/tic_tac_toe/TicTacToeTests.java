package com.games.tic_tac_toe;

import com.games.battleship.Point;
import org.junit.Test;

import static org.junit.Assert.*;

public class TicTacToeTests {
    @Test
    public void testFieldSetAndCheck(){
        var field = new TicTacField();
        var point = new Point(0, 0);

        var currentChar = field.getCharOnPosition(point);
        assertEquals(CharType.Empty, currentChar);

        var isCorrect = field.setCharOnPosition(CharType.X, point);
        assertTrue(isCorrect);

        currentChar = field.getCharOnPosition(point);
        assertEquals(CharType.X, currentChar);

    }

    @Test
    public void testCheckOnCharWinByLine(){
        var field = new TicTacField();

        assertFalse(field.checkOnCharWin(CharType.X));
        for (var i = 0; i < 3; i++){
            field.setCharOnPosition(CharType.X, new Point(0, i));
        }

        assertTrue(field.checkOnCharWin(CharType.X));
    }

    @Test
    public void testCheckOnCharWinByDiagonal(){
        var field = new TicTacField();

        for (var i = 0; i < 3; i++)
            field.setCharOnPosition(CharType.X, new Point(2 - i, i));

        assertTrue(field.checkOnCharWin(CharType.X));
    }

    @Test
    public void testIncorrectMove(){
        var field = new TicTacField();
        var isCorrect = field.setCharOnPosition(CharType.X, new Point(3, 0));
        assertFalse(isCorrect);

        field.setCharOnPosition(CharType.X, new Point(0, 0));
        isCorrect = field.setCharOnPosition(CharType.O, new Point(0, 0));
        assertFalse(isCorrect);

    }

    @Test
    public void testPlayerWin() throws GameIsEndException {
        var game = new TicTacToeGame("o", "x");
        for (var i = 0; i < 3; i++)
            game.setCharOnPosition("o", new Point(0, i));

        assertTrue(game.isGameEnd());
        assertEquals(game.getWinPlayerName(), "o");
    }

    @Test
    public void testGameIsEndException() throws GameIsEndException {
        var game = new TicTacToeGame("o", "x");
        for (var i = 0; i < 3; i++)
            game.setCharOnPosition("o", new Point(0, i));

        var isExceptionCatch = false;
        try {
            game.setCharOnPosition("x", new Point(1, 1));
        } catch (GameIsEndException throwMsg){
            isExceptionCatch = true;
        }

        assertTrue(isExceptionCatch);
    }

}
