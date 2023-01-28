package quotes.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import quotes.commands.BotCommands;
import quotes.models.*;

@Component
@RequiredArgsConstructor
public class BotService {
    private final BotCommands botCommands;
    private final Keyboard keyboard;

    public SendMessage getResponseMessage(Message message) {
        Long chatId = message.getChatId();

        SendMessage responseMessage = new SendMessage();
        if (!message.getText().isEmpty()) {
            String responseText = botCommands.runBotCommands(message.getText(), chatId);

            responseMessage.setReplyMarkup(keyboard.getKeyboard());
            responseMessage.setText(responseText);
            responseMessage.setChatId(chatId);
        }

        return responseMessage;
    }
}
