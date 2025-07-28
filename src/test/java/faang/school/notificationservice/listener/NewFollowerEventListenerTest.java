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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
        NewFollowerEvent newFollowerEvent = NewFollowerEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .follower(null)
                .build();

        newFollowerEventListener.handle(newFollowerEvent);

        verify(newFollowerEventListener, never()).sendNotification(any(NewFollowerEvent.class));
    }

    @Test
    public void testValidEventSendNotification() {
        ArgumentCaptor<NewFollowerEvent> eventCaptor = ArgumentCaptor.forClass(NewFollowerEvent.class);
        NewFollowerEvent newFollowerEvent = NewFollowerEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .follower(UserData.CORRECT_USER_DTO)
                .build();

        doNothing().when(newFollowerEventListener).sendNotification(any(NewFollowerEvent.class));

        newFollowerEventListener.handle(newFollowerEvent);

        verify(newFollowerEventListener, times(1)).sendNotification(eventCaptor.capture());

        NewFollowerEvent capturedEvent = eventCaptor.getValue();
        assertEquals(UserData.CORRECT_USER_DTO.getId(), capturedEvent.getOwner().getId());
        assertEquals(UserData.CORRECT_USER_DTO.getId(), capturedEvent.getFollower().getId());
    }

    @ParameterizedTest
    @MethodSource("faang.school.notificationservice.listener.data.UserData#invalidNewFollowerEvents")
    public void testInvalidNewFollowerEventValidation(UserDto owner, UserDto follower) {
        NewFollowerEvent newFollowerEvent = NewFollowerEvent.builder()
                .owner(owner)
                .follower(follower)
                .build();

        assertFalse(newFollowerEventListener.isEventValid(newFollowerEvent));
    }

    @Test
    public void testValidNewFollowerEventValidation() {
        NewFollowerEvent newFollowerEvent = NewFollowerEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .follower(UserData.CORRECT_USER_DTO)
                .build();

        assertTrue(newFollowerEventListener.isEventValid(newFollowerEvent));
    }
}