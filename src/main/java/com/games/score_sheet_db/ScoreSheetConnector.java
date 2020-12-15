package com.games.score_sheet_db;

import com.reader.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreSheetConnector {
    static private final String HOST = ConfigReader.getDataFromConfFile("db_host.conf");
    static private final String USER = ConfigReader.getDataFromConfFile("db_user.conf");
    static private final String PWD = ConfigReader.getDataFromConfFile("db_pwd.conf");

    private static Connection setConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(HOST, USER, PWD);
        } catch (SQLException throwMsg) {
            return null;
        }
        return connection;
    }

    private static Statement setStatement(Connection connection) {
        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException throwMsg) {
            try {
                connection.close();
            } catch (SQLException ignored) {}
            return null;
        }
        return statement;
    }


    public static HashMap<String, PlayerScoreData> getGameScoreSheet(int lineCount) {
        Connection connection = setConnection();
        if (connection == null)
            return null;

        Statement statement = setStatement(connection);
        if (statement == null)
            return null;

        HashMap<String, PlayerScoreData> board;
        var sql = "SELECT player_name, score FROM scoreboard_bs ORDER BY score DESC";
        try {
            var reply = statement.executeQuery(sql);

            board = new HashMap<String, PlayerScoreData>();
            for (var i = 0; i < lineCount; i++) {
                var is_next = reply.next();
                if (!is_next)
                    break;

                var name = reply.getString("player_name");
                var score = reply.getInt("score");
                board.put(name, new PlayerScoreData(i + 1, score));
            }
            reply.close();
        } catch (SQLException throwMsg) {
            return null;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ignore){}
        }

        return board;

    }

    public static boolean setPlayersScore(String player_name, int addScore) {
        var sql = String.format("SELECT * FROM scoreboard_bs WHERE player_name = '%s';",
                player_name);

        Connection connection = setConnection();
        if (connection == null)
            return false;

        Statement statement = setStatement(connection);
        if (statement == null)
            return false;

        try {
            var reply = statement.executeQuery(sql);
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
            reply.close();
        } catch (SQLException throwMsg) {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ignored) {}
            return false;
        }

        return true;
    }

    /**
     * Возвращает ArrayList с номером игрока в рейтинге и количеством его очков
     * */
    public static PlayerScoreData getPlayerPosition(String player_name) {
        Connection connection = setConnection();
        if (connection == null)
            return null;

        Statement statement = setStatement(connection);
        if (statement == null)
            return null;

        var num = 0;
        var score = 0;
        try {
            var sql = "SELECT player_name, score FROM scoreboard_bs ORDER BY score DESC";

            var reply = statement.executeQuery(sql);

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

            reply.close();
        } catch (SQLException throwMsg){
            return null;
        } finally {
            try {
                statement.close();
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
        Connection connection = setConnection();
        if (connection == null)
            return false;

        Statement statement = setStatement(connection);
        if (statement == null)
            return false;

        var pattern = "DELETE FROM scoreboard_bs WHERE player_name = '%s'";
        var sql = String.format(pattern, playerName);
        try {
            statement.executeUpdate(sql);
        } catch (SQLException throwMsg){
            return false;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ignore) {}
        }

        return true;
    }
}
