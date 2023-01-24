package quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import quotes.models.Quote;

import java.util.Optional;

/**
 * QuoteRepository - database quote table repository
 *
 */

public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    Optional<Quote> findByQuoteIdEquals(Integer quoteId);
}
