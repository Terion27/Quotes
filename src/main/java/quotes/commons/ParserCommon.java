package quotes.commons;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class ParserCommon {

    public Map.Entry<Integer, String> getRandomFromInternet(){
        try {
            Document doc = Jsoup.connect("http://ibash.org.ru/random.php").get();
            return getParsingPage(doc);
        } catch (IOException e) {
            log.error("Unable to load random quotes from internet: " + e.getMessage());
            throw new RuntimeException("Unable to load random quotes from internet: " + e);
        }
    }

    private Map.Entry<Integer, String> getParsingPage(Document doc) {
        Element quoteElement = doc.select(".quote").first();
        assert quoteElement != null;
        String realId = Objects.requireNonNull(quoteElement.select("b").first()).text();
        if (!realId.equals("#???") && !realId.equals("")) {
            String text = Objects.requireNonNull(quoteElement.select(".quotbody").first()).text();
            return new AbstractMap.SimpleEntry<>(Integer.parseInt(realId.substring(1)), text);        }
        return null;
    }

    public Map<Integer, String> getPageFromInternet(int pageNumber){
        Map<Integer, String> quotes = new HashMap<>();
        try {
            Document doc = Jsoup.connect("http://ibash.org.ru/?page=" + pageNumber).get();
            Elements sourceQuotes = doc.select(".quote");
            for (Element quoteElement : sourceQuotes) {
                int id = Integer.parseInt(Objects.requireNonNull(quoteElement.select("b").first()).text().substring(1));
                String text = Objects.requireNonNull(quoteElement.select(".quotbody").first()).text();
                if (!text.isEmpty()) quotes.put(id, text);
            }
        } catch (IOException e){
            log.error("Unable to load page with quotes from internet: " + e.getMessage());
        }
        return quotes;
    }

    public Map.Entry<Integer, String> getQuoteByIdFromInternet(int id) {
        try {
            Document doc = Jsoup.connect("http://ibash.org.ru/quote.php?id=" + id).get();
            return getParsingPage(doc);
        } catch (IOException e) {
            log.error("Unable to load quote from internet: " + e.getMessage());
        }
        return null;
    }
}
