/**
 * Configuration file
 */

package quotes.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Slf4j
public class BotConfig {

    private static final String BOT_CONFIG_FILE = "d:\\bot_citatnik.txt";
    private final String BotUsername;
    private final String BotToken;

    public BotConfig() {
        try {
            Scanner scanFile = new Scanner(new FileInputStream(BOT_CONFIG_FILE));
            this.BotUsername = scanFile.nextLine();
            this.BotToken = scanFile.nextLine();
        } catch (FileNotFoundException e) {
            log.error("Configuration file is missing: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getBotName() {
        return this.BotUsername;
    }

    public String getToken() {
        return this.BotToken;
    }
}
