package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.RecommendationRequestDto;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.RecommendationRequestedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestedMessageBuilder implements MessageBuilder<RecommendationRequestedEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<RecommendationRequestedEvent> getSupportedClass() {
        return RecommendationRequestedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
        UserDto requesterDto = userServiceClient.getUser(event.getRequesterId());
        UserDto receiverDto = userServiceClient.getUser(event.getReceiverId());
        RecommendationRequestDto recommendationRequestDto = userServiceClient.getRecommendationRequest(event.getRequestId());
        return messageSource.getMessage("recommendation.requested",
                new Object[]{receiverDto.getUsername(), requesterDto.getUsername(), recommendationRequestDto.getId()},
                locale);
    }
}