package faang.school.notificationservice.messaging;

import faang.school.notificationservice.async.AsyncUserService;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.recommendation.RecommendationEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationMessageBuilderTest {
    private RecommendationMessageBuilder messageBuilder;

    @Mock
    private MessageSource messageSource;

    @Mock
    private AsyncUserService asyncUserService;

    private static final Locale TEST_LOCALE = Locale.US;

    @BeforeEach
    void setUp() {
        messageBuilder = new RecommendationMessageBuilder(messageSource, asyncUserService);
    }

    @Test
    void buildMessage_shouldReturnFormattedMessageFromBundle() {
        long authorId = 1L;
        long receiverId = 2L;
        long recommendationId = 10L;
        String content = "Отличный специалист, рекомендую!";
        String authorUsername = "alex_dev";
        String receiverUsername = "maria_hr";

        RecommendationEventDto event = new RecommendationEventDto(
                authorId,
                receiverId,
                recommendationId,
                content,
                TEST_LOCALE
        );

        UserDto authorDto = new UserDto();
        authorDto.setUsername(authorUsername);
        UserDto receiverDto = new UserDto();
        receiverDto.setUsername(receiverUsername);

        when(asyncUserService.getUserDtoAsync(authorId))
                .thenReturn(CompletableFuture.completedFuture(authorDto));

        when(asyncUserService.getUserDtoAsync(receiverId))
                .thenReturn(CompletableFuture.completedFuture(receiverDto));

        String messageFromBundle = "%s, пользователь %s оставил вам рекомендацию: \"%s\"";
        String expectedFinalMessage = String.format(messageFromBundle, receiverUsername, authorUsername, content);

        when(messageSource.getMessage(
                eq("recommendation.received"),
                argThat(args -> args.length == 3 &&
                        args[0].equals(receiverUsername) &&
                        args[1].equals(authorUsername) &&
                        args[2].equals(content)),
                eq(TEST_LOCALE)))
                .thenReturn(expectedFinalMessage);

        String result = messageBuilder.buildMessage(event, TEST_LOCALE);

        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedFinalMessage);
    }

    @Test
    void getInstance_shouldReturnRecommendationEventDtoClass() {
        Class<RecommendationEventDto> result = messageBuilder.getInstance();

        assertThat(result).isEqualTo(RecommendationEventDto.class);
    }
}