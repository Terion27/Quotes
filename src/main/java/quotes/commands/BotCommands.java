package quotes.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quotes.commons.QuoteCommon;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotCommands {

    private final QuoteCommon quoteCommon;

    @AppBotCommands(name = "/start", description = "Старт")
    String start(Long chatId) { return "Старт!"; }

    @AppBotCommands(name = "/next", description = "Следующая цитата")
    String next(Long chatId) { return quoteCommon.getNextQuote(chatId); }

    @AppBotCommands(name = "/prev", description = "Предыдущая цитата")
    String prev(Long chatId) { return quoteCommon.getPrevQuote(chatId); }

    @AppBotCommands(name = "/rand", description = "Случайная цитата")
    String rand(Long chatId) { return quoteCommon.getRandQuote(chatId); }






}
