package com.buttons;

public class Buttons {
    final private String begin = "Начать";
    final private String letsPlay = "Играть \uD83C\uDFAE";
    final private String info = "Информация \uD83D\uDDFF";
    final private String battleShip = "Морской бой ⚓";
    final private String createGame = "Создать игру";
    final private String connectGame = "Подключиться";
    final private String cancel = "Отмена";

    public Buttons() {
    }

    public String getCancel() {
        return cancel;
    }

    public String getBegin() {
        return begin;
    }

    public String getLetsPlay() {
        return letsPlay;
    }

    public String getInfo() {
        return info;
    }

    public String getBattleShip() {
        return battleShip;
    }

    public String getCreateGame() {
        return createGame;
    }

    public String getConnectGame() {
        return connectGame;
    }
}
