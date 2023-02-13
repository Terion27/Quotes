package quotes.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import quotes.models.Chat;
import quotes.models.Quote;
import quotes.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QuoteCommon {

    private final ChatRepository chatRepository;
    private final QuoteRepository quoteRepository;
    private final ParserCommon parserCommon;


    public String getNextQuote(Long chatId) {
        Chat chat = chatSession(chatId);
        int lastId = chat.getLastId();

        Quote newQuote = quoteRepository.findFirstByQuoteIdGreaterThanOrderByQuoteIdAsc(lastId).stream()
                .findFirst()
                .orElseGet(() -> lastQuote(lastId));
        saveLastId(chat, newQuote.getQuoteId());

        return newQuote.getText();
    }

    public String getPrevQuote(Long chatId) {
        Chat chat = chatSession(chatId);
        int lastId = chat.getLastId();

        Quote newQuote = quoteRepository.findFirstByQuoteIdLessThanOrderByQuoteIdDesc(lastId).stream()
                .findFirst()
                .orElseGet(() -> FirstQuote(lastId));
        saveLastId(chat, newQuote.getQuoteId());

        return newQuote.getText();
    }

    public String getRandQuote(Long chatId) {
        Chat chat = chatSession(chatId);
        Quote quote = getRandomQuote();
        if (quote != null) {
            saveLastId(chat, quote.getQuoteId());
            return quote.getText();
        }
        return "Не удалосьполучить цитату";
    }

    public Quote getRandomQuote() {
        Quote randomQuote;
        var quoteEntry = parserCommon.getRandomFromInternet();
        if (quoteEntry != null) {
            randomQuote = quoteRepository.findByQuoteIdEquals(quoteEntry.getKey()).stream()
                    .findFirst()
                    .orElseGet(() -> saveQuoteRepository(quoteEntry.getKey(), quoteEntry.getValue()));
        } else return new Quote();

        return randomQuote;
    }


    private Quote saveQuoteRepository(int quoteId, String textQuote) {
        var quote = new Quote();
        quote.setQuoteId(quoteId);
        quote.setText(textQuote);

        return quoteRepository.save(quote);
    }

    private Quote lastQuote(int lastId){
        Quote quote = new Quote();
        quote.setText("Достигнут конец списка");
        quote.setQuoteId(lastId);
        return quote;
    }

    private Quote FirstQuote(int lastId){
        Quote quote = new Quote();
        quote.setText("Достигнуто начало списка");
        quote.setQuoteId(lastId);
        return quote;
    }

    private Chat regNewChat(Long chatId) {
        var chat = new Chat();
        chat.setChatId(chatId);
        chat.setLastId(0);

        return chatRepository.save(chat);
    }

    private void saveLastId(Chat chat, int newLastId) {
        chat.setLastId(newLastId);
        chatRepository.save(chat);
    }

    private Chat chatSession(Long chatId) {
        return chatRepository.findByChatIdEquals(chatId)
                .orElseGet(() -> regNewChat(chatId));
    }

    public List<Quote> getPage(int pageNumber) {
        List<Quote> quoteList = new ArrayList<>();
        Map<Integer, String> map = parserCommon.getPageFromInternet(pageNumber);
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

    public Quote getQuote(int id) {
        var existingQuote = quoteRepository.findByQuoteIdEquals(id);
        if (existingQuote.isPresent())
            return existingQuote.get();
        var quoteEntry = parserCommon.getQuoteByIdFromInternet(id);
        if (quoteEntry != null) {
            return saveQuoteRepository(quoteEntry.getKey(), quoteEntry.getValue());
        }
        return null;
    }

}
