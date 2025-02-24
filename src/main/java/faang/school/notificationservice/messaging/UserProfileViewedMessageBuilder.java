package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.kafka.UserProfileViewedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserProfileViewedMessageBuilder implements MessageBuilder<UserProfileViewedDto> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserProfileViewedDto inputDto, UserServiceDto profileOwner, List<String> additionalWordsForOwnerMessage) {
        String ownername = additionalWordsForOwnerMessage.get(0);
        String formattedDateTime = additionalWordsForOwnerMessage.get(1);

        return messageSource.getMessage(
                "notification.user.profile.viewed",
                new Object[]{ownername, formattedDateTime},
                profileOwner.getLocale()
        );
    }

    @Override
    public Class<?> getInstance() {
        return UserProfileViewedDto.class;
    }
}
