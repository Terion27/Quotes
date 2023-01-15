/**
 * ChatService - Chat layer.
 *      processingChatSession() - Search for a chat in the database and register a new chat.
 */

package quotes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quotes.models.Chat;
import quotes.repositories.ChatRepository;

@Component
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    /**
     * processingChatSession() - Search for a chat in the database and register a new chat.
     *
     * @param chatId - Chat id from telegram
     *
     * @return - current chat
     */
    public Chat processingChatSession(Long chatId) {
        var rawChat = chatRepository.findByChatIdEquals(chatId);

        return rawChat.orElseGet(() -> regNewChat(chatId));
    }

    /**
     * regNewChat() - register a new chat
     *
     * @param chatId - Chat id
     *      setLastId() - last viewed chat id
     * @return - current chat
     */
    private Chat regNewChat(Long chatId) {
        var chat = new Chat();
        chat.setChatId(chatId);
        chat.setLastId(0);

        return chatRepository.save(chat);
    }
}
