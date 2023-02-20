package quotes.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import quotes.commands.AppBotCommands;
import quotes.commands.BotCommands;
import quotes.models.Chat;
import quotes.models.Quote;
import quotes.repositories.ChatRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static quotes.config.MessageStrings.FAILED_INVOKE_METHOD;
import static quotes.config.MessageStrings.UNKNOWN_TEAM;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final BotCommands botCommands;
    private final ChatRepository chatRepository;

    public SendMessage runBotCommands(Message message) {
        var method = Arrays.stream(botCommands.getClass().getDeclaredMethods())
                .filter(f -> f.isAnnotationPresent(AppBotCommands.class))
                .filter(m -> m.getAnnotation(AppBotCommands.class).description().equals(message.getText()))
                .findFirst();
        if (method.isEmpty()) return new SendMessage(message.getChatId().toString(), UNKNOWN_TEAM);
        try {
            method.get().setAccessible(true);
            Chat chat = chatSession(message.getChatId());
            Quote newQuote = (Quote) method.get().invoke(botCommands, chat.getLastId());
            if (newQuote.getQuoteId() != -1) saveLastId(chat, newQuote.getQuoteId());
            return new SendMessage(message.getChatId().toString(), newQuote.getText()); // setReplyMarkup(keyboard.getKeyboard();

        } catch (IllegalAccessException | InvocationTargetException e)  {
            log.error(FAILED_INVOKE_METHOD + e.getMessage());
            throw new RuntimeException(FAILED_INVOKE_METHOD, e);
        }
    }

    private void saveLastId(Chat chat, int newLastId) {
        chat.setLastId(newLastId);
        chatRepository.save(chat);
    }
    private Chat chatSession(Long chatId) {
        return chatRepository.findByChatIdEquals(chatId)
                .orElseGet(() -> regNewChat(chatId));
    }
    private Chat regNewChat(Long chatId) {
        return chatRepository.save(new Chat(chatId, 0));
    }
}
