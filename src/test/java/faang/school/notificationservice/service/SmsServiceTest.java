package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.smsaero.SmsAero;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private SmsAero client;

    @InjectMocks
    private SmsService smsService;

    @Test
    void testSend() throws IOException, ParseException {
        String phone = "Test";
        UserDto user = UserDto.builder()
                .phone(phone)
                .build();
        String message = "Test";

        smsService.send(user, message);

        verify(client).SendSms(phone, message, null);
    }

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact expected = UserDto.PreferredContact.SMS;
        UserDto.PreferredContact result = smsService.getPreferredContact();
        assertEquals(expected, result);
    }
}