package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto notifiedUser, Locale locale, Object[] additionData) {

        if (additionData != null){
            Object[] args = {additionData[0],additionData[1],additionData[2]};
            return messageSource.getMessage("post.like.new", args, locale);
        } else {
            log.info("Invalid method parameters, buildMessage()" );
            throw new DataValidationException("Invalid method parameters");
        }
    }

    @Override
    public LikeEvent getEvent(LikeEvent likeEvent) {
        return likeEvent;
    }
}
