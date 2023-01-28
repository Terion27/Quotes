package quotes.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quotes.services.QuoteService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotCommands {
    private final QuoteService quoteService;

    @AppBotCommands(name = "/start", description = "Старт")
    String start(Long chatId) { return "Старт!"; }

    @AppBotCommands(name = "/next", description = "Следующая цитата")
    String next(Long chatId) { return quoteService.getNextQuote(chatId); }

    @AppBotCommands(name = "/prev", description = "Предыдущая цитата")
    String prev(Long chatId) { return quoteService.getPrevQuote(chatId); }

    @AppBotCommands(name = "/rand", description = "Случайная цитата")
    String rand(Long chatId) { return quoteService.getRandQuote(chatId); }



    public String runBotCommands(String messageText, Long chatId) {
        String responseText = "";
        for (Method method : getAppBotMethods()) {
            if (method.getAnnotation(AppBotCommands.class).description().equals(messageText)) {
                method.setAccessible(true);
                try {
                    responseText = (String) method.invoke(this, chatId);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("Failed to invoke method: " + e.getMessage());
                }
            }
        }
        return responseText;
    }

    public ArrayList<String> getListBotCommands() {
        ArrayList<String> commands = new ArrayList<>();
        for (Method method : getAppBotMethods()) {
            commands.add(method.getAnnotation(AppBotCommands.class).description());
        }
        return commands;
    }

    private ArrayList<Method> getAppBotMethods() {
        ArrayList<Method> methods = new ArrayList<>();
        Method[] classMethods = BotCommands.class.getDeclaredMethods();
        for (Method method : classMethods) {
            if (!method.isAnnotationPresent(AppBotCommands.class)) continue;
            methods.add(method);
        }
        return methods;
    }
}
