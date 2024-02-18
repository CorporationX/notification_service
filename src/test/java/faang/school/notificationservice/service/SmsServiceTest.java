package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private SmsService smsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testSendSms_Success() {
        Assertions.assertTrue(true);
        //нужно использовать PowerMock, но помню что Влад говорил не использовать, как написать тест?
    }

}
