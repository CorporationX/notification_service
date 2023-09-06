package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static faang.school.notificationservice.commonMessage.ErrorMessageForEmailService.EMAIL_ADDRESS_IS_NULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {
    @Mock
    private MailSender mailSender;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    private SimpleMailMessage mailMessage;

    @BeforeEach
    void setUp() {
        mailMessage = getMailMessage();
    }

    @Test
    void testSend_WhenInputDataIsValid() {
        UserDto userDto = getUserDto();
        String message = ValuesForTest.MESSAGE.getValue();

        emailNotificationService.send(userDto, message);

        Mockito.verify(mailSender, Mockito.times(1)).send(mailMessage);
    }

    @Test
    void testSend_WhenInputDtoWithoutEmail_ShouldThrowException() {
        UserDto dtoWithoutEmail = UserDto.builder().build();
        String message = ValuesForTest.MESSAGE.getValue();

        Exception exception = assertThrows(DataValidationException.class,
                () -> emailNotificationService.send(dtoWithoutEmail, message));

        assertEquals(EMAIL_ADDRESS_IS_NULL, exception.getMessage());
    }

    @Test
    void testGetPreferredContact() {
        PreferredContact expectedResult = PreferredContact.EMAIL;

        PreferredContact result = emailNotificationService.getPreferredContact();

        assertEquals(expectedResult, result);
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .email(ValuesForTest.VALID_EMAIL.getValue())
                .build();
    }

    private SimpleMailMessage getMailMessage() {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(ValuesForTest.VALID_EMAIL.getValue());
        mail.setSubject(ValuesForTest.MESSAGE.getValue());
        mail.setText(ValuesForTest.MESSAGE.getValue());
        return mail;
    }
}

enum ValuesForTest {
    VALID_EMAIL("username@domain.com"),
    MESSAGE("test message"),
    INVALID_EMAIL_1("test#mail.ru"),
    INVALID_EMAIL_2("testmail.ru"),
    INVALID_EMAIL_3("@mail.ru");

    private final String value;

    ValuesForTest(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}