package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillOfferDto;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillOfferedMessageBuilderTest {
    @InjectMocks
    private SkillOfferedMessageBuilder skillOfferedMessageBuilder;
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    private UserDto userDto;
    private SkillOfferDto skillOfferDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .username("TestUser")
                .build();

        skillOfferDto = SkillOfferDto.builder()
                .authorId(1L)
                .skill(2L)
                .build();
    }

    @Test
    void testGetInstance() {
        Class<?> instance = skillOfferedMessageBuilder.getInstance();
        assertEquals(SkillOfferDto.class, instance);
    }

    @Test
    void testBuildMessage() {
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).thenReturn("Test Message");

        String result = skillOfferedMessageBuilder.buildMessage(skillOfferDto, Locale.getDefault());

        assertEquals("Test Message", result);
        verify(userServiceClient, times(1)).getUser(1L);
        verify(messageSource, times(1)).getMessage(anyString(), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testBuildMessageWithException() {
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        Locale englishLocale = new Locale("en");

        when(messageSource.getMessage(eq("skill.offered.message"), any(Object[].class), eq(englishLocale)))
                .thenThrow(new RuntimeException("MessageSource exception"));

        assertThrows(RuntimeException.class, () -> {
            skillOfferedMessageBuilder.buildMessage(skillOfferDto, englishLocale);
        });

        verify(userServiceClient, times(1)).getUser(1L);
        verify(messageSource, times(1)).getMessage(eq("skill.offered.message"), any(Object[].class), eq(englishLocale));
    }
}