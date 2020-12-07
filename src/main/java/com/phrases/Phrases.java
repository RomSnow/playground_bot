package com.phrases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Phrases {
    final static private String[] questions = new String[] {"Что надо, хозяин?", "Чего хочешь?", "Дааа?"};
    final static private String[] answers = new String[] {"Почему бы и нет.", "Угу.", "Хорошо.", "Попробуем."};
    final static private String[] chooseGame = new String[] {"Во что?", "Выбирай игру.", "Выбери."};

    public Phrases() {
    }

    public static String getMistake() {
        return "Ошибка!";
    }

    public static String getNewUserHello() {
        return "Welcome to the club, buddy!";
    }

    public static String getConnectCanceled() {
        return "Поиск отменен.";
    }

    public static String getLetSetUserName() {
        return "Поставьте имя пользователя в настройках Telegram.";
    }

    public static String getCreateGame(String id) {
        String createGame = "Игра создана. Ожидаю подключения.";
        return createGame + "\nID вашей игры: '" + id + "'.\nВведите 'Отмена' для завершения игры.";
    }

    public static String getGameDoesntExist() {
        return "Игра не найдена.";
    }

    public static String getFoundGame(String enemyName) {
        String foundGame = "Подключаю к игре.";
        return foundGame + "\nСоединяю с " + enemyName + "." +
                "\nДля помощи введи 'Что?'.";
    }

    public static String getBSInfo() {
        return """
                Доступные команды для игры:
                -f [0-5] [0-5] : выстрел по клетке
                -s [0-5] [0-5] [1-3] [U, D, R, L] : поставить корабль с размером и направлением
                -m : показать свою и карту противника
                -r : сдаться
                
                Примечание: при выстреле и постановке корабля первый аргумент отвечает за выбор столбца, второй за выбор строки""";
    }

    public static String getConnectGame() {
        return "Введи id игры для подключения.\nНажмите 'Отмена' для отмены.";
    }

    public static String getQuestion() {
        return questions[getRandom(questions.length - 1)];
    }

    public static String getAnswer() {
        return answers[getRandom(answers.length - 1)];
    }

    public static String getChooseGame() {
        return chooseGame[getRandom(chooseGame.length - 1)];
    }

    public static String getReadiness() {
        return "Готов вкалывать!";
    }

    public static String getInfo() {
        return """
                Я игровой многофункциональный раб... ой, то есть бот.
                В данный момент ты можешь сыграть в:
                Морской бой.""";
    }

    public static String getWaitStr() {
        return "Ждём подключения.";
    }

    public static String getConnected(String enemyName) {
        return "К игре подключился " + enemyName + ".\nДля помощи введи 'Что?'.";
    }

    public static String gameIsOver() {
        return "Игра завершена!";
    }

    public static String getCommandIsntFound() {
        return "Команда не найдена.";
    }

    public static String getFaultInCommand() {
        return "Что-то не так с тем, что ты набрал!";
    }

    public static String getNotYourQueue() {
        return "Ход противника!";
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

    public static String getWhiteFlag() {
        return "\uD83C\uDFF3";
    }

    public static String getBoom() {
        return "\uD83D\uDCA5";
    }

    public static String getLeaderboard(HashMap<String, Integer> lb, ArrayList<Integer> pos) {
        var sb = new StringBuilder();
        var tm = new TreeMap<Integer, String>();
        for (String name: lb.keySet()) {
            tm.put(lb.get(name), name);
        }
        var i = 1;
        for (Integer points: tm.descendingKeySet()) {
            sb.append(i).append(" место - ").append(tm.get(points))
                    .append(", у которого набрано: ").append(points).append(" очков\n");
            i++;
        }
        if (pos != null)
            sb.append("Ваша позиция - ").append(pos.get(0)).append(". Ваши очки - ").append(pos.get(1));
        else
            sb.append("У вас не набрано очков в играх");
        return sb.toString();
    }

    private static int getRandom(int max)
    {
        return (int)(Math.random() * ++max);
    }
}
