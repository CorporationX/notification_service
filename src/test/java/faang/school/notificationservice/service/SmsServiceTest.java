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

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @InjectMocks
    private SmsService smsService;

    @Mock
    private SmsAero smsAeroClient;

    @Test
    public void testSendSms() throws IOException, ParseException {
        UserDto user = new UserDto();
        user.setPhone("123456789");

        String text = "text";
        smsService.send(user, text);

        verify(smsAeroClient).SendSms(user.getPhone(), text);
    }
}
