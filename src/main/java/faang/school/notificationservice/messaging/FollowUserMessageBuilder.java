package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.kafka.FollowUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FollowUserMessageBuilder implements MessageBuilder<FollowUserDto> {
    private final MessageSource messageSource;


    @Override
    public String buildMessage(FollowUserDto inputDto, UserServiceDto profileOwner, List<String> additionalWordsForOwnerMessage) {
        String followee = additionalWordsForOwnerMessage.get(0);
        String formattedDateTime = additionalWordsForOwnerMessage.get(1);
        return messageSource.getMessage(
                "notification.user.follows.user",
                new Object[]{followee, formattedDateTime},
                profileOwner.getLocale()
        );
    }

    @Override
    public Class<?> getInstance() {
        return FollowUserDto.class;
    }
}
