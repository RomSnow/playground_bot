package com.phrases;

public class Phrases {
    final String[] questions;
    final String[] answers;
    final String[] chooseGame;
    final String readiness;
    final String information;
    final String waitStr;
    final String hiStr;
    final String notImplementStr;

    public Phrases() {
        hiStr = "Приветствую, ";
        questions = new String[] {"Что надо, хозяин?", "Чего хочешь?", "Дааа?"};
        answers = new String[] {"Почему бы и нет.", "Угу.", "Хорошо.", "Попробуем."};
        chooseGame = new String[] {"Во что?", "Выбирай игру.", "Выбери."};
        readiness = "Готов вкалывать!";
        information = "Я игровой многофункциональный раб... ой, то есть бот.\n" +
                "В данный момент ты можешь сыграть в:\n" +
                "Упс, здесь сейчас пусто.";
        waitStr = "Ждём подключения.";
        notImplementStr = "Далее бот не реализован.";
        //Список игр сюда как-нибудь присобачить
    }

    public String getHelloStr(String userName) {
        return hiStr + userName + '!';
    }

    public String getNotImplementStr() {
        return notImplementStr;
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
