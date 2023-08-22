package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventAttendeeDto;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.messaging.EventStartMessageBuilder;
import faang.school.notificationservice.service.notification.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EventStartService {

    private final UserServiceClient userServiceClient;
    private final EventStartMessageBuilder eventStartMessageBuilder;
    private final EmailService emailService;

    public void sendNotifications(EventStartDto eventStartDto) {
        EventDto event = userServiceClient.getEvent(eventStartDto.getId());
        List<UserDto> users = userServiceClient.getUsersByIds(eventStartDto.getAttendeeIds());

        users.forEach(user -> user.setPreference(UserDto.PreferredContact.EMAIL)); // temporary

        users.forEach(user -> {
            EventAttendeeDto eventAttendeeDto = EventAttendeeDto.builder()
                    .username(user.getUsername())
                    .eventTitle(event.getTitle())
                    .build();

            emailService.sendNotification(
                    user,
                    eventStartMessageBuilder.buildMessage(eventAttendeeDto, Locale.getDefault())
            );
        });
    }
}
