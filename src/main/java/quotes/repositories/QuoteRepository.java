package quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quotes.models.Quote;

import java.util.Optional;

/**
 * QuoteRepository - database quote table repository
 *
 */
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    Optional<Quote> findByQuoteIdEquals(Integer quoteId);
    Optional<Quote> findFirstByQuoteIdGreaterThanOrderByQuoteIdAsc(Integer quoteId);
    Optional<Quote> findFirstByQuoteIdLessThanOrderByQuoteIdDesc(Integer quoteId);
}
