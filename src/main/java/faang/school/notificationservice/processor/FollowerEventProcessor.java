package faang.school.notificationservice.processor;

import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.service.MessageService;
import faang.school.notificationservice.service.NotificationDispatcher;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FollowerEventProcessor extends AbstractEventProcessor<FollowerEventDto> {

    public FollowerEventProcessor(
            MessageService messageService,
            NotificationDispatcher notificationDispatcher) {
        super(messageService, notificationDispatcher);
    }

    @Override
    protected Locale extractLocale(FollowerEventDto event) {
        return event.getLocale() != null ?
                Locale.forLanguageTag(String.valueOf(event.getLocale())) : Locale.getDefault();
    }

    @Override
    public long extractUserId(FollowerEventDto event) {
        return event.getFolloweeId();
    }

    @Override
    public Class<FollowerEventDto> getEventType() {
        return FollowerEventDto.class;
    }
}