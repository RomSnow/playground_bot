package com.games.score_sheet_db;

public class PlayerScoreData {

    private final int rating;
    private final int score;

    public PlayerScoreData(int rating, int score){
        this.rating = rating;
        this.score = score;
    }

    public int getRating() {
        return rating;
    }

    public int getScore() {
        return score;
    }
}
