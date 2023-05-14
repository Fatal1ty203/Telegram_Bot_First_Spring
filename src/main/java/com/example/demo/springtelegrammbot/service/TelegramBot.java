package com.example.demo.springtelegrammbot.service;

import com.example.demo.springtelegrammbot.config.BotConfig;
import com.example.demo.springtelegrammbot.model.User;
import com.example.demo.springtelegrammbot.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.currentTimeMillis;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    private final BotConfig config;

    static final String HELP_TEXT = "тестовый бот" +"\n" +
            "Type /start to see a welcome message";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "user info"));
        listOfCommands.add(new BotCommand("/deletedata", "deleted my data"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        listOfCommands.add(new BotCommand("/settings", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(),null));
        } catch (TelegramApiException e) {
            log.error("Error setting bots command list: " +e.getMessage());
            System.err.println(e.getMessage());
        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
//        if (update.getMessage().hasDocument()){
//            Document document = update.getMessage().getDocument();
//            document
//        }
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText){
                case "/start" :
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                case "/help" : sendMessage(chatId, HELP_TEXT);
                break;
                default: log.info("Этот, " +update.getMessage().getChat().getFirstName() + ", нехороший человек, пытался ввести: " + messageText);
                sendMessage(chatId, "Сорян брат, я еще тупой, но скоро Скайнет прокачает меня и я пойму что ты от меня хочешь");
            }

        }
    }

    private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()){
            long chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setChatID(chat.getId());
            user.setUserName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            long l = currentTimeMillis();
            user.setRegistration(new Timestamp(l));
            userRepository.save(user);
            log.info("User registration: " + user);
        }
    }

    private void startCommandReceived(long chatId, String name){
        String answer = "Hi, " + name + ", как жизнь, епте?";

        log.info("Replied to user " + name);

        sendMessage(chatId,answer);
    }


    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " +e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
