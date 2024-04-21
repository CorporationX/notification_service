package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LikeEventMessageBuilder extends AbstractMessageBuilder<LikeEvent> implements MessageBuilder<LikeEvent> {

    public LikeEventMessageBuilder(UserServiceClient userServiceClient,
                                   MessageSource messageSource) {
        super(userServiceClient, messageSource);
    }


    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return ""; //ToDo: посмотри как сконфигурировать MessageSource в spring, что бы он мог читать текст сообщения из messages.yaml
        // + смотри на 2 видео из 2 раздела с 55 минуты как делается это
    }

    @Override
    public Class<?> supportEventType() {
        return LikeEvent.class;
    }
}
