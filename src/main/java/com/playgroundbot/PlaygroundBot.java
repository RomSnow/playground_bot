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
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        if (msg.equals("/start") || msg.equals("Начать")) {
            keyboardFirstRow.add("Играть");
            keyboardFirstRow.add("Информация");
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return phrases.getQuestion();
        }
        if (msg.equals("Играть") && lastMessage.equals("Начать")) {
            keyboardFirstRow.add("Морской бой");
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return phrases.getChooseGame();
        }
        if (msg.equals("Информация") && lastMessage.equals("Начать")) {
            keyboardFirstRow.add("Начать");
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return phrases.getInfo();
        }
        if (msg.equals("Морской бой") && lastMessage.equals("Играть")) {
            keyboardFirstRow.add("Морской бой");
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return phrases.getAnswer();
        }
        keyboardFirstRow.add("Начать");
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return phrases.getReadiness();
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