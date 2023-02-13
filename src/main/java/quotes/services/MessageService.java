package quotes.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quotes.commands.AppBotCommands;
import quotes.commands.BotCommands;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final BotCommands botCommands;

    public String runBotCommands(String messageText, Long chatId) {
        String responseText = "";
        var method = Arrays.stream(botCommands.getClass().getDeclaredMethods())
                .filter(m -> m.getAnnotation(AppBotCommands.class).description().equals(messageText))
                .findFirst();
        if (method.isEmpty()) return "Неизвестная комманда";
        try {
            method.get().setAccessible(true);
            responseText = (String) method.get().invoke(botCommands, chatId);
        } catch (IllegalAccessException | InvocationTargetException e)  {
            log.error("Failed to invoke method: " + e.getMessage());
        }
        return responseText;
    }
}
