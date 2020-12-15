package com.phrases;

public class BSPhrases {
    public BSPhrases() {
    }

    public static String getInfo() {
        return """
                Доступные команды для игры:
                -f [0-5] [0-5] : выстрел по клетке
                -s [0-5] [0-5] [1-3] [U, D, R, L] : поставить корабль с размером и направлением
                -m : показать свою и карту противника
                -r : сдаться
                
                Примечание: при выстреле и постановке корабля первый аргумент отвечает за выбор столбца, второй за выбор строки""";
    }

    public static String getLetShoot() {
        return "Твой ход. Стреляй!";
    }

    public static String getShootStat(String hor, String ver) {
        return "Выстрел по " + hor + " " + ver;
    }

    public static String getSetShipStat(String hor, String ver, String size, String dir) {
        return "Ставлю корабль на " + hor + " " + ver + "\n" + "size " + size + "\ndirection " + dir;
    }

    public static String getMaps(String yours, String enemys) {
        return "Ваша карта:\n" + yours + "\nКарта противника:\n" + enemys;
    }
}
