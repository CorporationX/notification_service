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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private FollowerMessageBuilder followerMessageBuilder;

    @Test
    void testBuildMessage() {
        SubscriptionEventDto eventDto = SubscriptionEventDto.builder()
                .followerId(1L)
                .followeeId(2L)
                .eventTime(LocalDateTime.now())
                .build();

        when(messageSource.getMessage(eq("follow.message"), any(), eq(Locale.getDefault())))
                .thenReturn("User 1 started following you at 2023-01-01 12:00");

        String result = followerMessageBuilder.buildMessage(eventDto, Locale.getDefault());

        assertNotNull(result);
        assertTrue(result.contains("User 1 started following you"));
    }

    @Test
    void testGetMessageKey() {
        assertEquals("follow.message", followerMessageBuilder.getMessageKey());
    }

    @Test
    void testGetInstance() {
        assertEquals(SubscriptionEventDto.class, followerMessageBuilder.getInstance());
    }
}
