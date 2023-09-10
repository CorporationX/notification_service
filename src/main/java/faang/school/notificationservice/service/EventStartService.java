package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.EventStartMessageBuilder;
import faang.school.notificationservice.sender.NotificationService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EventStartService {
    private final EventStartMessageBuilder eventStartMessageBuilder;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    @PreDestroy
    public void clean() {
        executorService.shutdown();
    }

    @Async
    public void scheduleNotifications(EventStartDto eventStartDto) {
        EventDto event = userServiceClient.getEvent(eventStartDto.getEventId());
        List<UserDto> users = userServiceClient.getUsersByIds(eventStartDto.getUserIds());
        long eventStart = event.getStartDate().toInstant(ZoneOffset.UTC).toEpochMilli();
        long now = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        long dayBefore = Math.max(0, eventStart - now - TimeUnit.DAYS.toMillis(1));
        long fiveHoursBefore = Math.max(0, eventStart - now - TimeUnit.HOURS.toMillis(5));
        long hourBefore = Math.max(0, eventStart - now - TimeUnit.HOURS.toMillis(1));
        long tenMinutesBefore = Math.max(0, eventStart - now - TimeUnit.MINUTES.toMillis(10));
        long beforeStart = Math.max(0, eventStart - now);

        executorService.schedule(() -> notify(users, event, dayBefore, beforeStart), dayBefore, TimeUnit.MILLISECONDS);
        executorService.schedule(() -> notify(users, event, fiveHoursBefore, beforeStart), fiveHoursBefore, TimeUnit.MILLISECONDS);
        executorService.schedule(() -> notify(users, event, hourBefore, beforeStart), hourBefore, TimeUnit.MILLISECONDS);
        executorService.schedule(() -> notify(users, event, tenMinutesBefore, beforeStart), tenMinutesBefore, TimeUnit.MILLISECONDS);
        executorService.schedule(() -> notify(users, event, beforeStart, beforeStart), beforeStart, TimeUnit.MILLISECONDS);
    }

    private void notify(List<UserDto> users, EventDto event, long delay, long timeTillStart) {
        if (delay > 0) {
            for (UserDto user : users) {
                event.setUserDto(user);
                String message = String.format(eventStartMessageBuilder.buildMessage(event, Locale.UK, String.valueOf(timeTillStart)));
				notificationServices.stream()
                        .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                        .forEach(service -> service.send(user, message));
            }
        }
    }
}
