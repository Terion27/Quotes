package quotes.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import quotes.config.BotConfig;
import quotes.services.BotService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotController extends TelegramLongPollingBot {
    private final BotService botService;
    BotConfig botConfig = new BotConfig(botUsername, botToken);

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
        } catch (TelegramApiException e) {
            log.error("Message is not sending: " + e.getMessage());
        }

    }
}
