package quotes.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quotes.models.*;
import quotes.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * QuoteService - Quote layer.
 *   Main methods of the layer:
 *      getNextQuote() - getting the next quote by id.
 *      getPrevQuote() - getting the previous quote by id.
 *      getRandQuote() - getting a random quote.
 *      getPage() - getting a quotes page by number.
 *   Auxiliary methods of the layer.
 *      getQuote() - loading quote from database.
 *      getRandomQuote() - loading quote from database.
 *      saveQuoteRepository() - writing a quote to the repository.
 *      saveChatRepository()- writing a chat to the repository.
 */



@Service
@RequiredArgsConstructor
public class QuoteService {

    private final BashParser parser;
    private final QuoteRepository quoteRepository;
    private final ChatRepository chatRepository;

    public String getNextQuote(Chat chat) {
        Quote quote = null;
        int nextIdQuote = chat.getLastId();
        while (quote == null) {
            nextIdQuote = (nextIdQuote < 2000) ? ++nextIdQuote : 0;
            quote = getQuote(nextIdQuote);
        }
        saveChatRepository(chat, quote.getQuoteId());
        return quote.getText();
    }

    public String getPrevQuote(Chat chat) {
        Quote quote = null;
        int newId = chat.getLastId();
        while (quote == null) {
            newId--;
            if (newId < 2) newId = 2000;
            quote = getQuote(newId);
        }
        saveChatRepository(chat, quote.getQuoteId());
        return quote.getText();
    }

    public String getRandQuote(Chat chat) {
        Quote quote = getRandomQuote();
        if (quote != null) {
            saveChatRepository(chat, quote.getQuoteId());
            return quote.getText();
        }
        return null;
    }

    public Quote getQuote(int id) {
        var existingQuote = quoteRepository.findByQuoteIdEquals(id);
        if (existingQuote.isPresent())
            return existingQuote.get();
        var quoteEntry = parser.getQuoteByIdFromInternet(id);
        if (quoteEntry != null) {
            return saveQuoteRepository(quoteEntry.getKey(), quoteEntry.getValue());
        }
        return null;
    }

    public Quote getRandomQuote() {
        var quoteEntry = parser.getRandomFromInternet();
        if (quoteEntry != null) {
            var existingQuote = quoteRepository.findByQuoteIdEquals(quoteEntry.getKey());
            return existingQuote.orElseGet(() -> saveQuoteRepository(quoteEntry.getKey(), quoteEntry.getValue()));
        }
        return null;
    }

    public List<Quote> getPage(int pageNumber) {
        List<Quote> quoteList = new ArrayList<>();
        Map<Integer, String> map = parser.getPageFromInternet(pageNumber);
        map.forEach((key, value) -> {
            var existed = quoteRepository.findByQuoteIdEquals(key);
            if (existed.isEmpty()) {
                quoteList.add(saveQuoteRepository(key, value));
            } else {
                quoteList.add(existed.get());
            }
        });
        return quoteList;
    }

    private Quote saveQuoteRepository(int quoteId, String textQuote) {
        var quote = new Quote();
        quote.setQuoteId(quoteId);
        quote.setText(textQuote);

        return quoteRepository.save(quote);
    }

    private void saveChatRepository(Chat chat, int quoteId) {
        chat.setLastId(quoteId);
        chatRepository.save(chat);
    }
}
