package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.listener.subscription.NewFollowerEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NewFollowerEventListenerTest {

    @Value("${spring.kafka.topics.subscription.new-follower-topic.name}")
    private String newFollowerTopic;

    @Mock
    private MessageBuilder<NewFollowerEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    @Spy
    private NewFollowerEventListener newFollowerEventListener;

    @ParameterizedTest
    @MethodSource("invalidNewFollowerEvents")
    public void testInvalidEventDontSendNotification(NewFollowerEvent newFollowerEvent) {
        ArgumentCaptor<NewFollowerEvent> eventNotificationCaptor = ArgumentCaptor.forClass(NewFollowerEvent.class);

        newFollowerEventListener.handle(newFollowerEvent);

        verify(newFollowerEventListener, times(1)).sendNotification(eventNotificationCaptor.capture());
        verify(newFollowerEventListener, never()).sendMessage(any(UserDto.class), anyString());
    }

    private static Stream<Arguments> invalidNewFollowerEvents() {
        UserDto correctUserDto = UserDto.builder()
                .id(1L)
                .email("email")
                .phone("phone")
                .username("username")
                .build();

        UserDto noIdUserDto = UserDto.builder()
                .email("email")
                .phone("phone")
                .username("username")
                .build();

        UserDto noEmailUserDto = UserDto.builder()
                .id(1L)
                .phone("phone")
                .username("username")
                .build();

        UserDto noPhoneUserDto = UserDto.builder()
                .id(1L)
                .email("email")
                .username("username")
                .build();

        UserDto noUsernameUserDto = UserDto.builder()
                .id(1L)
                .email("email")
                .phone("phone")
                .build();

        NewFollowerEvent nullUsers = NewFollowerEvent.builder()
                .owner(null)
                .follower(null)
                .build();

        NewFollowerEvent ownerCorrectEvent = NewFollowerEvent.builder()
                .owner(correctUserDto)
                .follower(null)
                .build();

        NewFollowerEvent followerCorrectEvent = NewFollowerEvent.builder()
                .owner(null)
                .follower(correctUserDto)
                .build();
        // id
        NewFollowerEvent noIdOwner = NewFollowerEvent.builder()
                .owner(noIdUserDto)
                .follower(correctUserDto)
                .build();

        NewFollowerEvent noIdFollower = NewFollowerEvent.builder()
                .owner(correctUserDto)
                .follower(noIdUserDto)
                .build();
        // email

        NewFollowerEvent noEmailOwner = NewFollowerEvent.builder()
                .owner(noEmailUserDto)
                .follower(correctUserDto)
                .build();

        NewFollowerEvent noEmailFollower = NewFollowerEvent.builder()
                .owner(correctUserDto)
                .follower(noEmailUserDto)
                .build();

        // phone

        NewFollowerEvent noPhoneOwner = NewFollowerEvent.builder()
                .owner(noPhoneUserDto)
                .follower(correctUserDto)
                .build();

        NewFollowerEvent noPhoneFollower = NewFollowerEvent.builder()
                .owner(correctUserDto)
                .follower(noPhoneUserDto)
                .build();

        // username

        NewFollowerEvent noUsernameOwner = NewFollowerEvent.builder()
                .owner(noUsernameUserDto)
                .follower(correctUserDto)
                .build();

        NewFollowerEvent noUsernameFollower = NewFollowerEvent.builder()
                .owner(correctUserDto)
                .follower(noUsernameUserDto)
                .build();


        return Stream.of(
                Arguments.of(nullUsers),
                Arguments.of(ownerCorrectEvent),
                Arguments.of(followerCorrectEvent),
                Arguments.of(noIdOwner),
                Arguments.of(noIdFollower),
                Arguments.of(noEmailOwner),
                Arguments.of(noEmailFollower),
                Arguments.of(noPhoneOwner),
                Arguments.of(noPhoneFollower),
                Arguments.of(noUsernameOwner),
                Arguments.of(noUsernameFollower)
        );
    }
}