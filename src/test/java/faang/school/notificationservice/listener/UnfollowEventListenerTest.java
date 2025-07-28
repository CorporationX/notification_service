package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.kafka.UnfollowEvent;
import faang.school.notificationservice.listener.data.UserData;
import faang.school.notificationservice.listener.subscription.UnfollowEventListener;
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
public class UnfollowEventListenerTest {

    @Spy
    @InjectMocks
    private UnfollowEventListener unfollowEventListener;

    @Test
    public void testInvalidEventDontSendNotification() {
        UnfollowEvent unfollowEvent = UnfollowEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .follower(null)
                .build();

        unfollowEventListener.handle(unfollowEvent);

        verify(unfollowEventListener, never()).sendNotification(any(UnfollowEvent.class)); // sendNotification не должен вызываться
    }


    @Test
    public void testValidEventSendNotification() {
        ArgumentCaptor<UnfollowEvent> eventCaptor = ArgumentCaptor.forClass(UnfollowEvent.class);

        UnfollowEvent unfollowEvent = UnfollowEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .follower(UserData.CORRECT_USER_DTO)
                .build();

        doNothing().when(unfollowEventListener).sendNotification(any(UnfollowEvent.class));

        unfollowEventListener.handle(unfollowEvent);

        verify(unfollowEventListener, times(1)).sendNotification(eventCaptor.capture());
        assertEquals(UserData.CORRECT_USER_DTO.getId(), eventCaptor.getValue().getOwner().getId());
    }

    @ParameterizedTest
    @MethodSource("faang.school.notificationservice.listener.data.UserData#invalidUnfollowEvents")
    public void testInvalidUnfollowEventValidation(UserDto owner, UserDto follower) {
        UnfollowEvent unfollowEvent = UnfollowEvent.builder()
                .owner(owner)
                .follower(follower)
                .build();

        assertFalse(unfollowEventListener.isEventValid(unfollowEvent));
    }

    @Test
    public void testValidUnfollowEventValidation() {
        UnfollowEvent unfollowEvent = UnfollowEvent.builder()
                .owner(UserData.CORRECT_USER_DTO)
                .follower(UserData.CORRECT_USER_DTO)
                .build();

        assertTrue(unfollowEventListener.isEventValid(unfollowEvent));
    }
}