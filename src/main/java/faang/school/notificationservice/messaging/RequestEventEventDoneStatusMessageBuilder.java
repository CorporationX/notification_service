package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.RequestStatus;
import faang.school.notificationservice.event.RequestEventEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RequestEventEventDoneStatusMessageBuilder implements RequestEventEventStatusMessageBuilder {

    private final MessageSource messageSource;

    @Override
    public boolean isApplicable(RequestEventEvent event) {
        return event.requestStatus() == RequestStatus.DONE;
    }

    @Override
    public String buildMessage(RequestEventEvent event, UserDto userDto, Locale locale) {
        return messageSource.getMessage("request.done",
                new Object[]{userDto.getUsername(), event.id(), event.timestamp()},
                Locale.getDefault());
    }
}
