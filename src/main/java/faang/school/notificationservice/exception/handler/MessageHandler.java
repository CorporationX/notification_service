package faang.school.notificationservice.exception.handler;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.utils.ListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageHandler<T> {
    private final List<MessageBuilder<T>> messageBuilders;
    private final ListUtil listUtil;

    public String getMessage(T inputDto, UserServiceDto profileOwner, List<String> additionalWordsForOwnerMessage) {
        MessageBuilder<T> builder = listUtil.requireSingleElement(
                messageBuilders.stream()
                .filter(messageBuilder -> Objects.equals(messageBuilder.getInstance(), inputDto.getClass()))
                .toList(),
                MessageBuilder.class, inputDto.getClass());

        return builder.buildMessage(inputDto, profileOwner, additionalWordsForOwnerMessage);
    }
}
