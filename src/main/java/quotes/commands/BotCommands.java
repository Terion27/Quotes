package quotes.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quotes.commons.ParserCommon;
import quotes.models.Quote;
import quotes.repositories.QuoteRepository;

import static quotes.config.MessageStrings.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotCommands {

    private final QuoteRepository quoteRepository;
    private final ParserCommon parserCommon;

    @AppBotCommands(name = "/start", description = "Старт")
    Quote start(int ignoredLastId) { return new Quote("Старт", 0); }

    @AppBotCommands(name = "/next", description = NEXT_QUOTE)
    Quote next(int lastId) { return getNextQuote(lastId); }

    @AppBotCommands(name = "/prev", description = PREVIOUS_QUOTE)
    Quote prev(int lastId) { return getPrevQuote(lastId); }

    @AppBotCommands(name = "/rand", description = RANDOM_QUOTE)
    Quote rand(int ignoredLastId) { return getRandomQuote(); }


    private Quote getNextQuote(int lastId){
        return quoteRepository.findFirstByQuoteIdGreaterThanOrderByQuoteIdAsc(lastId).stream()
                .findFirst()
                .orElseGet(() -> new Quote(END_OF_LIST_REACHED, lastId));
    }

    public Quote getPrevQuote(int lastId) {
        return quoteRepository.findFirstByQuoteIdLessThanOrderByQuoteIdDesc(lastId).stream()
                .findFirst()
                .orElseGet(() -> new Quote(TOP_OF_LIST_REACHED, lastId));
    }

    public Quote getRandomQuote() {
        var quoteEntry = parserCommon.getRandomFromInternet();
        if (quoteEntry == null) return new Quote(FAILED_TO_GET_QUOTE, -1);

        return quoteRepository.findByQuoteIdEquals(quoteEntry.getKey()).stream()
                    .findFirst()
                    .orElseGet(() -> saveQuoteRepository(quoteEntry.getValue(), quoteEntry.getKey()));
    }

    private Quote saveQuoteRepository(String textQuote, int quoteId) {
        return quoteRepository.save(new Quote(textQuote, quoteId));
    }
}
