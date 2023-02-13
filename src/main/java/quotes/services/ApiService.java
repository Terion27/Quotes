package quotes.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import quotes.commons.QuoteCommon;
import quotes.models.Quote;
import quotes.repositories.QuoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final QuoteRepository quoteRepository;
    private final QuoteCommon quoteCommon;

    public ResponseEntity<List<Quote>> apiGetAll(String page) {
        int _page = 1;
        try {
            _page = Integer.parseInt(page);
        }catch (Exception e) {
            log.error("Api, method getAll: " + e.getMessage());
        }
        var res = quoteRepository.findAll(PageRequest.of(_page - 1, 5));

        return new ResponseEntity<>(res.stream().collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<List<Quote>> apiGetPage(String page) {
        int _page = 1;
        try {
            _page = Integer.parseInt(page);
        } catch (Exception e) {
            log.error("Api, method getPage: " + e.getMessage());
        }
        var res = quoteCommon.getPage(_page);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<Quote> apiGetById(int id) {
        var res = quoteCommon.getQuote(id);
        if (res == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<Quote> apiGetRandom() {
        var res = quoteCommon.getRandomQuote();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
