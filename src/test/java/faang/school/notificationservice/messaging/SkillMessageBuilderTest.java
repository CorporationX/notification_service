package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillDto;
import faang.school.notificationservice.event.SkillAcquiredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillMessageBuilderTest {
    @InjectMocks
    private SkillMessageBuilder skillMessageBuilder;

    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    private SkillDto skillDto;
    private SkillAcquiredEvent acquiredEvent;

    private String message;

    @BeforeEach
    void setUp() {
        skillDto = new SkillDto();
        skillDto.setId(13);
        skillDto.setTitle("Java");

        acquiredEvent = new SkillAcquiredEvent();
        acquiredEvent.setSkillId(13);

        message = "message";
    }

    @Test
    void testGetInstance() {
        Class clas = skillMessageBuilder.getInstance();
        assertEquals(clas, SkillAcquiredEvent.class);
    }

    @Test
    void testBuildMessage() {
        when(userServiceClient.getSkill(13)).thenReturn(skillDto);
        when(messageSource.getMessage("skill.new", new Object[]{skillDto.getTitle()}, Locale.UK)).thenReturn(message);

        String resultMessage = skillMessageBuilder.buildMessage(acquiredEvent, Locale.UK);

        assertEquals(resultMessage,message);
    }
}