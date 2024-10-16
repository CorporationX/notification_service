package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.SkillDto;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.SkillAcquiredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.List;
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
class ProfileViewMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SkillAcquiredMessageBuilder skillAcquiredMessageBuilder;

    private SkillAcquiredEvent skillAcquiredEvent;
    private UserDto receiverDto;
    private SkillDto skillDto;

    @BeforeEach
    public void setUp() {
        receiverDto = new UserDto();
        receiverDto.setId(1L);
        receiverDto.setUsername("Receiver");

        skillDto = new SkillDto();
        skillDto.setId(2L);
        skillDto.setTitle("FAANG");

        skillAcquiredEvent = new SkillAcquiredEvent();
        skillAcquiredEvent.setUserId(receiverDto.getId());
        skillAcquiredEvent.setSkillId(skillDto.getId());
    }

    @Test
    @DisplayName("Should build correct message")
    public void testBuildMessage_Success() {
        when(userServiceClient.getUser(receiverDto.getId())).thenReturn(receiverDto);
        when(userServiceClient.getUserSkills(receiverDto.getId())).thenReturn(List.of(skillDto));
        when(messageSource.getMessage(eq("skill.acquired"), any(Object[].class), eq(Locale.UK)))
                .thenReturn("Receiver acquired FAANG skill");

        String message = skillAcquiredMessageBuilder.buildMessage(skillAcquiredEvent, Locale.UK);

        verify(userServiceClient).getUser(receiverDto.getId());
        verify(userServiceClient).getUserSkills(receiverDto.getId());
        verify(messageSource).getMessage(eq("skill.acquired"), any(Object[].class), eq(Locale.UK));

        assertEquals("Receiver acquired FAANG skill", message);

        verify(messageSource).getMessage(eq("skill.acquired"),
                argThat(args -> args[0].equals(receiverDto.getUsername()) && args[1].equals("FAANG")),
                eq(Locale.UK));
    }

    @Test
    @DisplayName("Should return correct supported class")
    public void testGetSupportedClass_Success() {
        assertEquals(SkillAcquiredEvent.class, skillAcquiredMessageBuilder.getSupportedClass());
    }

    @Test
    @DisplayName("Should build message for a different locale")
    public void testBuildMessage_FrenchLocale() {
        when(userServiceClient.getUser(receiverDto.getId())).thenReturn(receiverDto);
        when(userServiceClient.getUserSkills(receiverDto.getId())).thenReturn(List.of(skillDto));
        when(messageSource.getMessage(eq("skill.acquired"), any(Object[].class), eq(Locale.FRANCE)))
                .thenReturn("Message en français");

        String message = skillAcquiredMessageBuilder.buildMessage(skillAcquiredEvent, Locale.FRANCE);

        assertEquals("Message en français", message);

        verify(messageSource).getMessage(eq("skill.acquired"),
                argThat(args -> args[0].equals(receiverDto.getUsername()) && args[1].equals("FAANG")),
                eq(Locale.FRANCE));
    }

    @Test
    @DisplayName("Should handle exception when fetching user data")
    public void testBuildMessage_UserServiceError() {
        when(userServiceClient.getUser(receiverDto.getId()))
                .thenThrow(new RuntimeException("User service error"));

        assertThrows(RuntimeException.class, () -> {
            skillAcquiredMessageBuilder.buildMessage(skillAcquiredEvent, Locale.UK);
        });

        verify(userServiceClient, never()).getUserSkills(receiverDto.getId());
        verify(messageSource, never()).getMessage(any(), any(), any());
    }
}
