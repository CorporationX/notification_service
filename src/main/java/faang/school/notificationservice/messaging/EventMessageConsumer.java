package faang.school.notificationservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventMessageConsumer implements MessageBuilder {
    @Override
    public Class<?> getInstance() {
        return null;
    }

    @Override
    public String buildMessage(Object event, Locale locale) {
        return "";
    }
}
