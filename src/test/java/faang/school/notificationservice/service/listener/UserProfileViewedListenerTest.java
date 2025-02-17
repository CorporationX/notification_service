package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.kafka.UserProfileViewedDto;
import faang.school.notificationservice.exception.handler.EventHandler;
import faang.school.notificationservice.exception.handler.KafkaMapperHandler;
import faang.school.notificationservice.exception.handler.MessageHandler;
import faang.school.notificationservice.exception.handler.NotificationServiceHandler;
import faang.school.notificationservice.exception.handler.UserServiceHandler;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileViewedListenerTest {
    public static final LocalDateTime CURRENT_TIME = LocalDateTime.now();

    @Mock
    private EventHandler eventHandler;
    @Mock
    private UserServiceHandler userServiceHandler;
    @Mock
    private NotificationServiceHandler notificationServiceHandler;
    @Mock
    private MessageHandler<UserProfileViewedDto> messageHandler;
    @Mock
    private KafkaMapperHandler kafkaMapperHandler;
    @InjectMocks
    private UserProfileViewedListener userProfileViewedListener;

    private UserProfileViewedDto inputDto;
    private ConsumerRecord<String, Object> kafkaEvent;

    @BeforeEach
    void setUp() {
        inputDto = UserProfileViewedDto.builder()
                .profileOwnerId(1L)
                .viewerId(2L)
                .viewedTime(CURRENT_TIME)
                .build();
        kafkaEvent = mock(ConsumerRecord.class);
    }

    @Test
    void testListen_Success() {
        UserServiceDto ownerDto = UserServiceDto.builder()
                .id(1L)
                .username("owner")
                .build();
        UserServiceDto viewerDto = UserServiceDto.builder()
                .id(2L)
                .username("viewer")
                .build();

        when(kafkaMapperHandler.mapAndValidateKafkaEvent(any(), eq(UserProfileViewedDto.class))).thenReturn(inputDto);
        when(userServiceHandler.getOrderedUsers(anyList())).thenReturn(List.of(ownerDto, viewerDto));
        when(messageHandler.getMessage(any(), any(), anyList())).thenReturn("Test message");

        userProfileViewedListener.listen(kafkaEvent);

        verify(notificationServiceHandler).sendSingleNotification(ownerDto, "Test message");
    }

    @Test
    void testListen_UserNotFound() {
        when(kafkaMapperHandler.mapAndValidateKafkaEvent(any(), eq(UserProfileViewedDto.class))).thenReturn(inputDto);
        when(userServiceHandler.getOrderedUsers(anyList())).thenReturn(List.of());

        assertThrows(NotFoundElementException.class, () -> userProfileViewedListener.listen(kafkaEvent));
    }
}
