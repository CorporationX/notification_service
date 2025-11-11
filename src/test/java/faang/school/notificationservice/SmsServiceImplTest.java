package faang.school.notificationservice;

import com.vonage.client.sms.SmsClient;
import faang.school.notificationservice.dto.SendSmsRequestDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.SmsGatewayException;
import faang.school.notificationservice.error.SmsSendException;
import faang.school.notificationservice.mapper.NotificationMapper;
import faang.school.notificationservice.service.SmsServiceImpl;
import faang.school.notificationservice.sms.SmsGateway;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SmsServiceImplTest {

    private static final String DEFAULT_PHONE = "+78888888888";
    private static final String EMPTY_PHONE_SPACES = "   ";
    private static final String MESSAGE_TEXT = "Hi!";

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

    @Test
    void send_givenValidInput_whenSending_thenCallsSmsGatewayWithCorrectArguments() {
        UserDto user = new UserDto(1L, DEFAULT_PHONE, UserDto.PreferredContact.PHONE);

        assertDoesNotThrow(() -> smsServiceImpl.send(user, MESSAGE_TEXT));
        verify(smsGateway, times(1)).send(DEFAULT_PHONE, MESSAGE_TEXT);
    }

    @Test
    void send_givenInvalidPhone_whenValidating_thenThrowsSmsSendException() {
        UserDto invalidUserNullPhone = new UserDto(1L, null, UserDto.PreferredContact.PHONE);
        UserDto invalidUserBlankPhone = new UserDto(1L, "", UserDto.PreferredContact.PHONE);

        assertThrows(SmsSendException.class, () -> smsServiceImpl.send(invalidUserNullPhone, MESSAGE_TEXT));
        assertThrows(SmsSendException.class, () -> smsServiceImpl.send(invalidUserBlankPhone, MESSAGE_TEXT));
        verifyNoInteractions(smsGateway);
    }

    @Test
    void send_givenEmptyPhone_whenValidating_thenThrowsAndDoesNotCallClient() {
        UserDto user = new UserDto(1L, EMPTY_PHONE_SPACES, UserDto.PreferredContact.PHONE);
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
