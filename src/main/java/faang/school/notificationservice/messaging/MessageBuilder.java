package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserServiceDto;

import java.util.List;

public interface MessageBuilder<T> {

    Class<?> getInstance();

    String buildMessage(T inputDto, UserServiceDto profileOwner, List<String> additionalWordsForOwnerMessage);
}
