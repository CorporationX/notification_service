package faang.school.notificationservice.service;

import com.vonage.client.sms.SmsClient;
import faang.school.notificationservice.dto.SendSmsRequestDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.SmsGatewayException;
import faang.school.notificationservice.error.SmsSendException;
import faang.school.notificationservice.mapper.NotificationMapper;
import faang.school.notificationservice.service.sms.SmsGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceImplTest {

    private static final String DEFAULT_PHONE = "+78888888888";
    private static final String EMPTY_PHONE_SPACES = "   ";
    private static final String MESSAGE_TEXT = "Hi!";
    private static final String DEFAULT_LOCALE = "en";
    private static final String DEFAULT_PREFERENCE = "PHONE";

    @Mock
    private SmsGateway smsGateway;

    @Mock
    SmsClient smsClient;

    @Spy
    NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);

    @InjectMocks
    SmsServiceImpl smsServiceImpl;

    @BeforeEach
    void setUp() {
        smsServiceImpl = new SmsServiceImpl(notificationMapper, smsGateway);
    }

    /**
     * Helper to build a UserDto for tests.
     * Only phone and preference really matter for SmsServiceImpl.
     */
    private static UserDto newUser(Long id, String phone) {
        return new UserDto(
                id,
                null,
                null,
                phone,
                null,
                DEFAULT_LOCALE,
                DEFAULT_PREFERENCE
        );
    }

    @Test
    void send_givenValidInput_whenSending_thenCallsSmsGatewayWithCorrectArguments() {
        UserDto user = newUser(1L, DEFAULT_PHONE);

        assertDoesNotThrow(() -> smsServiceImpl.send(user, MESSAGE_TEXT));
        verify(smsGateway, times(1)).send(DEFAULT_PHONE, MESSAGE_TEXT);
    }

    @Test
    void send_givenInvalidPhone_whenValidating_thenThrowsSmsSendException() {
        UserDto invalidUserNullPhone = newUser(1L, null);
        UserDto invalidUserBlankPhone = newUser(1L, "");

        assertThrows(SmsSendException.class, () -> smsServiceImpl.send(invalidUserNullPhone, MESSAGE_TEXT));
        assertThrows(SmsSendException.class, () -> smsServiceImpl.send(invalidUserBlankPhone, MESSAGE_TEXT));
        verifyNoInteractions(smsGateway);
    }

    @Test
    void send_givenEmptyPhone_whenValidating_thenThrowsAndDoesNotCallClient() {
        UserDto user = newUser(1L, EMPTY_PHONE_SPACES);

        assertThrows(SmsSendException.class, () -> smsServiceImpl.send(user, MESSAGE_TEXT));
        verifyNoInteractions(smsClient);
    }

    @Test
    void sendFromRequest_givenValidRequest_whenSending_thenMapsAndCallsSend() {
        SendSmsRequestDto request = new SendSmsRequestDto(1L, DEFAULT_PHONE, MESSAGE_TEXT);

        assertDoesNotThrow(() -> smsServiceImpl.sendFromRequest(request, MESSAGE_TEXT));

        verify(notificationMapper, times(1)).toUserDto(request);
        verify(smsGateway, times(1)).send(DEFAULT_PHONE, MESSAGE_TEXT);
    }

    @Test
    void sendFromRequest_givenRequestWithInvalidPhone_whenValidating_thenThrowsSmsSendException() {
        SendSmsRequestDto requestNullPhone = new SendSmsRequestDto(1L, null, MESSAGE_TEXT);
        SendSmsRequestDto requestBlankPhone = new SendSmsRequestDto(1L, "", MESSAGE_TEXT);

        assertThrows(SmsSendException.class,
                () -> smsServiceImpl.sendFromRequest(requestNullPhone, MESSAGE_TEXT));
        assertThrows(SmsSendException.class,
                () -> smsServiceImpl.sendFromRequest(requestBlankPhone, MESSAGE_TEXT));

        verify(smsGateway, times(0)).send(any(), any());
    }

    @Test
    void sendFromRequest_givenValidRequestButGatewayFails_whenSending_thenPropagatesException() {
        SendSmsRequestDto request = new SendSmsRequestDto(1L, DEFAULT_PHONE, MESSAGE_TEXT);
        doThrow(new SmsGatewayException("Gateway error")).when(smsGateway).send(any(), any());

        assertThrows(SmsGatewayException.class,
                () -> smsServiceImpl.sendFromRequest(request, MESSAGE_TEXT));

        verify(notificationMapper, times(1)).toUserDto(request);
        verify(smsGateway, times(1)).send(DEFAULT_PHONE, MESSAGE_TEXT);
    }
}
