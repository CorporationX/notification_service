package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillOfferEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.exception.NotFoundException;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.message.SkillOfferMessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.notification.TelegramService;
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
class SkillOfferListenerTest {
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private TelegramService telegramService;
    @Mock
    private SkillOfferMessageBuilder messageBuilder;
    private SkillOfferEventDto event;
    @InjectMocks
    private SkillOfferListener skillOfferListener;
    private UserDto userDto;
    private final String message = "You got a new skill";

    @BeforeEach
    void setUp() {
        List<NotificationService> notificationServices = new ArrayList<>(List.of(telegramService));

        List<MessageBuilder<SkillOfferEventDto>> messageBuilders = new ArrayList<>(List.of(messageBuilder));

        event = SkillOfferEventDto.builder()
                .skillOfferId(1L)
                .receiverId(1L)
                .authorId(2L)
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .preferredContact(PreferredContact.TELEGRAM)
                .build();

        skillOfferListener = new SkillOfferListener(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Test
    void testGetMessage() {
        Mockito.when(messageBuilder.supports(SkillOfferMessageBuilder.class)).thenReturn(true);
        Mockito.when(messageBuilder.buildMessage(event, Locale.ENGLISH)).thenReturn(message);

        String actual = skillOfferListener.getMessage(SkillOfferMessageBuilder.class, event);

        assertEquals(message, actual);
    }

    @Test
    void testGetMessageThrowException() {
        Mockito.when(messageBuilder.supports(SkillOfferMessageBuilder.class)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> skillOfferListener.getMessage(SkillOfferMessageBuilder.class, event));
    }

    @Test
    void testSendNotification() {
        Mockito.when(userServiceClient.getUser(event.getReceiverId())).thenReturn(userDto);
        Mockito.when(telegramService.getPreferredContact()).thenReturn(PreferredContact.TELEGRAM);

        skillOfferListener.sendNotification(event.getReceiverId(), message);

        Mockito.verify(telegramService).sendNotification(message);
    }
}