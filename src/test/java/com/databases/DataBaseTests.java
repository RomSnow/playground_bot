package com.databases;

import com.games.score_sheet_db.ScoreSheetConnector;
import org.junit.Test;

import static org.junit.Assert.*;

//Запускать только все тесты сразу
public class DataBaseTests {

    @Test
    public void testDatabase(){
        testSetPlayerScore();
        testGetPlayerPosition();
        testDeletePlayerScore();
    }

    public void testSetPlayerScore() {
        var isCorrect = ScoreSheetConnector.setPlayersScore("Test",
                100000);
        assertTrue(isCorrect);
    }

    public void testGetPlayerPosition() {
        var playerData = ScoreSheetConnector.getPlayerPosition("Test");
        assertNotNull(playerData);
        long rate = playerData.getRating();
        long score = playerData.getScore();
        assertEquals(1, rate);
        assertEquals(100000, score);
    }

    public void testDeletePlayerScore() {
        var isCorrect = ScoreSheetConnector.deletePlayerData("Test");
        assertTrue(isCorrect);

        var pos = ScoreSheetConnector.getPlayerPosition("Test");
        assertNull(pos);
    }

}
