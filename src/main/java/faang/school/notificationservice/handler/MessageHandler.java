package faang.school.notificationservice.handler;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.impl.non_retryable.ListSizeNotOneException;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import faang.school.notificationservice.messaging.MessageBuilder;
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

    public String getMessage(T inputDto, UserServiceDto profileOwner, List<String> additionalWordsForOwnerMessage) {
        List<MessageBuilder<T>> builders = messageBuilders.stream()
                .filter(messageBuilder -> Objects.equals(messageBuilder.getInstance(), inputDto.getClass()))
                .toList();
        MessageBuilder<T> builder = getSingleBuilderThrow(builders, inputDto);

        return builder.buildMessage(inputDto, profileOwner, additionalWordsForOwnerMessage);
    }

    private MessageBuilder<T> getSingleBuilderThrow(List<MessageBuilder<T>> list, T inputDto) {
        int size = list.size();

        if (size == 0) {
            String error = String.format("No message builder found for %s", inputDto.getClass());
            log.error(error);
            throw new NotFoundElementException(error);
        }

        if (size != 1) {
            String error = String.format("Expected exactly 1 message builder for: %s, but got %d",
                    inputDto.getClass(), size);
            log.error(error);
            throw new ListSizeNotOneException(error);
        }

        return list.get(0);
    }
}
