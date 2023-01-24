package quotes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quotes.models.*;
import quotes.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class QuoteService {

    @Autowired
    private final BashParser parser;
    @Autowired
    private final QuoteRepository quoteRepository;
    @Autowired
    private final ChatRepository chatRepository;

    public QuoteService(BashParser parser, QuoteRepository quoteRepository, ChatRepository chatRepository) {
        this.parser = parser;
        this.quoteRepository = quoteRepository;
        this.chatRepository = chatRepository;
    }

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
        if (quote == null) return null;
        saveChatRepository(chat, quote.getQuoteId());
        return quote.getText();
    }

    public Quote getQuote(int id) {
        var existingQuote = quoteRepository.findByQuoteIdEquals(id);
        if (existingQuote.isPresent())
            return existingQuote.get();
        var quoteEntry = parser.getQuoteByIdFromInternet(id);
        if (quoteEntry == null) return null;

        return saveQuoteRepository(quoteEntry.getKey(), quoteEntry.getValue());

    }

    public Quote getRandomQuote() {
        var quoteEntry = parser.getRandomFromInternet();
        if (quoteEntry == null) return null;
        var existingQuote = quoteRepository.findByQuoteIdEquals(quoteEntry.getKey());

        return existingQuote.orElseGet(() -> saveQuoteRepository(quoteEntry.getKey(), quoteEntry.getValue()));
    }

    public List<Quote> getPage(int pageNumber) {
        List<Quote> quoteList = new ArrayList<>();
        Map<Integer, String> map = parser.getPageFromInternet(pageNumber);
        for (var entry : map.entrySet()) {
            var existed = quoteRepository.findByQuoteIdEquals(entry.getKey());
            if (existed.isEmpty()) {
                quoteList.add(saveQuoteRepository(entry.getKey(), entry.getValue()));
            } else {
                quoteList.add(existed.get());
            }
        }
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
