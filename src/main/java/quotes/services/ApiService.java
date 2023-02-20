package quotes.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import quotes.commands.BotCommands;
import quotes.commons.ParserCommon;
import quotes.models.Quote;
import quotes.repositories.QuoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static quotes.config.MessageStrings.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {


    private final QuoteRepository quoteRepository;
    private final BotCommands botCommands;
    private final ParserCommon parserCommon;

    public ResponseEntity<List<Quote>> apiGetAll(String page) {
        int _page;
        try {
            _page = Integer.parseInt(page);
        }catch (Exception e) {
            log.error(API_METHOD_GETALL + e.getMessage());
            throw new RuntimeException(API_METHOD_GETALL + e);
        }
        var res = quoteRepository.findAll(PageRequest.of(_page - 1, 5));

        return new ResponseEntity<>(res.stream().collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<List<Quote>> apiGetPage(String page) {
        int _page;
        try {
            _page = Integer.parseInt(page);
        } catch (Exception e) {
            log.error(API_METHOD_GETPAGE + e.getMessage());
            throw new RuntimeException(API_METHOD_GETPAGE + e);
        }
        return new ResponseEntity<>(getPage(_page), HttpStatus.OK);
    }

    public ResponseEntity<Quote> apiGetById(int id) {
        return new ResponseEntity<>(getQuote(id), HttpStatus.OK);
    }

    public ResponseEntity<Quote> apiGetRandom() {
        return new ResponseEntity<>(botCommands.getRandomQuote(), HttpStatus.OK);
    }

    public Quote getQuote(int id) {
        return quoteRepository.findByQuoteIdEquals(id).stream()
                .findFirst()
                .orElseGet(() -> {
                    var quoteEntry = parserCommon.getQuoteByIdFromInternet(id);
                    if (quoteEntry != null) return quoteRepository.save(new Quote(quoteEntry.getValue(), quoteEntry.getKey()));
                    return new Quote(NO_QUOTE_WITH_THIS_ID, -1);
                });
    }

    public List<Quote> getPage(int pageNumber) {
        List<Quote> quoteList = new ArrayList<>();
        Map<Integer, String> map = parserCommon.getPageFromInternet(pageNumber);
        map.forEach((key, value) -> quoteList.add(quoteRepository.findByQuoteIdEquals(key).stream()
                .findFirst()
                .orElseGet(() -> quoteRepository.save(new Quote(value, key)))
        ));
        return quoteList;
    }
}
