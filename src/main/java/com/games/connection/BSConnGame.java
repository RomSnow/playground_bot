package com.games.connection;

import com.games.battleship.BattleshipGame;
import com.games.battleship.GameParams;
import com.user.User;

public class BSConnGame implements ConnectGame{
    private BattleshipGame game;

    @Override
    public void connectUser(User firstPlayer, User secondPlayer) {
        var params = new GameParams(3, 2, 1, 0, 6);
        game = new BattleshipGame(params, firstPlayer.getUserName(), secondPlayer.getUserName());
    }

    public BattleshipGame getGame() {
        return game;
    }

    @Override
    public GameType getType() {
        return GameType.BattleShip;
    }
}
