package faang.school.notificationservice.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AbstractEventListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final Map<Class<?>, MessageBuilder> messageBuildersMap;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServicesMap;

    public AbstractEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationServices,
            List<MessageBuilder> messageBuilders) {

        this.objectMapper = objectMapper;
        this.userServiceClient = userServiceClient;
        this.messageBuildersMap = messageBuilders.stream()
                .collect(Collectors.toMap(
                        builder -> {

                            try {
                                Method getInstance = builder.getClass().getMethod("getInstance");
                                return (Class<?>) getInstance.invoke(builder);
                            } catch (Exception e) {
                                throw new RuntimeException("Cannot get instance type from MessageBuilder", e);
                            }
                        },
                        Function.identity(),
                        (existing, replacement) -> {
                            return existing;
                        }
                ));

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
        return builder.buildMessage(parameters, locale);
    }

    public void sendNotification(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        NotificationService service = notificationServicesMap.get(user.getPreference());
        service.send(user, message);
    }

}
