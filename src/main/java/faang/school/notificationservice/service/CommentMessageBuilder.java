package faang.school.notificationservice.service;


import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<UserDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto commentAuthor, Locale locale) {
        Object[] args = {commentAuthor.getUsername()};
        return messageSource.getMessage("comment.new", args, locale);
    }
}