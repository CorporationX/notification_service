package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.LikePostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikePostMessageBuilder implements MessageBuilder<LikePostResponseDto> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return LikePostResponseDto.class;
    }

    @Override
    public String buildMessage(LikePostResponseDto event, Locale locale) {
        return messageSource.getMessage("likePost.new", new Object[]{
                        event.getLikedUserId(),
                        event.getPostId(),
                        event.getLikeTime()},
                locale);
    }
}
