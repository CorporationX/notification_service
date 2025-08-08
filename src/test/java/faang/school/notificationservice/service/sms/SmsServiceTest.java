package faang.school.notificationservice.service.sms;

import faang.school.notificationservice.config.properties.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsValidationException;
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

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private SmsGateway smsGateway;

    @InjectMocks
    private SmsService smsService;

    private final String sender = "CorpX";
    private final String recipientPhoneNumber = "+48123456789";
    private final String message = "Your verification code is 123456";

    @BeforeEach
    void setUp() {
        VonageProperties properties = new VonageProperties("BANK", "fake-secret", sender);
        smsService = new SmsService(smsGateway, properties);
    }

    @Test
    @DisplayName("Should send SMS successfully when input is valid")
    void shouldSendSmsSuccessfully() {
        UserDto user = new UserDto();
        user.setPhone(recipientPhoneNumber);

        smsService.send(user, message);

        verify(smsGateway).send(sender, recipientPhoneNumber, message);
    }

    @Test
    @DisplayName("Should throw exception when user is null")
    void shouldThrowWhenUserIsNull() {
        SmsValidationException exception = assertThrows(SmsValidationException.class,
                () -> smsService.send(null, message));

        assertEquals("User must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when phone number is null")
    void shouldThrowWhenPhoneIsNull() {
        UserDto user = new UserDto();
        user.setPhone(null);

        SmsValidationException exception = assertThrows(SmsValidationException.class,
                () -> smsService.send(user, message));

        assertEquals("Phone number is missing", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when phone number is blank")
    void shouldThrowWhenPhoneIsBlank() {
        UserDto user = new UserDto();
        user.setPhone("   ");

        SmsValidationException exception = assertThrows(SmsValidationException.class,
                () -> smsService.send(user, message));

        assertEquals("Phone number is missing", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when message is null")
    void shouldThrowWhenMessageIsNull() {
        UserDto user = new UserDto();
        user.setPhone(recipientPhoneNumber);

        SmsValidationException exception = assertThrows(SmsValidationException.class,
                () -> smsService.send(user, null));

        assertEquals("Message is missing", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when message is blank")
    void shouldThrowWhenMessageIsBlank() {
        UserDto user = new UserDto();
        user.setPhone(recipientPhoneNumber);

        SmsValidationException exception = assertThrows(SmsValidationException.class,
                () -> smsService.send(user, "  "));

        assertEquals("Message is missing", exception.getMessage());
    }

    @Test
    @DisplayName("Should return preferred contact type as PHONE")
    void shouldReturnPreferredContactAsPhone() {
        assertEquals(PHONE, smsService.getPreferredContact());
    }
}
