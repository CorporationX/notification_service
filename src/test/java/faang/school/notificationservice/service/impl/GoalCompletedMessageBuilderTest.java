package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.GoalDto;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.GoalCompletedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GoalCompletedMessageBuilder goalCompletedMessageBuilder;

    private GoalCompletedEvent goalCompletedEvent;
    private UserDto receiverDto;
    private GoalDto goalDto;

    @BeforeEach
    public void setUp() {
        receiverDto = new UserDto();
        receiverDto.setId(1L);
        receiverDto.setUsername("Receiver");

        goalDto = new GoalDto();
        goalDto.setId(1L);
        goalDto.setTitle("Some Title");

        goalCompletedEvent = new GoalCompletedEvent();
        goalCompletedEvent.setUserId(receiverDto.getId());
        goalCompletedEvent.setGoalId(goalDto.getId());
    }

    @Test
    @DisplayName("Should build correct message")
    public void testBuildMessage_Success() {
        when(userServiceClient.getUser(receiverDto.getId())).thenReturn(receiverDto);
        when(userServiceClient.getGoal(goalDto.getId())).thenReturn(goalDto);
        when(messageSource.getMessage(eq("goal.completed"), any(Object[].class), eq(Locale.UK)))
                .thenReturn("Receiver completed a new goal");

        String message = goalCompletedMessageBuilder.buildMessage(goalCompletedEvent, Locale.UK);

        verify(userServiceClient).getUser(receiverDto.getId());
        verify(userServiceClient).getGoal(goalDto.getId());
        verify(messageSource).getMessage(eq("goal.completed"), any(Object[].class), eq(Locale.UK));

        assertEquals("Receiver completed a new goal", message);

        verify(messageSource).getMessage(eq("goal.completed"),
                argThat(args -> args[0].equals(receiverDto.getUsername())
                        && args[1].equals(goalDto.getTitle())),
                eq(Locale.UK));
    }

    @Test
    @DisplayName("Should return correct supported class")
    public void testGetSupportedClass_Success() {
        assertEquals(GoalCompletedEvent.class, goalCompletedMessageBuilder.getSupportedClass());
    }

    @Test
    @DisplayName("Should build message for a different locale")
    public void testBuildMessage_FrenchLocale() {
        when(userServiceClient.getUser(receiverDto.getId())).thenReturn(receiverDto);
        when(userServiceClient.getGoal(goalDto.getId())).thenReturn(goalDto);
        when(messageSource.getMessage(eq("goal.completed"), any(Object[].class), eq(Locale.FRANCE)))
                .thenReturn("Message en français");

        String message = goalCompletedMessageBuilder.buildMessage(goalCompletedEvent, Locale.FRANCE);

        assertEquals("Message en français", message);

        verify(messageSource).getMessage(eq("goal.completed"),
                argThat(args -> args[0].equals(receiverDto.getUsername())
                        && args[1].equals(goalDto.getTitle())),
                eq(Locale.FRANCE ));
    }

    @Test
    @DisplayName("Should handle exception when fetching user data")
    public void testBuildMessage_UserServiceError() {
        when(userServiceClient.getUser(receiverDto.getId()))
                .thenThrow(new RuntimeException("User service error"));

        assertThrows(RuntimeException.class, () ->
                goalCompletedMessageBuilder.buildMessage(goalCompletedEvent, Locale.UK));

        verify(userServiceClient, never()).getGoal(goalDto.getId());
        verify(messageSource, never()).getMessage(any(), any(), any());
    }
}
