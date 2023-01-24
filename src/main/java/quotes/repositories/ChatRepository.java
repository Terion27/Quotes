package quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import quotes.models.Chat;

import java.util.Optional;

/**
 * ChatRepository - database chat table repository
 *
 */

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatIdEquals(Long chatId);
}
