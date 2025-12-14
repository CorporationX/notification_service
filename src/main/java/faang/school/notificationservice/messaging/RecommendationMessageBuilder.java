package faang.school.notificationservice.messaging;

import faang.school.notificationservice.async.AsyncUserService;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.recommendation.RecommendationEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class RecommendationMessageBuilder implements MessageBuilder<RecommendationEventDto> {
    private final MessageSource messageSource;
    private final AsyncUserService asyncUserService;

    @Override
    public String buildMessage(RecommendationEventDto event, Locale locale) {
        log.info("Starting to build message {}", event.content());
        CompletableFuture<UserDto> authorFuture = asyncUserService.getUserDtoAsync(event.authorId());
        CompletableFuture<UserDto> receiverFuture = asyncUserService.getUserDtoAsync(event.receiverId());
        return authorFuture.thenCombine(receiverFuture, (author, receiver) -> {
            String authorName = author.getUsername();
            String receiverName = receiver.getUsername();
            String content = event.content();

            String text  =  messageSource.getMessage(
                    "recommendation.received",
                    new Object[]{receiverName, authorName, content},
                    locale);
            log.info("Successful to build message {}", text);

            return text;
        }).join();
    }

    @Override
    public Class<RecommendationEventDto> getInstance() {
        return RecommendationEventDto.class;
    }
}