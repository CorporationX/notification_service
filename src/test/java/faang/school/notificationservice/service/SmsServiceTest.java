package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @Mock
    private VonageClient client;

    @InjectMocks
    private SmsService smsService;

    @Test
    void send() {

    }

    @Test
    void getPreferredContact() {
    }
}