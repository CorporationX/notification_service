package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public abstract class MessageBuilder<E> {

    protected final MessageSource messageSource;

    public abstract Class<E> getInstance();

    public abstract String buildMessage(UserDto userDto, E event);
}
