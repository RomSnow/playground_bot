package com.games.score_sheet_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ScoreSheetConnector {
    static private final String HOST = "jdbc:mysql://f0493502.xsph.ru/f0493502_kn204?serverTimezone=Europe/Moscow";
    static private final String USER = "f0493502_telegrambot";
    static private final String PWD = "VovaRoma204";

    public static HashMap<String, Integer> getGameScoreSheet(int lineCount)
            throws SQLException {
        var connection = DriverManager.getConnection(HOST, USER, PWD);
        var sql = "SELECT player_name, score FROM scoreboard_bs ORDER BY score DESC";
        var statement = connection.createStatement();
        
        var reply = statement.executeQuery(sql);
        
        var board = new HashMap<String, Integer>();
        for (var i = 0; i < lineCount; i++){
            var is_next = reply.next();
            if (!is_next)
                break;

            var name = reply.getString("player_name");
            var score = reply.getInt("score");
            board.put(name, score);
        }
        
        reply.close();
        statement.close();
        connection.close();

        return board;
    }

    public static void setPlayersScore(String player_name, int score)
            throws SQLException {
        var connection = DriverManager.getConnection(HOST, USER, PWD);
        var sql = String.format("SELECT * FROM scoreboard_bs WHERE player_name = '%s';", player_name);
        var statement = connection.createStatement();

        var reply = statement.executeQuery(sql);
        String newSQL;
        if (reply.next()){
            newSQL = String.format("UPDATE scoreboard_bs SET score = %d " +
                    "WHERE player_name = '%s' AND score < %d;", score, player_name, score);
        }
        else {
            newSQL = String.format("INSERT INTO scoreboard_bs (player_name, score)" +
                    " VALUES('%s', %d);", player_name, score);
        }
        reply.close();

        statement.executeUpdate(newSQL);
        statement.close();
        connection.close();
    }

    public static int getPlayerPosition(String player_name) throws SQLException {
        var connection = DriverManager.getConnection(HOST, USER, PWD);
        var statement = connection.createStatement();
        var sql = "SELECT player_name, score FROM scoreboard_bs ORDER BY score DESC";

        var reply = statement.executeQuery(sql);

        var i = 1;
        var num = -1;
        while (reply.next()){
            var name = reply.getString("player_name");
            if (name.equals(player_name)){
                num = i;
                break;
            }
            i++;
        }

        reply.close();
        statement.close();
        connection.close();

        return num;
    }

}
