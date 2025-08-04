package faang.school.notificationservice.messaging.like;

import com.fasterxml.jackson.databind.JsonNode;
import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public abstract class LikeNotificationsMessageBuilder implements MessageBuilder<AggregatedNotificationsDto> {

    protected final MessageSource messageSource;

    protected abstract String getMessageTemplateCode(EventType eventType, int count);

    @Override
    public String buildMessage(AggregatedNotificationsDto notificationsDto, Locale locale) {
        int count = notificationsDto.getNotificationCount();
        JsonNode eventData = notificationsDto.getEventData();
        String likerUsername = eventData.get("likerUsername").asText();
        String content = eventData.get("shortContent").asText();
        EventType eventType = notificationsDto.getEventType();

        if (count > 1) {
            return getMultipleLikesMessage(likerUsername, content, getMessageTemplateCode(eventType, count), locale);
        }

        return getSingleLikeMessage(likerUsername, content, getMessageTemplateCode(eventType, count), locale);
    }

    private String getSingleLikeMessage(
            String likerUsername, String content, String messageTemplateCode, Locale locale
    ) {
        return messageSource.getMessage(messageTemplateCode,
                new Object[]{content, likerUsername},
                locale
        );
    }

    private String getMultipleLikesMessage(
            String likerUsername, String content, String messageTemplateCode, Locale locale
    ) {
        return messageSource.getMessage(messageTemplateCode,
                new Object[]{content, likerUsername},
                locale
        );
    }
}