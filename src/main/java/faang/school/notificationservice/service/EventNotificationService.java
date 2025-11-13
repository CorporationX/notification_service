package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.EventMessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventNotificationService {

    private final EventMessageConsumer eventOwnerMessageConsumer;

    public void send(UserDto userDto, String message) {
        log.info("{} - {}", userDto.getId(), message);
    }


    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    public void processEventStart(EventStartEventDto eventStartEventDto) {
        String text = eventOwnerMessageConsumer.buildMessage(eventStartEventDto, Locale.getDefault());

        log.info("Information about the event has arrived! event id - {}, owner id-{} and name-{}, name event -{}",
                eventStartEventDto.eventId(), eventStartEventDto.userId(),
                eventStartEventDto.nameOwner(), eventStartEventDto.titleEvent());

        List<UserDto> attendeesIds = eventStartEventDto.attendeesUser();

        if (attendeesIds.isEmpty()) {
            log.info("There are no subscribers to the event {}.", eventStartEventDto.eventId());
        } else {
            attendeesIds.forEach(user -> send(user
                    , text));
        }
        log.info("Received EventStartEvent: {} ", eventStartEventDto.nameOwner());
    }
}
