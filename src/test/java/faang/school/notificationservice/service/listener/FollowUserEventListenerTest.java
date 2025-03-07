package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.kafka.FollowUserDto;
import faang.school.notificationservice.handler.EventHandler;
import faang.school.notificationservice.handler.KafkaMapperHandler;
import faang.school.notificationservice.handler.MessageHandler;
import faang.school.notificationservice.handler.NotificationServiceHandler;
import faang.school.notificationservice.handler.UserServiceHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unckecked")
@ExtendWith(MockitoExtension.class)
public class FollowUserEventListenerTest {
    @Mock
    private EventHandler eventHandler;

    @Mock
    private NotificationServiceHandler notificationServiceHandler;

    @Mock
    private MessageHandler<FollowUserDto> messageHandler;

    @Mock
    private KafkaMapperHandler kafkaMapperHandler;

    @Mock
    private UserServiceHandler userServiceHandler;

    private TestFollowUserEventListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listener = new TestFollowUserEventListener(
                eventHandler,
                notificationServiceHandler,
                messageHandler,
                kafkaMapperHandler,
                userServiceHandler);
    }

    @Test
    void testListenTopic() {
        FollowUserDto followUserDto = new FollowUserDto(1L, 2L, LocalDateTime.of(2023, 3, 15, 10, 30));
        ConsumerRecord<String, Object> record = new ConsumerRecord<>(
                "user-follows-user-topic", 0, 0L, "dummyKey", followUserDto);

        UserServiceDto followeeDto = UserServiceDto.builder()
                .id(2L)
                .username("followee")
                .preference(UserServiceDto.PreferredContact.SMS)
                .build();

        UserServiceDto followerDto = UserServiceDto.builder()
                .id(1L)
                .username("follower")
                .preference(UserServiceDto.PreferredContact.SMS)
                .build();

        when(userServiceHandler.getSingleUser(1L)).thenReturn(followerDto);
        when(userServiceHandler.getSingleUser(2L)).thenReturn(followeeDto);

        TestFollowUserEventListener spyListener = spy(listener);

        doReturn(followUserDto)
                .when(spyListener)
                .handleUniqueEvent(any(ConsumerRecord.class), eq(FollowUserDto.class));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        String formattedDate = followUserDto.followedAt().format(formatter);

        List<String> expectedArguments = List.of(followerDto.getUsername(), formattedDate);

        doReturn("Test message")
                .when(spyListener)
                .getMessage(eq(followUserDto), eq(followeeDto), eq(expectedArguments));

        spyListener.listenTopic(record);

        assertSame(followeeDto, spyListener.getCapturedUser());
        assertEquals("Test message", spyListener.getCapturedMessage());

        ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        verify(spyListener).getMessage(eq(followUserDto), eq(followeeDto), captor.capture());
        List<String> actualArguments = captor.getValue();
        assertEquals(expectedArguments, actualArguments);
    }

    static class TestFollowUserEventListener extends FollowUserEventListener {
        private UserServiceDto capturedUser;
        private String capturedMessage;

        public TestFollowUserEventListener(EventHandler eventHandler,
                                           NotificationServiceHandler notificationServiceHandler,
                                           MessageHandler<FollowUserDto> messageHandler,
                                           KafkaMapperHandler kafkaMapperHandler,
                                           UserServiceHandler userServiceHandler) {
            super(eventHandler, notificationServiceHandler, messageHandler, kafkaMapperHandler, userServiceHandler);
        }

        @Override
        protected void sendSingleNotification(UserServiceDto user, String message) {
            this.capturedUser = user;
            this.capturedMessage = message;
        }

        public UserServiceDto getCapturedUser() {
            return capturedUser;
        }

        public String getCapturedMessage() {
            return capturedMessage;
        }
    }
}