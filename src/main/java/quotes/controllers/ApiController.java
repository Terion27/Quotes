package quotes.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import quotes.models.Quote;
import quotes.services.ApiService;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    @GetMapping("/all")
    public ResponseEntity<List<Quote>> getAll(@RequestParam(required = false, defaultValue = "1") String page) {

        return apiService.apiGetAll(page);
    }

    @GetMapping("/page")
    public ResponseEntity<List<Quote>> getPage(@RequestParam(required = false, defaultValue = "1") String page) {

        return apiService.apiGetPage(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quote> getById(@PathVariable("id") int id) {

        return apiService.apiGetById(id);
    }

    @GetMapping("/random")
    public ResponseEntity<Quote> getRandom() {

        return apiService.apiGetRandom();
    }
}
