package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.config.properties.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidMessageException;
import faang.school.notificationservice.validation.UserSmsValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.PHONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private SmsGateway smsGateway;

    @Mock
    private UserSmsValidator userSmsValidator;

    private final String sender = "CorpX";
    private final String recipientPhoneNumber = "+48123456789";

    @InjectMocks
    private SmsService smsService;

    @BeforeEach
    void setUp() {
        VonageProperties properties = new VonageProperties("BANK", "fake-secret", sender);
        smsService = new SmsService(smsGateway, properties, userSmsValidator);
    }

    @Test
    @DisplayName("Should send SMS successfully when input is valid")
    void shouldSendSmsSuccessfully() {
        UserDto user = new UserDto();
        user.setPhone(recipientPhoneNumber);

        String message = "Your verification code is 123456";
        smsService.send(user, message);

        verify(userSmsValidator).validateForSms(user);
        verify(smsGateway).send(sender, recipientPhoneNumber, message);
    }

    @Test
    @DisplayName("Should throw exception when message is null")
    void shouldThrowWhenMessageIsNull() {
        UserDto user = new UserDto();
        user.setPhone(recipientPhoneNumber);

        assertThrows(InvalidMessageException.class,
                () -> smsService.send(user, null));

        verify(userSmsValidator).validateForSms(user);
        verifyNoInteractions(smsGateway);
    }

    @Test
    @DisplayName("Should throw exception when message is blank")
    void shouldThrowWhenMessageIsBlank() {
        UserDto user = new UserDto();
        user.setPhone(recipientPhoneNumber);

        assertThrows(InvalidMessageException.class,
                () -> smsService.send(user, "  "));

        verify(userSmsValidator).validateForSms(user);
        verifyNoInteractions(smsGateway);
    }

    @Test
    @DisplayName("Should return preferred contact type as PHONE")
    void shouldReturnPreferredContactAsPhone() {
        assertEquals(PHONE, smsService.getPreferredContact());
    }
}
