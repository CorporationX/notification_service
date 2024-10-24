package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.RecommendationReceivedEvent;
import faang.school.notificationservice.model.event.UserFollowerEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<RecommendationReceivedEvent> getSupportedClass() {
        return RecommendationReceivedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        UserDto authorDto = userServiceClient.getUser(event.getAuthorId());
        UserDto receiverDto = userServiceClient.getUser(event.getReceiverUserId());
        return messageSource.getMessage("recommendation.received",
                new Object[]{authorDto.getUsername(), receiverDto.getUsername(), event.getRecommendationId()},
                locale);
    }
}