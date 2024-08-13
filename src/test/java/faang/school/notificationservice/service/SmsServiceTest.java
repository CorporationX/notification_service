package faang.school.notificationservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ExceptionMessages;
import faang.school.notificationservice.exception.notification.SmsSendingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private MessageCreator messageCreator;
    @InjectMocks
    private SmsService smsService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(smsService, "sender", "+15551234567");
        Twilio.init("testAccountSid", "testAuthToken");
    }

    @Test
    void testSendSuccess() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("+12345678901");
        String message = "Test message";

        try (MockedStatic<Message> mocked = mockStatic(Message.class)) {
            mocked.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString()))
                    .thenReturn(messageCreator);
            when(messageCreator.create()).thenReturn(mock(Message.class));

            assertDoesNotThrow(() -> smsService.send(user, message));

            verify(messageCreator, times(1)).create();
        }
    }

    @Test
    void testSendFailure() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("+12345678901");
        String message = "Test message";

        try (MockedStatic<Message> mocked = mockStatic(Message.class)) {
            mocked.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString()))
                    .thenReturn(messageCreator);
            when(messageCreator.create()).thenThrow(new RuntimeException("Test exception"));

            var exception = assertThrows(SmsSendingException.class, () -> smsService.send(user, message));

            assertEquals(String.format(ExceptionMessages.SMS_SENDING_FAILURE, user.getId()), exception.getMessage());
        }
    }

    @Test
    void testSendInvalidNumber() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPhone("invalid");
        String message = "Test message";

        var exception = assertThrows(SmsSendingException.class, () -> smsService.send(user, message));

        assertEquals(String.format(ExceptionMessages.INVALID_PHONE_NUMBER, user.getId()), exception.getMessage());
    }


}