package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnfollowerMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UnfollowerMessageBuilder unfollowerMessageBuilder;

    @Test
    void testBuildMessage() {
        SubscriptionEventDto eventDto = SubscriptionEventDto.builder()
                .followerId(1L)
                .followeeId(2L)
                .eventTime(LocalDateTime.now())
                .build();

        when(messageSource.getMessage(eq("unfollow.message"), any(), eq(Locale.getDefault())))
                .thenReturn("User 1 unfollowed you at 2023-01-01 12:00");

        String result = unfollowerMessageBuilder.buildMessage(eventDto, Locale.getDefault());

        assertNotNull(result);
        assertTrue(result.contains("User 1 unfollowed you"));
    }

    @Test
    void testGetMessageKey() {
        assertEquals("unfollow.message", unfollowerMessageBuilder.getMessageKey());
    }

    @Test
    void testGetInstance() {
        assertEquals(SubscriptionEventDto.class, unfollowerMessageBuilder.getInstance());
    }
}
