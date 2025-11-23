package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipEventDto> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return MentorshipEventDto.class;
    }

    @Override
    public String buildMessage(MentorshipEventDto event, Locale locale) {

        UserDto mentee = userServiceClient.getUser(event.menteeId());

        return messageSource.getMessage(
                "mentorship.offered",
                new Object[]{mentee.getUsername()},
                locale
        );
    }
}
