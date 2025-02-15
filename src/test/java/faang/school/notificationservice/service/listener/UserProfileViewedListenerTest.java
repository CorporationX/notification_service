package faang.school.notificationservice.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.kafka.UserProfileViewedDto;
import faang.school.notificationservice.exception.impl.non_retryable.DuplicateEventException;
import faang.school.notificationservice.exception.impl.non_retryable.ListSizeNotOneException;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import faang.school.notificationservice.exception.impl.retryable.UserServiceClientException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.EventService;
import faang.school.notificationservice.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileViewedListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<UserProfileViewedDto> messageBuilder;
    @Mock
    private NotificationService notificationService;
    @Mock
    private EventService eventService;

    @InjectMocks
    private UserProfileViewedListener listener;

    private UserProfileViewedDto inputDto;
    private UserServiceDto profileOwner;
    private UserServiceDto viewer;
    private ConsumerRecord<String, Object> kafkaEvent;
    private UUID eventKey;

    @BeforeEach
    void setUp() {
        listener = new UserProfileViewedListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService),
                eventService
        );
        eventKey = UUID.randomUUID();
        kafkaEvent = mock(ConsumerRecord.class);
        inputDto = new UserProfileViewedDto(2L, 1L);
        profileOwner = UserServiceDto.builder()
                .id(1L)
                .username("ProfileOwner")
                .preference(UserServiceDto.PreferredContact.SMS)
                .locale(Locale.ENGLISH)
                .build();
        viewer = UserServiceDto.builder()
                .id(2L)
                .username("Viewer")
                .preference(UserServiceDto.PreferredContact.SMS)
                .locale(Locale.ENGLISH)
                .build();
    }

    @Test
    void sendNotificationSucess() {
        when(kafkaEvent.key()).thenReturn(eventKey.toString());
        when(objectMapper.convertValue(any(), eq(UserProfileViewedDto.class))).thenReturn(inputDto);
        when(eventService.existsById(eventKey)).thenReturn(false);
        when(userServiceClient.getOrderedUsers(anyList()))
                .thenReturn(List.of(profileOwner, viewer));

        when(messageBuilder.getInstance()).thenAnswer(invocation -> UserProfileViewedDto.class);
        when(messageBuilder.buildMessage(any(), any(), any())).thenReturn("Test message");
        when(notificationService.getPreferredContact()).thenReturn(profileOwner.getPreference());

        listener.listen(kafkaEvent);

        verify(notificationService).send(profileOwner, "Test message");
    }

    @Test
    void eventDuplicateException() {
        when(kafkaEvent.key()).thenReturn(eventKey.toString());
        when(eventService.existsById(eventKey)).thenReturn(true);

        assertThrows(DuplicateEventException.class, () -> listener.listen(kafkaEvent));
    }

    @Test
    void usersNotFoundException() {
        when(kafkaEvent.key()).thenReturn(eventKey.toString());
        when(objectMapper.convertValue(any(), eq(UserProfileViewedDto.class))).thenReturn(inputDto);
        when(eventService.existsById(eventKey)).thenReturn(false);
        when(userServiceClient.getOrderedUsers(List.of(1L, 2L)))
                .thenReturn(List.of(profileOwner));

        assertThrows(NotFoundElementException.class, () -> listener.listen(kafkaEvent));
    }

    @Test
    void unreachableFeignThrowException() {
        when(kafkaEvent.key()).thenReturn(eventKey.toString());
        when(objectMapper.convertValue(any(), eq(UserProfileViewedDto.class))).thenReturn(inputDto);
        when(eventService.existsById(eventKey)).thenReturn(false);
        when(userServiceClient.getOrderedUsers(anyList()))
                .thenThrow(new UserServiceClientException("ex"));

        assertThrows(UserServiceClientException.class, () -> listener.listen(kafkaEvent));
    }

    @Test
    void notFoundBuilderThrowException() {
        listener = new UserProfileViewedListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService),
                eventService
        );
        when(kafkaEvent.key()).thenReturn(eventKey.toString());
        when(objectMapper.convertValue(any(), eq(UserProfileViewedDto.class))).thenReturn(inputDto);
        when(eventService.existsById(eventKey)).thenReturn(false);
        when(userServiceClient.getOrderedUsers(anyList()))
                .thenReturn(List.of(profileOwner, viewer));
        when(messageBuilder.getInstance()).thenAnswer(invocation -> UserProfileViewedListener.class);

        assertThrows(NotFoundElementException.class, () -> listener.listen(kafkaEvent));
    }

    @Test
    void duplicateBuilderThrowException() {
        listener = new UserProfileViewedListener(
                objectMapper,
                userServiceClient,
                List.of(messageBuilder, messageBuilder),
                Collections.singletonList(notificationService),
                eventService
        );
        when(kafkaEvent.key()).thenReturn(eventKey.toString());
        when(objectMapper.convertValue(any(), eq(UserProfileViewedDto.class))).thenReturn(inputDto);
        when(eventService.existsById(eventKey)).thenReturn(false);
        when(userServiceClient.getOrderedUsers(anyList()))
                .thenReturn(List.of(profileOwner, viewer));
        when(messageBuilder.getInstance()).thenAnswer(invocation -> UserProfileViewedDto.class);

        assertThrows(ListSizeNotOneException.class, () -> listener.listen(kafkaEvent));
    }
}
