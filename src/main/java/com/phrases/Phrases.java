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

    public String getBSInfo() {
        return """
                Доступные команды для игры:
                -f [0-5] [0-5] : выстрел по клетке
                -s [0-5] [0-5] [1-3] [U, D, R, L] : поставить корабль с размером и направлением
                -m : показать свою и карту противника
                -r : сдаться
                
                Примечание: при выстреле и постановке корабля первый аргумент отвечает за выбор столбца, второй за выбор строки""";
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
        return """
                Я игровой многофункциональный раб... ой, то есть бот.
                В данный момент ты можешь сыграть в:
                Морской бой.""";
    }

    public String getWaitStr() {
        return "Ждём подключения.";
    }

    public String getConnected(String enemyName) {
        return "К игре подключился " + enemyName + ".\nДля помощи введи 'Что?'.";
    }

    public String gameIsOver() {
        return "Игра завершена!";
    }

    public String commandIsntFound() {
        return "Команда не найдена.";
    }

    public String faultInCommand() {
        return "Что-то не так с тем, что ты набрал!";
    }

    private int getRandom(int max)
    {
        return (int)(Math.random() * ++max);
    }
}
