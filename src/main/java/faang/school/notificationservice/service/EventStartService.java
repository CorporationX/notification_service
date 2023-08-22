package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EventStartService {
    private final AbstractEventListener<EventDto, String> abstractEventListener;
    private final UserServiceClient userServiceClient;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    @PreDestroy
    public void clean() {
        executorService.shutdown();
    }

    @Async
    public void scheduleNotifications(EventStartDto eventStartDto) {
//        EventDto event = new EventDto();
//        event.setDescription("SomeDescription");
//        event.setId(0L);
//        event.setTitle("SomeTitle");
//        event.setStartDate(LocalDateTime.now().plusMinutes(11));
//
//        UserDto user = new UserDto();
//        user.setId(1L);
//        user.setEmail("some@email.com");
//        user.setPreference(UserDto.PreferredContact.SMS);
//
//        List<UserDto> users = new ArrayList<>();
//        users.add(user);
//        users.add(user);
//        users.add(user);
//        users.add(user);
//        users.add(user);
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
                String message = abstractEventListener.getMessage(EventDto.class, String.valueOf(timeTillStart), event);
                abstractEventListener.sendNotification(user.getId(), message);
            }
        }
    }
}
