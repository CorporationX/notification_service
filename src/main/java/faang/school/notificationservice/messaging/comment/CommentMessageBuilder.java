package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.comment.CommentEvent;
import faang.school.notificationservice.exception.ExceptionMessages;
import faang.school.notificationservice.exception.feign.UserServiceException;
import faang.school.notificationservice.messaging.MessageBuilder;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;


    @Override
    public Class<CommentEvent> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        UserDto userDto = fetchUser(event.getAuthorId());
        return messageSource.getMessage("comment.new", new Object[]{userDto.getUsername()}, locale);
    }

    private UserDto fetchUser(Long id) {
        UserDto user;
        try {
            user = userServiceClient.getUser(id);
        } catch (FeignException | RestClientException exception) {
            throw new UserServiceException(String.format(ExceptionMessages.USER_DATA_FETCH_FAILURE, id), exception);
        }
        return user;
    }
}
