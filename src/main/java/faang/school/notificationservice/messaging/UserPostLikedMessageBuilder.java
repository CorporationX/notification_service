package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPostLikedMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(LikeEvent inputDto, UserServiceDto postAuthor,
                               List<String> additionalWordsForOwnerMessage) {
        String ownername = additionalWordsForOwnerMessage.get(0);

        return messageSource.getMessage(
                "notification.user.post.liked",
                new Object[]{ownername},
                postAuthor.getLocale()
        );
    }

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }
}