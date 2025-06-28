package faang.school.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RegisterTelegramDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ErrorResponse;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.service.telegram.TelegramBot;
import faang.school.notificationservice.service.telegram.TelegramMessages;
import faang.school.notificationservice.util.Utils;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {
    public static final Long CHAT_ID = 1L;
    public static final String TEXT = "text";
    public static final String PHONE = "1234567890";

    @Mock
    private TelegramBotsApi telegramBotsApi;
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Spy
    private Utils utils;
    @Spy
    private MessageSource messageSource;
    private final Locale locale = new Locale("ru");

    private TelegramService telegramService;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        TelegramMessages messages = new TelegramMessages(messageSource, locale);
        telegramService = new TelegramService(
            telegramBotsApi, telegramBot, userServiceClient, messages, objectMapper, utils
        );
    }

    @Test
    public void testPreferredValue() {
        UserDto.PreferredContact preferredContact = telegramService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.TELEGRAM, preferredContact);
    }

    @Test
    public void testSetUpService() throws TelegramApiException {
        when(telegramBotsApi.registerBot(telegramBot)).thenReturn(any(BotSession.class));

        telegramService.setUp();

        verify(telegramBotsApi).registerBot(any(TelegramBot.class));
        verify(telegramBot).setService(any(TelegramService.class));
    }

    @Test
    public void testSuccessSendMessage() {
        UserDto userDto = getMockUser(CHAT_ID);

        telegramService.send(userDto, TEXT);

        verify(telegramBot).sendMessage(anyLong(), anyString());
    }

    @Test
    public void testFailSendMessage() {
        UserDto userDto = getMockUser(null);

        telegramService.send(userDto, TEXT);

        verify(telegramBot, times(0)).sendMessage(anyLong(), anyString());
    }

    @Test
    public void testSuccessSendMessage_TextIsEmpty() {
        UserDto userDto = getMockUser(CHAT_ID);

        telegramService.send(userDto, "");

        verify(telegramBot, times(0)).sendMessage(anyLong(), anyString());
    }

    @Test
    public void testSuccessSendMessage_TextIsNull() {
        UserDto userDto = getMockUser(CHAT_ID);

        telegramService.send(userDto, null);

        verify(telegramBot, times(0)).sendMessage(anyLong(), anyString());
    }

    @Test
    public void testRegisterUser() {
        doNothing().when(userServiceClient).registerTelegramChatId(any(RegisterTelegramDto.class));
        doNothing().when(telegramBot).removeKeyboardAfterReceivingContact(eq(CHAT_ID), anyString());

        telegramService.registerUser(CHAT_ID, PHONE);

        verify(userServiceClient).registerTelegramChatId(any(RegisterTelegramDto.class));
        verify(telegramBot).removeKeyboardAfterReceivingContact(eq(CHAT_ID), anyString());
    }


    @Test
    public void testWhenUserServiceClientIsUnavailable() {
        RegisterTelegramDto dto = getMockTelegramDto();
        FeignException feignException = mock(FeignException.class);

        when(feignException.status()).thenReturn(-1);
        doThrow(feignException).when(userServiceClient).registerTelegramChatId(dto);
        UserNotFoundException resultException = assertThrows(UserNotFoundException.class,
            () -> telegramService.registerUser(CHAT_ID, PHONE));

        String expectedError = utils.format(TelegramService.USER_BY_PHONE_NOT_FOUND, PHONE);
        assertEquals(expectedError, resultException.getMessage());
    }

    @Test
    public void testFailRegisterUserExceptionMessageIsEmpty() {
        RegisterTelegramDto dto = getMockTelegramDto();
        FeignException feignException = mock(FeignException.class);

        when(feignException.status()).thenReturn(404);
        when(feignException.contentUTF8()).thenReturn(null);
        doThrow(feignException).when(userServiceClient).registerTelegramChatId(dto);
        UserNotFoundException resultException = assertThrows(UserNotFoundException.class,
            () -> telegramService.registerUser(CHAT_ID, PHONE));

        String expectedError = utils.format(TelegramService.USER_BY_PHONE_NOT_FOUND, PHONE);
        assertEquals(expectedError, resultException.getMessage());
    }

    @Test
    public void testFailRegisterUserButUserIsMissing() throws JsonProcessingException {
        RegisterTelegramDto dto = getMockTelegramDto();
        FeignException feignException = mock(FeignException.class);
        final String expectedError = utils.format(TelegramService.USER_BY_PHONE_NOT_FOUND, PHONE);
        ErrorResponse errorResponse = new ErrorResponse(expectedError);

        when(feignException.status()).thenReturn(404);
        when(feignException.contentUTF8())
            .thenReturn(objectMapper.writeValueAsString(errorResponse));
        doThrow(feignException).when(userServiceClient).registerTelegramChatId(dto);
        UserNotFoundException resultException = assertThrows(UserNotFoundException.class,
            () -> telegramService.registerUser(CHAT_ID, PHONE));

        assertEquals(expectedError, resultException.getMessage());
    }

    @Test
    public void testFailRegisterUserWhenMessageIsNotJson() {
        RegisterTelegramDto dto = getMockTelegramDto();
        FeignException feignException = mock(FeignException.class);
        final String expectedError = utils.format(TelegramService.USER_BY_PHONE_NOT_FOUND, PHONE);

        when(feignException.status()).thenReturn(404);
        when(feignException.contentUTF8()).thenReturn("simple error message");
        doThrow(feignException).when(userServiceClient).registerTelegramChatId(dto);
        UserNotFoundException resultException = assertThrows(UserNotFoundException.class,
            () -> telegramService.registerUser(CHAT_ID, PHONE));

        assertEquals(expectedError, resultException.getMessage());
    }

    @Test
    public void testSuccessUnregister() {
        doNothing().when(userServiceClient).unregisterTelegramChatId(CHAT_ID);
        doNothing().when(telegramBot).removeKeyboardAfterReceivingContact(eq(CHAT_ID), anyString());

        telegramService.unregisterUser(CHAT_ID);

        verify(userServiceClient).unregisterTelegramChatId(CHAT_ID);
        verify(telegramBot).removeKeyboardAfterReceivingContact(eq(CHAT_ID), anyString());
    }

    @Test
    public void testFailUnregisterChatIsMissing() throws JsonProcessingException {
        final String expectedError = utils.format(TelegramService.TELEGRAM_NOT_FOUND, CHAT_ID);
        ErrorResponse errorResponse = new ErrorResponse(expectedError);
        FeignException feignException = mock(FeignException.class);

        when(feignException.status()).thenReturn(404);
        when(feignException.contentUTF8())
            .thenReturn(objectMapper.writeValueAsString(errorResponse));
        doThrow(feignException).when(userServiceClient).unregisterTelegramChatId(CHAT_ID);
        UserNotFoundException resultException = assertThrows(UserNotFoundException.class,
            () -> telegramService.unregisterUser(CHAT_ID));

        assertEquals(expectedError, resultException.getMessage());
    }

    private RegisterTelegramDto getMockTelegramDto() {
        return RegisterTelegramDto.builder()
            .chatId(CHAT_ID)
            .phone(PHONE)
            .build();
    }

    private UserDto getMockUser(Long chatId) {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("user name");
        userDto.setChatId(chatId);
        userDto.setPreference(UserDto.PreferredContact.TELEGRAM);
        return userDto;
    }
}