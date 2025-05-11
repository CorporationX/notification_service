package faang.school.notificationservice.listener.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;
import java.util.Locale;

@Slf4j
public abstract class AbstractSubscriptionListener implements MessageListener {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;

    protected AbstractSubscriptionListener(ObjectMapper objectMapper,
                                           UserServiceClient userServiceClient,
                                           List<NotificationService> notificationServices) {
        this.objectMapper = objectMapper;
        this.userServiceClient = userServiceClient;
        this.notificationServices = notificationServices;
    }

    protected abstract String buildMessage(SubscriptionEventDto eventDto, Locale locale);

    protected abstract long getUserId(SubscriptionEventDto eventDto);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        if (messageBody.isEmpty()) {
            log.warn("Received empty message. Skipping processing");
            return;
        }

        try {
            SubscriptionEventDto eventDto = objectMapper.readValue(messageBody, SubscriptionEventDto.class);
            long userId = getUserId(eventDto);
            UserDto user = userServiceClient.getUser(userId);
            if (user == null) {
                log.warn("User with id {} not found. Skipping notification", userId);
                return;
            }
            if (user.getPreference() == null) {
                log.warn("User {} has no preferred contact method set. Using EMAIL as default", userId);
                user.setPreference(UserDto.PreferredContact.EMAIL);
            }
            String text = buildMessage(eventDto, Locale.getDefault());

            notificationServices.stream()
                    .filter(s -> s.getPreferredContact().equals(user.getPreference()))
                    .findFirst()
                    .ifPresentOrElse(
                            service -> service.send(user, text),
                            () -> log.warn("No matching notification service for preferred contact: {}", user.getPreference())
                    );

        } catch (JsonProcessingException e) {
            log.error("Failed to parse message body into SubscriptionEventDto: {}", messageBody, e);
        } catch (NullPointerException e) {
            log.error("Null pointer encountered while processing message: {}", messageBody, e);
        } catch (RuntimeException e) {
            log.error("Unexpected error occurred while processing subscription event", e);
        }
    }
}
