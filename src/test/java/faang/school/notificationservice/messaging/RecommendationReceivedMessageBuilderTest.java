package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.Recommendation;
import faang.school.notificationservice.dto.RecommendationEventBuilder;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecommendationReceivedMessageBuilderTest {
    @Test
    void buildMessage_usesMessageSourceWithCorrectArgs() {
        MessageSource ms = mock(MessageSource.class);
        RecommendationReceivedMessageBuilder builder =
                new RecommendationReceivedMessageBuilder(ms);

        UserDto author = new UserDto();
        author.setUsername("Danila");

        Recommendation rec = new Recommendation(1L, 2L, "You are super!");

        RecommendationEventBuilder event = RecommendationEventBuilder.builder()
                .author(author)
                .receiver(new UserDto())
                .recommendation(rec)
                .build();

        when(ms.getMessage(eq("recommendation.receive"),
                eq(new Object[]{"Danila", "You are super!"}),
                eq(Locale.UK)))
                .thenReturn("MSG");

        String result = builder.buildMessage(event, Locale.UK);

        assertThat(result).isEqualTo("MSG");
        verify(ms).getMessage(eq("recommendation.receive"),
                eq(new Object[]{"Danila", "You are super!"}),
                eq(Locale.UK));
    }
}