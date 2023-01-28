package quotes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import quotes.controllers.BotController;

/**
 * Telegram Bot - https://t.me/fun_quotes_bot
 */

@Slf4j
@Service
@RequiredArgsConstructor
@SpringBootApplication
public class QuotesApplication implements CommandLineRunner {
    private final BotController botController;

    public static void main(String[] args) {
        SpringApplication.run(QuotesApplication.class, args);
    }

    @Override
    public void run(String... args) {
        TelegramBotsApi api;
        try {
            api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(botController);
        } catch (TelegramApiException e) {
            log.error("TelegramBot is not running: " + e.getMessage());
        }
    }
}
