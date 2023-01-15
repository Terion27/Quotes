/**
 * QuoteRepository - database quote table repository
 *
 */

package quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import quotes.models.Quote;

import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    Optional<Quote> findByQuoteIdEquals(Integer quoteId);
}
