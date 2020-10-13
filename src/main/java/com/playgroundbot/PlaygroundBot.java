package com.playgroundbot;

import com.games.connection.AvailableGame;
import com.phrases.Phrases;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class PlaygroundBot extends TelegramLongPollingBot {
    private static ArrayList<AvailableGame> availableGames;
    private static ArrayList<AvailableGame> startedGames;
    private String userName;
    private Long chatId;
    private String currentMessage;
    private String lastMessage;
    private boolean isAtTheGameBS;
    final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public PlaygroundBot() {
        availableGames = new ArrayList<AvailableGame>();
        startedGames = new ArrayList<AvailableGame>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        userName = update.getMessage().getFrom().getUserName();
        chatId = update.getMessage().getChatId();
        currentMessage = update.getMessage().getText();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            sendMessage.setText(getMessage(currentMessage));
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        lastMessage = currentMessage;
    }

    private String getMessage(String msg) throws TelegramApiException {
        Phrases phrases = new Phrases();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        if (msg.equals("/start")) {
            return messageHandler(new String[]{"Начать"}, phrases.getHelloStr(userName));
        }
        if (msg.equals("Начать")) {
            return messageHandler(new String[]{"Играть \uD83C\uDFAE", "Информация \uD83D\uDDFF"},
                    phrases.getQuestion());
        }
        if (msg.equals("Играть \uD83C\uDFAE") &&
                (lastMessage.equals("Начать") || lastMessage.equals("Информация \uD83D\uDDFF"))) {
            return messageHandler(new String[]{"Морской бой ⚓"}, phrases.getChooseGame());
        }
        if (msg.equals("Информация \uD83D\uDDFF") && lastMessage.equals("Начать")) {
            return messageHandler(new String[]{"Играть \uD83C\uDFAE", "Информация \uD83D\uDDFF"},
                    phrases.getInfo());
        }
        if (msg.equals("Морской бой ⚓") && lastMessage.equals("Играть \uD83C\uDFAE")) {
            return messageHandler(new String[]{"Создать игру", "Подключиться"}, phrases.getAnswer());
        }
        if (msg.equals("Создать игру") && lastMessage.equals("Морской бой ⚓")) {
            availableGames.add(new AvailableGame(chatId, "1", "BattleShip"));
            return phrases.getCreateGame();
        }
        if (msg.equals("Подключиться") && lastMessage.equals("Морской бой ⚓")) {
            return phrases.getConnectGame();
        }
        if (lastMessage.equals("Подключиться") && availableGameExist(currentMessage)) {
            SendMessage sendConnectGame = new SendMessage(availableGames.get(0).getFirstPlayerChatId(),
                    "К игре подключился " + userName + '.');
            try {
                execute(sendConnectGame);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return phrases.getFoundGame();
        }
        if (lastMessage.equals("Подключиться") && !availableGameExist(currentMessage)) {
            return phrases.getGameDoesntExist();
        }

        return messageHandler(new String[]{"Начать"}, phrases.getReadiness());
    }

    private String messageHandler(String[] buttons, String outPhrase) {
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        for (String button: buttons) {
            keyboardFirstRow.add(button);
        }
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return outPhrase;
    }

    private boolean availableGameExist(String gameId) {
        for (AvailableGame avGame: availableGames) {
            if (gameId.equals(avGame.getGameId()))
                return true;
        }
        return false;
    }

    @Override
    public String getBotUsername() {
        return "PlaygroundMasterBot";
    }

    @Override
    public String getBotToken() {
        return "1179723857:AAGNIjsk9XvwrC0mBg6-c4nti8AGuCZeBso";
    }
}