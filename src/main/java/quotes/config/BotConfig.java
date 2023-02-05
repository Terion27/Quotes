package quotes.config;

import lombok.Getter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Getter
@Slf4j
@Configuration
@RequiredArgsConstructor
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    private final String botName;

    @Value("${bot.token}")
    private final String token;
}
