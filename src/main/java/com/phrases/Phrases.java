package com.phrases;

public class Phrases {
    final private String[] questions = new String[] {"Что надо, хозяин?", "Чего хочешь?", "Дааа?"};
    final private String[] answers = new String[] {"Почему бы и нет.", "Угу.", "Хорошо.", "Попробуем."};
    final private String[] chooseGame = new String[] {"Во что?", "Выбирай игру.", "Выбери."};

    public Phrases() {
    }

    public String getNewUserHello() {
        return "Welcome to the club, buddy!";
    }

    public String getConnectCanceled() {
        return "Поиск отменен.";
    }

    public String getHelloStr(String userName) {
        String hiStr = "Приветствую, ";
        return hiStr + userName + '!';
    }

    public String getLetSetUserName() {
        return "Поставьте имя пользователя в настройках Telegram.";
    }

    public String getCreateGame(String id) {
        String createGame = "Игра создана. Ожидаю подключения.";
        return createGame + "\nID вашей игры: '" + id + "'.\nВведите 'Отмена' для завершения игры.";
    }

    public String getGameDoesntExist() {
        return "Игра не найдена.";
    }

    public String getFoundGame(String enemyName) {
        String foundGame = "Подключаю к игре.";
        return foundGame + "\nСоединяю с " + enemyName + "." +
                "\nДля помощи введи 'Что?'.";
    }

    public String getNotImplementStr() {
        return "Далее бот не реализован.";
    }

    public String getBSInfo() {
        return """
                Доступные команды для игры:
                -f [A-F] [1-6] : выстрел по клетке
                -s [A-F] [1-6] [1-3] [U, D, R, L] : поставить корабль с размером и направлением
                -r : сдаться""";
    }

    public String getConnectGame() {
        return "Введи id игры для подключения.\nНажмите 'Отмена' для отмены.";
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
        return "Готов вкалывать!";
    }

    public String getInfo() {
        return "Я игровой многофункциональный раб... ой, то есть бот.\n" +
                    "В данный момент ты можешь сыграть в:\n" +
                    "Упс, здесь сейчас пусто.";
    }

    public String getWaitStr() {
        return "Ждём подключения.";
    }

    public String getConnected(String enemyName) {
        return "К игре подключился " + enemyName + ".\nДля помощи введи 'Что?'.";
    }

    private int getRandom(int max)
    {
        return (int)(Math.random() * ++max);
    }
}
