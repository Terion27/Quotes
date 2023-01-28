package quotes.models;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import quotes.commands.BotCommands;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class Keyboard {
    private final BotCommands botCommands;

    public ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setKeyboard(getKeyboardRows(botCommands.getListBotCommands()));
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setInputFieldPlaceholder("press button or input command");

        return replyKeyboardMarkup;
    }

    private ArrayList<KeyboardRow> getKeyboardRows(ArrayList<String> listBotCommands) {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        int comCount = listBotCommands.size();
        int colCount = 2;
        int rowsCount = comCount / colCount + ((comCount % colCount == 0) ? 0 : 1);
        for (int rowInd = 0; rowInd < rowsCount; rowInd++) {
            KeyboardRow row = new KeyboardRow();
            for (int colInd = 0; colInd < colCount; colInd++) {
                int index = rowInd * colCount + colInd;
                if (index >= comCount) continue;
                KeyboardButton keyboardButtons = new KeyboardButton(listBotCommands.get(index));
                row.add(keyboardButtons);
            }
            keyboardRows.add(row);
        }
        return keyboardRows;
    }
}
