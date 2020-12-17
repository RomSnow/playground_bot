package com.games.score_sheet_db;

import com.reader.ConfigReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreSheetConnector {
    static private final String HOST = ConfigReader.getDataFromConfFile("db_host.conf");
    static private final String USER = ConfigReader.getDataFromConfFile("db_user.conf");
    static private final String PWD = ConfigReader.getDataFromConfFile("db_pwd.conf");

    public static HashMap<String, PlayerScoreData> getGameScoreSheet(int lineCount) {
        Connection connection = null;
        Statement statement = null;
        ResultSet reply = null;
        HashMap<String, PlayerScoreData> board;
        var sql = "SELECT player_name, score FROM scoreboard_bs ORDER BY score DESC";

        try {
            connection = DriverManager.getConnection(HOST, USER, PWD);
            statement = connection.createStatement();
            reply = statement.executeQuery(sql);

            board = new HashMap<>();
            for (var i = 0; i < lineCount; i++) {
                var is_next = reply.next();
                if (!is_next)
                    break;

                var name = reply.getString("player_name");
                var score = reply.getInt("score");
                board.put(name, new PlayerScoreData(i + 1, score));
            }
        } catch (SQLException throwMsg) {
            return null;
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (reply != null)
                    reply.close();
            } catch (SQLException ignore){}
        }

        return board;

    }

    public static boolean setPlayersScore(String player_name, int addScore) {
        var sql = String.format("SELECT * FROM scoreboard_bs WHERE player_name = '%s';",
                player_name);

        Connection connection = null;
        Statement statement = null;
        ResultSet reply = null;
        try {
            connection = DriverManager.getConnection(HOST, USER, PWD);
            statement = connection.createStatement();
            reply = statement.executeQuery(sql);

            var currentScore = 0;
            String newSQL;
            if (reply.next()) {
                currentScore = reply.getInt("score");
                var newScore = currentScore + addScore;
                newSQL = String.format("UPDATE scoreboard_bs SET score = %d " +
                                "WHERE player_name = '%s' AND score = %d;",
                        newScore, player_name, currentScore);
            } else {
                newSQL = String.format("INSERT INTO scoreboard_bs (player_name, score)" +
                        " VALUES('%s', %d);", player_name, addScore);
            }
            statement.executeUpdate(newSQL);
        } catch (SQLException throwMsg) {
            try {
                if (reply != null)
                    reply.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException ignored) {}
            return false;
        }

        return true;
    }

    public static PlayerScoreData getPlayerPosition(String player_name) {
        Connection connection = null;
        Statement statement = null;
        ResultSet reply = null;
        var num = 0;
        var score = 0;
        var sql = "SELECT player_name, score FROM scoreboard_bs ORDER BY score DESC";

        try {
            connection = DriverManager.getConnection(HOST, USER, PWD);
            statement = connection.createStatement();
            reply = statement.executeQuery(sql);

            var i = 1;
            num = -1;
            score = 0;
            while (reply.next()) {
                var name = reply.getString("player_name");
                if (name.equals(player_name)) {
                    num = i;
                    score = reply.getInt("score");
                    break;
                }
                i++;
            }

        } catch (SQLException throwMsg){
            return null;
        } finally {
            try {
                if (reply != null)
                    reply.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException ignored) {}
        }

        if (num == -1){
            return null;
        }
        else {
            var s = new ArrayList<Integer>();
            return new PlayerScoreData(num, score);
        }
    }

    public static boolean deletePlayerData(String playerName) {
        Connection connection = null;
        Statement statement = null;

        var pattern = "DELETE FROM scoreboard_bs WHERE player_name = '%s'";
        var sql = String.format(pattern, playerName);
        try {
            connection = DriverManager.getConnection(HOST, USER, PWD);
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException throwMsg){
            return false;
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException ignore) {}
        }

        return true;
    }
}
