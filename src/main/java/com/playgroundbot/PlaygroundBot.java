package com.playgroundbot;

import com.phrases.Phrases;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class PlaygroundBot extends TelegramLongPollingBot {
    private long chatId;
    private String lastMessage;
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            sendMessage.setText(getMessage(text));
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        lastMessage = text;
//        try {
//            if (update.hasMessage() && update.getMessage().hasText()) {
//                Message inMessage = update.getMessage();
//                User user = inMessage.getFrom();
//                SendMessage outMessage = new SendMessage();
//                outMessage.setChatId(inMessage.getChatId());
//                outMessage.setText("Привет, " + user.getFirstName() + '!');
//                execute(outMessage);
//            }
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }

    private String getMessage(String msg) {
        Phrases phrases = new Phrases();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        if (msg.equals("/start") || msg.equals("Начать")) {
            return messageHandler(new String[]{"Играть \uD83C\uDFAE", "Информация \uD83D\uDDFF"},
                    phrases.getQuestion());
        }
        if (msg.equals("Играть \uD83C\uDFAE") && lastMessage.equals("Начать")) {
            return messageHandler(new String[]{"Морской бой ⚓"}, phrases.getChooseGame());
        }
        if (msg.equals("Информация \uD83D\uDDFF") && lastMessage.equals("Начать")) {
            return messageHandler(new String[]{"Начать"}, phrases.getInfo());
        }
        //Со следующим блоком ответа и предстоит работа
        if (msg.equals("Морской бой ⚓") && lastMessage.equals("Играть \uD83C\uDFAE")) {
            return messageHandler(new String[]{"Морской бой ⚓"}, phrases.getAnswer());
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

    @Override
    public String getBotUsername() {
        return "PlaygroundMasterBot";
    }

    @Override
    public String getBotToken() {
        return "1179723857:AAGNIjsk9XvwrC0mBg6-c4nti8AGuCZeBso";
    }
}