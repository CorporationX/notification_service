package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventAttendeeDto;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.messaging.EventStartMessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventStartService {

    private final UserServiceClient userServiceClient;
    private final EventStartMessageBuilder eventStartMessageBuilder;
    private final List<NotificationService> notificationServices;

    public void sendNotifications(EventStartDto eventStartDto) {
        log.info("Sending notifications for event " + eventStartDto.getId());

        EventDto event = userServiceClient.getEvent(eventStartDto.getId());
        List<UserDto> attendees = userServiceClient.getUsersByIds(eventStartDto.getAttendeeIds());

        attendees.forEach(attendee -> {
            EventAttendeeDto eventAttendeeDto = getEventAttendeeDto(attendee, event);
            String message = eventStartMessageBuilder.buildMessage(eventAttendeeDto, Locale.getDefault());

            notificationServices.stream()
                    .filter(service -> service.getPreferredContact().equals(attendee.getPreference()))
                    .findFirst()
                    .ifPresent(service -> service.sendNotification(attendee, message));
        });
    }

    private EventAttendeeDto getEventAttendeeDto(UserDto user, EventDto event) {
        return EventAttendeeDto.builder()
                .username(user.getUsername())
                .eventTitle(event.getTitle())
                .build();
    }
}
