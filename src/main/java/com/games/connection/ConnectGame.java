package com.games.connection;

import com.games.IGame;
import com.user.User;

public interface ConnectGame {
    void connectUser(User firstPlayer, User secondPlayer);
    IGame getGame();
    GameType getType();
}
