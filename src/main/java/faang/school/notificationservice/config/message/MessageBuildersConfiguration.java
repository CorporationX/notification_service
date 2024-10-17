package faang.school.notificationservice.config.message;

import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class MessageBuildersConfiguration {

    private final List<MessageBuilder<?>> messageBuilders;

    @Bean
    public Map<Class<?>, MessageBuilder<?>> messageBuilders() {
        return messageBuilders.stream()
                .collect(Collectors.toMap(MessageBuilder::getInstance, Function.identity(),
                        (existing, replacement) -> {
                            throw new RuntimeException("Collision in message builders map, " +
                                    "exists: " + existing +
                                    " replacement " + replacement);
                        }));
    }
}
