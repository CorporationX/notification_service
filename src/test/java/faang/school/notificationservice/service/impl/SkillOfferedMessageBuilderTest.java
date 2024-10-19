package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.SkillCandidateDto;
import faang.school.notificationservice.model.dto.SkillDto;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.SkillOfferedEvent;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillOfferedMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SkillOfferedMessageBuilder skillOfferedMessageBuilder;

    private SkillOfferedEvent skillOfferedEvent;
    private UserDto receiverDto;
    private UserDto senderDto;
    private SkillCandidateDto candidate;

    @BeforeEach
    public void setUp() {
        receiverDto = new UserDto();
        receiverDto.setId(1L);
        receiverDto.setUsername("Receiver");

        senderDto = new UserDto();
        senderDto.setId(1L);
        senderDto.setUsername("Sender");

        SkillDto skillDto = new SkillDto();
        skillDto.setId(3L);
        skillDto.setTitle("FAANG");

        candidate = new SkillCandidateDto();
        candidate.setSkill(skillDto);
        candidate.setOffersAmount(1);

        skillOfferedEvent = new SkillOfferedEvent();
        skillOfferedEvent.setReceiverId(receiverDto.getId());
        skillOfferedEvent.setSenderId(senderDto.getId());
        skillOfferedEvent.setSkillId(skillDto.getId());
    }

    @Test
    @DisplayName("Should build correct message")
    public void testBuildMessage_Success() {
        when(userServiceClient.getUser(receiverDto.getId())).thenReturn(receiverDto);
        when(userServiceClient.getUser(senderDto.getId())).thenReturn(senderDto);
        when(userServiceClient.getOfferedSkills(receiverDto.getId())).thenReturn(List.of(candidate));
        when(messageSource.getMessage(eq("skill.offered"), any(Object[].class), eq(Locale.UK)))
                .thenReturn("Sender offered FAANG skill to Receiver");

        String message = skillOfferedMessageBuilder.buildMessage(skillOfferedEvent, Locale.UK);

        verify(userServiceClient, times(2)).getUser(receiverDto.getId());
        verify(userServiceClient).getOfferedSkills(receiverDto.getId());
        verify(messageSource).getMessage(eq("skill.offered"), any(Object[].class), eq(Locale.UK));

        assertEquals("Sender offered FAANG skill to Receiver", message);
    }

    @Test
    @DisplayName("Should return correct supported class")
    public void testGetSupportedClass_Success() {
        assertEquals(SkillOfferedEvent.class, skillOfferedMessageBuilder.getSupportedClass());
    }

    @Test
    @DisplayName("Should handle exception when fetching user data")
    public void testBuildMessage_UserServiceError() {
        when(userServiceClient.getUser(receiverDto.getId()))
                .thenThrow(new RuntimeException("User service error"));

        assertThrows(RuntimeException.class, () -> {
            skillOfferedMessageBuilder.buildMessage(skillOfferedEvent, Locale.UK);
        });

        verify(userServiceClient, never()).getUserSkills(receiverDto.getId());
        verify(messageSource, never()).getMessage(any(), any(), any());
    }
}
