package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.ContactDto;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StartCommandTest extends CommandTest {

    @InjectMocks
    private StartCommand startCommand;

    @Test
    void testExecute_userExists() {
        long chatId = 123L;
        String userName = "testUser";

        Mockito.when(telegramProfileService.existsByUserName(userName)).thenReturn(true);
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn("On the system message");

        SendMessage result = startCommand.execute(chatId, userName);

        assertEquals(String.valueOf(chatId), result.getChatId());
        assertEquals("On the system message", result.getText());
    }

    @Test
    void testExecute_userDoesNotExist_FeignExceptionThrown() {
        long chatId = 123L;
        String userName = "testUser";

        Mockito.when(telegramProfileService.existsByUserName(userName)).thenReturn(false);
        Mockito.when(userServiceClient.getContactByContent(userName)).thenThrow(FeignException.class);
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn("Not registered message");

        SendMessage result = startCommand.execute(chatId, userName);

        assertEquals(String.valueOf(chatId), result.getChatId());
        assertEquals("Not registered message", result.getText());
    }

    @Test
    void testExecute_userDoesNotExist() {
        long chatId = 123L;
        String userName = "testUser";
        ContactDto contactDto = new ContactDto();
        contactDto.setUserId(1L);

        Mockito.when(telegramProfileService.existsByUserName(userName)).thenReturn(false);
        Mockito.when(userServiceClient.getContactByContent(userName)).thenReturn(contactDto);
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn("Start message");

        SendMessage result = startCommand.execute(chatId, userName);

        assertEquals(String.valueOf(chatId), result.getChatId());
        assertEquals("Start message", result.getText());
    }
}
