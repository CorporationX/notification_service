package faang.school.notificationservice.messages.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.LikeEventDto;
import faang.school.notificationservice.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeMessageBuilder implements MessageBuilder<LikeEventDto> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String builderMessage(LikeEventDto likeEvent, Locale locale) {
        log.info("Подготавливаем запрос в FeignClient на получения пользователя c ID: {}", likeEvent.getAuthorId());
        UserDto user = userServiceClient.getUser(likeEvent.getAuthorId());
        log.info("Получили пользователя c ID: {} через FeignClient", likeEvent.getAuthorId());
        return messageSource.getMessage("liked.post.new", new Object[]{user.getUsername()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return LikeEventDto.class;
    }
}
