package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.SubscriptionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SubscriptionMessageBuilder subscriptionMessageBuilder;

    @Test
    void testBuildMessageSuccess() {
        SubscriptionEvent subscriptionEvent = SubscriptionEvent.builder()
                .followeeName("Followee")
                .followerName("Follower")
                .subscribedAt(LocalDateTime.now())
                .build();
        Locale locale = Locale.ENGLISH;
        String code = "subscription.new";
        Object[] placeholders = {subscriptionEvent.getFolloweeName(), subscriptionEvent.getFollowerName(),
                subscriptionEvent.getSubscribedAt()};
        String message = "Hello, {0}! {1} started following you at {2}. Congratulations!";

        when(messageSource.getMessage(code, placeholders, locale)).thenReturn(message);

        String result = subscriptionMessageBuilder.buildMessage(subscriptionEvent, locale);

        assertThat(result).isEqualTo(message);
    }

    @Test
    void testGetInstanceSuccess() {
        SubscriptionMessageBuilder messageBuilder = new SubscriptionMessageBuilder(null);

        Class<?> result = messageBuilder.getInstance();

        assertThat(result).isEqualTo(SubscriptionEvent.class);
    }
}