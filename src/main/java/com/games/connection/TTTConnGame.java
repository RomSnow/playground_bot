package com.games.connection;

import com.games.tic_tac_toe.TicTacToeGame;
import com.user.User;

public class TTTConnGame implements ConnectGame{
    private TicTacToeGame game;

    @Override
    public void connectUser(User firstPlayer, User secondPlayer) {
        game = new TicTacToeGame(firstPlayer.getUserName(), secondPlayer.getUserName());
    }

    public TicTacToeGame getGame() {
        return game;
    }

    @Override
    public GameType getType() {
        return GameType.TicTacToe;
    }
}
