package quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quotes.models.Chat;

import java.util.Optional;

/**
 * ChatRepository - database chat table repository
 *
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatIdEquals(Long chatId);
}
