package faang.school.notificationservice.listner;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AbstractEventListener {

    private final Map<Class<?>, MessageBuilder> messageBuildersMap;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServicesMap;

    public AbstractEventListener(
            List<NotificationService> notificationServices,
            List<MessageBuilder> messageBuilders) {

        this.messageBuildersMap = new HashMap<>();
        for (MessageBuilder builder : messageBuilders) {
            this.messageBuildersMap.put(builder.getInstance(), builder);
        }

        this.notificationServicesMap = notificationServices.stream()
                .collect(Collectors.toMap(
                        NotificationService::getPreferredContact,
                        Function.identity()
                ));
    }

    public String getMessage(Class<?> eventType, Locale locale, Map<String, Object> parameters) {
        MessageBuilder builder = messageBuildersMap.get(eventType);
        if (builder == null) {
            throw new IllegalArgumentException("No MessageBuilder found for event type: " + eventType);
        }
        log.info("Продолжаем работать с билдером {}", builder);
        return builder.buildMessage(parameters, locale);
    }

    public void sendNotification(UserDto userDto, String message) {

        NotificationService service = notificationServicesMap.get(userDto.getPreference());
        service.send(userDto, message);
    }

}
