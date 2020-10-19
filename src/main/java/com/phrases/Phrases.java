package com.phrases;

public class Phrases {
    final private String[] questions = new String[] {"Что надо, хозяин?", "Чего хочешь?", "Дааа?"};
    final private String[] answers = new String[] {"Почему бы и нет.", "Угу.", "Хорошо.", "Попробуем."};
    final private String[] chooseGame = new String[] {"Во что?", "Выбирай игру.", "Выбери."};
    final private String readiness = "Готов вкалывать!";
    final private String information = "Я игровой многофункциональный раб... ой, то есть бот.\n" +
                               "В данный момент ты можешь сыграть в:\n" +
                               "Упс, здесь сейчас пусто.";
    final private String waitStr = "Ждём подключения.";
    final private String hiStr = "Приветствую, ";
    final private String notImplementStr = "Далее бот не реализован.";
    final private String connectGame = "Введи id игры для подключения.\nНажмите 'Отмена' для отмены.";
    final private String gameDoesntExist = "Игра не найдена.";
    final private String foundGame = "Подключаю к игре.";
    final private String createGame = "Игра создана. Ожидаю подключения.";
    final private String letSetUserName = "Поставьте имя пользователя в настройках Telegram.";
    final private String newUserHello = "Welcome to the club, buddy!";
    final private String connectCanceled = "Поиск отменен.";

    public Phrases() {
    }

    public String getNewUserHello() {
        return newUserHello;
    }

    public String getConnectCanceled() {
        return connectCanceled;
    }

    public String getHelloStr(String userName) {
        return hiStr + userName + '!';
    }

    public String getLetSetUserName() {
        return letSetUserName;
    }

    public String getCreateGame(String id) {
        return createGame + "\nID вашей игры: '" + id + "'.\nВведите 'Отмена' для завершения игры.";
    }

    public String getGameDoesntExist() {
        return gameDoesntExist;
    }

    public String getFoundGame(String enemyName) {
        return foundGame + "\nСоединяю с " + enemyName + "'.\nВведите 'Отмена' для завершения игры." +
                "\nДля помощи введи 'Что?'.";
    }

    public String getNotImplementStr() {
        return notImplementStr;
    }

    public String getConnectGame() {
        return connectGame;
    }

    public String getQuestion() {
        return questions[getRandom(questions.length - 1)];
    }

    public String getAnswer() {
        return answers[getRandom(answers.length - 1)];
    }

    public String getChooseGame() {
        return chooseGame[getRandom(chooseGame.length - 1)];
    }

    public String getReadiness() {
        return readiness;
    }

    public String getInfo() {
        return information;
    }

    public String getWaitStr() {
        return waitStr;
    }

    private int getRandom(int max)
    {
        return (int)(Math.random() * ++max);
    }
}
