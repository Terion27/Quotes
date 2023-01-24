package quotes.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import quotes.commands.BotCommands;
import quotes.models.*;

@Component
public class BotService {

    @Autowired
    private final ChatService chatService;

    @Autowired
    private final BotCommands botCommands;

    public BotService(ChatService chatService, BotCommands botCommands) {
        this.chatService = chatService;
        this.botCommands = botCommands;
    }

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
