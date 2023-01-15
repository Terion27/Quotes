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

package quotes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quotes.models.*;
import quotes.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class QuoteService {

    @Autowired
    BashParser parser;
    @Autowired
    QuoteRepository quoteRepository;
    @Autowired
    ChatRepository chatRepository;

    /**
     * getNextQuote() - getting the next quote by id.
     *
     * @param chat - getLastId() last viewed user id
     *      getQuote() - loading quote from database
     *
     * @return - text of requested quote
     */
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

    /**
     * getPrevQuote() - getting the previous quote by id.
     *
     * @param chat - getLastId() last viewed user id
     *      getQuote() - loading quote from database
     *
     * @return - text of requested quote
     */
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

    /**
     * getRandQuote() - getting a random quote.
     *
     * @param chat
     *          - getLastId() last viewed user id
     *      getRandomQuote() - loading quote from database
     *
     * @return - text of requested quote
     */
    public String getRandQuote(Chat chat) {
        Quote quote = getRandomQuote();
        if (quote == null) return null;
        saveChatRepository(chat, quote.getQuoteId());
        return quote.getText();
    }

    /**
     * getQuote() - loading quote by id from database
     *
     * @param id - quote id
     *      parser.getQuoteByIdFromInternet() - get quote by id from internet
     *
     * @return - requested quote
     */
    public Quote getQuote(int id) {
        var existingQuote = quoteRepository.findByQuoteIdEquals(id);
        if (existingQuote.isPresent())
            return existingQuote.get();
        var quoteEntry = parser.getQuoteByIdFromInternet(id);
        if (quoteEntry == null) return null;

        return saveQuoteRepository(quoteEntry.getKey(), quoteEntry.getValue());

    }

    /**
     * getRandomQuote() - loading random quote from database
     *      parser.getRandomFromInternet() - getting a random quote from internet
     *
     * @return - requested quote
     */
    public Quote getRandomQuote() {
        var quoteEntry = parser.getRandomFromInternet();
        if (quoteEntry == null) return null;
        var existingQuote = quoteRepository.findByQuoteIdEquals(quoteEntry.getKey());

        return existingQuote.orElseGet(() -> saveQuoteRepository(quoteEntry.getKey(), quoteEntry.getValue()));
    }

    /**
     * getPage() - getting a quotes page by number
     *
     * @param pageNumber - requested page number
     *      parser.getPageFromInternet() - loading a page by number from the internet
     *
     * @return - requested quotes page
     */
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

    /**
     * saveQuoteRepository() - writing a quote to the repository
     *
     * @param quoteId - quote id
     * @param textQuote - quote text
     *
     * @return - requested quote
     */
    private Quote saveQuoteRepository(int quoteId, String textQuote) {
        var quote = new Quote();
        quote.setQuoteId(quoteId);
        quote.setText(textQuote);

        return quoteRepository.save(quote);
    }

    /**
     * saveChatRepository()- writing a chat to the repository
     *
     * @param chat
     *          - setLastId() record last viewed user id
     */
    private void saveChatRepository(Chat chat, int quoteId) {
        chat.setLastId(quoteId);
        chatRepository.save(chat);
    }
}
