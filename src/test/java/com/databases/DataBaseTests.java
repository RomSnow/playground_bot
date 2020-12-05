package com.databases;

import com.games.score_sheet_db.ScoreSheetConnector;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DataBaseTests {

    @Test
    public void testConnection() throws SQLException {
        var board = ScoreSheetConnector.getGameScoreSheet(0);
        for (var player : board.keySet()){
            System.out.print(player + ":" + board.get(player));
        }
    }

    @Test
    public void testSetPlayerScore() throws SQLException {
        ScoreSheetConnector.setPlayersScore("Sanya", 27);
    }

    @Test
    public void testGetPlayerPosition() throws SQLException {
        var pos = ScoreSheetConnector.getPlayerPosition("Sanya");
        System.out.print(pos);
    }

}
