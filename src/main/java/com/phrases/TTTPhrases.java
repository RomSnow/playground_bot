package com.phrases;

public class TTTPhrases {
    public TTTPhrases() {
    }

    public static String getInfo() {
        return """
                Доступные команды для игры:
                -s [0-2] [0-2] : поставить крестик/нолик
                -m : показать карту
                
                Примечание: при постановке символа первый аргумент отвечает за выбор столбца, второй за выбор строки""";
    }

    public static String nextTurn() {
        return "Твой ход!";
    }

    public static String endTurn() {
        return "Хороший ход!";
    }

    public static String fieldAlready() {
        return "Эта ячейка занята.";
    }

    public static String getMap(String map) {
        return "Поле \n" + map;
    }
}
