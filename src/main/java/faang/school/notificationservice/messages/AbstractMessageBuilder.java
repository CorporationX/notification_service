package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public abstract class AbstractMessageBuilder<T> {
    protected final UserServiceClient userServiceClient;
    protected final MessageSource messageSource;
}
