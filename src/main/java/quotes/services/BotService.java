package quotes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import quotes.commands.BotCommands;
import quotes.models.*;

@Component
public class BotService {

    @Autowired
    ChatService chatService;

    @Autowired
    BotCommands botCommands;

    public SendMessage getResponseMessage(Message message) {
        Long chatId = message.getChatId();
        Keyboard keyboard = new Keyboard();
        Chat chat = chatService.processingChatSession(chatId);

        if (message.getText().isEmpty()) return null;
        SendMessage responseMessage = new SendMessage();
        String responseText = botCommands.runBotCommands(message.getText(), chat);
        if (responseText.isEmpty()) return null;

        responseMessage.setReplyMarkup(keyboard.getKeyboard());
        responseMessage.setText(responseText);
        responseMessage.setChatId(chatId);

        return responseMessage;
    }
}
