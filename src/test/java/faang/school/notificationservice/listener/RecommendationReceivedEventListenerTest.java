package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.RecommendationReceivedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.message_builder.RecommendationReceivedEventMessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationReceivedEventListenerTest {
    private final static long DEFAULT_ID = 1L;
    private final static long RECOMMENDATION_ID = 16L;
    private final static long RECEIVER_ID = 7L;

    private final long recommendation_id = RECOMMENDATION_ID;
    private final long author_id = DEFAULT_ID;
    private final long receiver_id = RECEIVER_ID;

    private final RecommendationReceivedEventDto recommendationReceivedEventDto = RecommendationReceivedEventDto.builder()
            .id(recommendation_id)
            .authorId(author_id)
            .receiverId(receiver_id)
            .createdAt(LocalDateTime.now())
            .build();

    private final UserDto userDto = UserDto.builder()
            .id(recommendationReceivedEventDto.receiverId())
            .preference(UserDto.PreferredContact.SMS)
            .locale(Locale.CANADA)
            .build();

    @Captor
    private ArgumentCaptor<UserDto> userDtoArgumentCaptor;

    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;
    @Mock
    private RecommendationReceivedEventMessageBuilder recommendationReceivedEventMessageBuilder;

    private RecommendationReceivedEventListener recommendationReceivedEventListener;

    @BeforeEach
    void prepareTestData() {
        when(notificationService.getPreferredContact()).thenReturn(userDto.getPreference());
        recommendationReceivedEventListener = new RecommendationReceivedEventListener(userService,
                recommendationReceivedEventMessageBuilder,
                List.of(notificationService));
    }

    @Test
    void testHandleRecommendationReceivedEvent() {
        String messageText = "Message Text";

        when(recommendationReceivedEventMessageBuilder.buildMessage(Mockito.any(RecommendationReceivedEventDto.class),
                Mockito.any(Locale.class))).thenReturn(messageText);
        when(userService.getUser(userDto.getId())).thenReturn(userDto);

        recommendationReceivedEventListener.handleRecommendationReceivedEvent(recommendationReceivedEventDto);

        verify(notificationService).send(userDtoArgumentCaptor.capture(), Mockito.eq(messageText));
        UserDto capturedUserDto = userDtoArgumentCaptor.getValue();
        Assertions.assertEquals(userDto.getId(), capturedUserDto.getId());
    }
}
