package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.client.service.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public abstract class AbstractMessageBuilder {
    protected final UserServiceClient userServiceClient;
    protected final MessageSource messageSource;
}
