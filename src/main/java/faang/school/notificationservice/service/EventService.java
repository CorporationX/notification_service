package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.service.email.EmailService;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EventService {

    private final MentorshipOfferedMessageBuilder builder;
    private final EmailService emailService;
    private final TelegramService telegramService;

    public void sendMentorshipOfferedMessage(long idRequest, long idAuthor, long idReceiver) {
        String message = builder.buildMessage(new MentorshipOfferedEvent(idRequest, idAuthor, idReceiver), Locale.ENGLISH);
        UserDto userDto = getUserDto(idRequest);

        switch (userDto.getPreference()) {
            case EMAIL:
                emailService.send(userDto, message);
                break;
            case TELEGRAM:
                telegramService.send(userDto, message);
                break;
            default:
                throw new IllegalArgumentException("Unsupported contact preference: " + userDto.getPreference());
        }
    }

    UserDto getUserDto(Long userId) {
        return new UserDto();
    }
}