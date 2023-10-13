package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.exception.NotFoundException;
import faang.school.notificationservice.message.MentorshipOfferBuilder;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.message.RecommendationRequestMessageBuilder;
import faang.school.notificationservice.service.notification.EmailService;
import faang.school.notificationservice.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MentorshipOfferedListenerTest {
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private EmailService emailService;
    @Mock
    private MentorshipOfferBuilder messageBuilder;
    @InjectMocks
    private MentorshipOfferedListener mentorshipOfferedListener;
    private UserDto userDto;
    private MentorshipOfferedEventDto event;
    private final String message = "Congratulations! You've received a new mentorship offer";

    @BeforeEach
    public void init() {
        List<NotificationService> notificationServices = new ArrayList<>(List.of(emailService));
        List<MessageBuilder<MentorshipOfferedEventDto>> messageBuilders = new ArrayList<>(List.of(messageBuilder));
        mentorshipOfferedListener = new MentorshipOfferedListener(objectMapper, userServiceClient, notificationServices, messageBuilders);

        event = MentorshipOfferedEventDto.builder()
                .requesterId(1L)
                .receiverId(2L)
                .build();

        userDto = UserDto.builder()
                .id(2L)
                .preferredContact(PreferredContact.EMAIL)
                .build();
    }

    @Test
    public void sendNotificationTest() {
        Mockito.when(userServiceClient.getUser(event.getReceiverId())).thenReturn(userDto);
        Mockito.when(emailService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);
        mentorshipOfferedListener.sendNotification(event.getReceiverId(), message);

        Mockito.verify(emailService, Mockito.times(1)).sendNotification(event.getReceiverId(), message);
    }

    @Test
    public void getMessageTest() {
        Mockito.when(messageBuilder.buildMessage(event, Locale.ENGLISH)).thenReturn(message);
        Mockito.when(messageBuilder.supports(RecommendationRequestMessageBuilder.class)).thenReturn(true);
        assertEquals(message, mentorshipOfferedListener.getMessage(RecommendationRequestMessageBuilder.class, event));
    }

    @Test
    public void getMessageThrowsExceptionTest() {
        Mockito.when(messageBuilder.supports(RecommendationRequestMessageBuilder.class)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> mentorshipOfferedListener.getMessage(RecommendationRequestMessageBuilder.class, null));
    }

}