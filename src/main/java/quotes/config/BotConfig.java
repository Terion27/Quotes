/**
 * Configuration file
 */

package quotes.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
