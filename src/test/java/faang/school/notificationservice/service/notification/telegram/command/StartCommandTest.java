package faang.school.notificationservice.service.notification.telegram.command;

import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.entity.TelegramProfiles;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StartCommandTest extends CommandTest {
    @Spy
    @InjectMocks
    protected StartCommand startCommand;

//    @BeforeEach
//    void setUp() {
//        startCommand = new StartCommand(messageSource, telegramProfilesService, userServiceClient);
//    }

    @Test
    void testExecuteIsRegistered() {
        Mockito.when(telegramProfilesService.existsByChatId(1L)).thenReturn(true);
        Mockito.when(messageSource.getMessage("telegram.start.not_registered", null, defaultLocale))
                .thenReturn("test");

        SendMessage actual = startCommand.execute(1L, "test");

        assertEquals("test", actual.getText());
        assertEquals("1", actual.getChatId());
    }

    @Test
    void testExecuteThrowsFeignExceptions() {
        String nick = "setooooon";
        Mockito.when(telegramProfilesService.existsByChatId(1L)).thenReturn(false);
        Mockito.when(userServiceClient.getContact(nick)).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> startCommand.execute(1L, nick));
    }

    @Test
    void testExecuteIsNotRegistered() {
        String nick = "setooooon";
        Mockito.when(telegramProfilesService.existsByChatId(1L)).thenReturn(false);
        Mockito.when(userServiceClient.getContact(nick)).thenReturn(ContactDto.builder().userId(1L).build());
        Mockito.when(messageSource.getMessage("telegram.start.registered", new Object[]{nick}, defaultLocale))
                .thenReturn("test");
        TelegramProfiles telegramProfiles = TelegramProfiles.builder().nickname(nick).userId(1L).chatId(1L).build();

        SendMessage actual = startCommand.execute(1L, nick);

        Mockito.verify(telegramProfilesService, Mockito.times(1))
                .save(telegramProfiles);
        assertEquals("test", actual.getText());
        assertEquals("1", actual.getChatId());
    }
}