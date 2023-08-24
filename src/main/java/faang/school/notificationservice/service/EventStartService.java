package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.messaging.EventStartMessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EventStartService {

    private final UserServiceClient userServiceClient;
    private final EventStartMessageBuilder messageBuilder;
    private final List<NotificationService> notificationServices;
    private final ScheduledThreadPoolExecutor scheduledExecutor;

    @PreDestroy
    public void clean() {
        scheduledExecutor.shutdown();
    }

    public void scheduleNotifications(EventStartDto eventStartDto) {
        EventDto event = userServiceClient.getEvent(eventStartDto.getId());
        List<UserDto> attendees = userServiceClient.getUsersByIds(eventStartDto.getAttendeeIds());

        long eventStart = event.getStartDate().toInstant(ZoneOffset.UTC).toEpochMilli();
        long now = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        long beforeStart = eventStart - now;

        long oneDay = Math.max(0, eventStart - now - TimeUnit.DAYS.toMillis(1));
        long fiveHours = Math.max(0, eventStart - now - TimeUnit.HOURS.toMillis(5));
        long oneHour = Math.max(0, eventStart - now - TimeUnit.HOURS.toMillis(1));
        long tenMinutes = Math.max(0, eventStart - now - TimeUnit.MINUTES.toMillis(10));
        long oneMinute = Math.max(0, eventStart - now - TimeUnit.MINUTES.toMillis(1));

        scheduledExecutor.schedule(() -> notify(attendees, event, oneDay, beforeStart),
                oneDay, TimeUnit.MILLISECONDS);
        scheduledExecutor.schedule(() -> notify(attendees, event, fiveHours, beforeStart),
                fiveHours, TimeUnit.MILLISECONDS);
        scheduledExecutor.schedule(() -> notify(attendees, event, oneHour, beforeStart),
                oneHour, TimeUnit.MILLISECONDS);
        scheduledExecutor.schedule(() -> notify(attendees, event, tenMinutes, beforeStart),
                tenMinutes, TimeUnit.MILLISECONDS);
        scheduledExecutor.schedule(() -> notify(attendees, event, oneMinute, beforeStart),
                beforeStart, TimeUnit.MILLISECONDS);
    }

    private void notify(List<UserDto> users, EventDto event, long delay, long timeToWait) {
        if (delay > 0) {
            users.forEach(user -> {
                event.setAttendee(user);
                event.setTimeTillStart(timeToWait);
                String message = messageBuilder.buildMessage(event, Locale.getDefault());
                sendNotification(user, message);
            });
        }
    }

    private void sendNotification(UserDto user, String text) {
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .ifPresent(service -> service.send(user, text));
    }
}
