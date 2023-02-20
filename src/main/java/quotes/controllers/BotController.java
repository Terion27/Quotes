package quotes.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import quotes.services.MessageService;

import static quotes.config.MessageStrings.COMMAND_EXPECTED;
import static quotes.config.MessageStrings.MESSAGE_IS_NOT_SENDING;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotController extends TelegramLongPollingBot {

    private final MessageService messageService;

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_NAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(getResponseMessage(update.getMessage()));
        } catch (TelegramApiException e) {
            log.error(MESSAGE_IS_NOT_SENDING + e.getMessage());
            throw new RuntimeException(MESSAGE_IS_NOT_SENDING, e);
        }
    }

    public SendMessage getResponseMessage(Message message) {
        if (!message.getText().isEmpty()) return messageService.runBotCommands(message);

        return new SendMessage(message.getChatId().toString(), COMMAND_EXPECTED);
    }
}
