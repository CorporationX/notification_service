package faang.school.notificationservice.listener.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class EventStartListener extends AbstractEventListener<EventStartDto> {
    private final ScheduledThreadPoolExecutor scheduledExecutor;

    public EventStartListener(ObjectMapper objectMapper,
                              UserServiceClient userServiceClient,
                              List<MessageBuilder<EventStartDto>> messageBuilder,
                              List<NotificationService> notificationServices,
                              ScheduledThreadPoolExecutor scheduledExecutor) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
        this.scheduledExecutor = scheduledExecutor;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventStartDto eventStartDto = readValue(message.getBody(), EventStartDto.class);
        log.info("Sending notifications for event: {}", eventStartDto);
        scheduleNotifications(eventStartDto);
    }

    private void scheduleNotifications(EventStartDto event) {
        List<UserDto> attendees = userServiceClient.getUsersByIds(event.getAttendeeIds());

        scheduleForGivenTime(TimeUnit.DAYS, 1, event, attendees);
        scheduleForGivenTime(TimeUnit.HOURS, 5, event, attendees);
        scheduleForGivenTime(TimeUnit.HOURS, 1, event, attendees);
        scheduleForGivenTime(TimeUnit.MINUTES, 10, event, attendees);
        scheduleForGivenTime(TimeUnit.MINUTES, 1, event, attendees);
    }

    private void scheduleForGivenTime(TimeUnit timeUnit, long amount, EventStartDto event, List<UserDto> attendees) {
        long eventStart = getDateInMillis(event.getStartDate());
        long now = getDateInMillis(LocalDateTime.now());
        long delay = Math.max(0, eventStart - now - timeUnit.toMillis(amount));

        event.setTimeTillStart(eventStart - now);
        scheduledExecutor.schedule(() -> notify(attendees, event, delay), delay, TimeUnit.MILLISECONDS);
    }

    private void notify(List<UserDto> users, EventStartDto event, long delay) {
        if (delay > 0) {
            users.forEach(user -> {
                event.setNotifiedAttendee(user);
                String message = getMessage(event, Locale.getDefault());
                sendNotification(user, message);
            });
        }
    }

    private long getDateInMillis(LocalDateTime date) {
        return date.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @PreDestroy
    public void clean() {
        scheduledExecutor.shutdown();
    }
}
