package quotes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import quotes.config.BotConfig;
import quotes.services.BotService;

@Service
public class BotController extends TelegramLongPollingBot {

    @Autowired
    BotService botService;

    BotConfig botConfig = new BotConfig();

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        try {
            SendMessage responseMessage = botService.getResponseMessage(message);
            if (responseMessage != null) {
                execute(responseMessage);
            }
        } catch (TelegramApiException ignored) {

        }

    }
}
