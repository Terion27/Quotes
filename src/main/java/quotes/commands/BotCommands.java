package quotes.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quotes.models.Chat;
import quotes.services.QuoteService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Slf4j
@Component
public class BotCommands {

    @Autowired
    QuoteService quoteService;

    Chat chat;

    @AppBotCommands(name = "/start", description = "Старт", showInMenu = true, showInKeyboard = true)
    String start() { return "1"; }

    @AppBotCommands(name = "/next", description = "Следующая цитата", showInMenu = true, showInKeyboard = true)
    String next() { return quoteService.getNextQuote(chat); }

    @AppBotCommands(name = "/prev", description = "Предыдущая цитата", showInMenu = true, showInKeyboard = true)
    String prev() { return quoteService.getPrevQuote(chat); }

    @AppBotCommands(name = "/rand", description = "Случайная цитата", showInMenu = true, showInKeyboard = true)
    String rand() { return quoteService.getRandQuote(chat); }



    public String runBotCommands(String messageText, Chat chat) {
        this.chat = chat;
        String responseText = "";
        for (Method method : getAppBotMethods()) {
            if (method.getAnnotation(AppBotCommands.class).description().equals(messageText)) {
                method.setAccessible(true);
                try {
                    responseText = (String) method.invoke(this);
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
