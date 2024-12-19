package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EventService {

    private final MentorshipOfferedMessageBuilder builder;
    private final EmailService emailService;

    public void mentorshipOffered(long idRequest, long idAuthor, long idRequester) {
        String message = builder.buildMessage(new MentorshipOfferedEvent(idRequest, idAuthor, idRequester), Locale.ENGLISH);
        UserDto userDto = new UserDto();
        userDto.setId(idRequester);
        // emailService.send(userDto, message);
    }
}