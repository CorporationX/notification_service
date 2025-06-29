package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.listener.data.UserData;
import faang.school.notificationservice.listener.subscription.NewFollowerEventListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NewFollowerEventListenerTest {

    @InjectMocks
    @Spy
    private NewFollowerEventListener newFollowerEventListener;

    @Test
    public void testInvalidEventDontSendNotification() {
        ArgumentCaptor<NewFollowerEvent> eventNotificationCaptor = ArgumentCaptor.forClass(NewFollowerEvent.class);
        NewFollowerEvent newFollowerEvent = NewFollowerEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .follower(null)
                .build();

        newFollowerEventListener.handle(newFollowerEvent);

        verify(newFollowerEventListener, times(1)).sendNotification(eventNotificationCaptor.capture());
        verify(newFollowerEventListener, never()).sendMessage(any(UserDto.class), anyString());

        assertEquals(UserData.CORRECT_USER_DTO.getId(), eventNotificationCaptor.getValue().getOwner().getId());
    }

    @Test
    public void testValidEventSendNotification() {
        ArgumentCaptor<NewFollowerEvent> eventNotificationCaptor = ArgumentCaptor.forClass(NewFollowerEvent.class);
        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        NewFollowerEvent newFollowerEvent = NewFollowerEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .follower(UserData.CORRECT_USER_DTO)
                .build();

        doNothing().when(newFollowerEventListener).sendMessage(any(UserDto.class), any());
        doReturn("Mocked message").when(newFollowerEventListener).getMessage(any(NewFollowerEvent.class));

        newFollowerEventListener.handle(newFollowerEvent);

        verify(newFollowerEventListener, times(1)).sendNotification(eventNotificationCaptor.capture());
        verify(newFollowerEventListener, times(1)).sendMessage(userCaptor.capture(), messageCaptor.capture());

        assertEquals(userCaptor.getValue().getId(), eventNotificationCaptor.getValue().getOwner().getId());
        assertEquals("Mocked message", messageCaptor.getValue());
    }

    @ParameterizedTest
    @MethodSource("faang.school.notificationservice.listener.data.UserData#invalidNewFollowerEvents")
    public void testInvalidNewFollowerEventValidation(UserDto owner, UserDto follower) {
        NewFollowerEvent unfollowEvent = NewFollowerEvent.builder()
                .owner(owner)
                .follower(follower)
                .build();

        newFollowerEventListener.handle(unfollowEvent);

        assertFalse(newFollowerEventListener.isEventValid(unfollowEvent));
    }
}