package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messages.MessageBuilder;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageBuilder<Class<?>> messageBuilder;
    private UserDto userDto;

    private Locale usLocale;
    private Class postLike;

    private AbstractEventListener<Class<?>> eventListener;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("M@A")
                .preference(PreferredContact.EMAIL)
                .build();

        usLocale = Locale.US;
        postLike = Object.class;

        eventListener = new AbstractEventListener(null, userServiceClient, List.of(notificationService), messageBuilder) {
            @Override
            public void onMessage(@NonNull Message message, byte[] pattern) {

            }
        };
    }

    @Test
    void testSendNotificationDataValidationException() {
        when(userServiceClient.getUserInternal(1L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(PreferredContact.SMS);

        assertThrows(DataValidationException.class,
                () -> eventListener.sendNotification(1L, "Test"));
    }

    @Test
    void testSendNotification() {
        when(userServiceClient.getUserInternal(userDto.id())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);

        eventListener.sendNotification(userDto.id(), "Test");
        verify(notificationService).send(userDto, "Test");
    }
}