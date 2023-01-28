/**
 * ChatService - Chat layer.
 *      processingChatSession() - Search for a chat in the database and register a new chat.
 *      regNewChat() - register a new chat
 */

package quotes.services;

import org.springframework.stereotype.Component;
import quotes.models.Chat;
import quotes.repositories.ChatRepository;

@Component
public class ChatService {
   private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat processingChatSession(Long chatId) {
        var rawChat = chatRepository.findByChatIdEquals(chatId);

        return rawChat.orElseGet(() -> regNewChat(chatId));
    }

    private Chat regNewChat(Long chatId) {
        var chat = new Chat();
        chat.setChatId(chatId);
        chat.setLastId(0);

        return chatRepository.save(chat);
    }
}
