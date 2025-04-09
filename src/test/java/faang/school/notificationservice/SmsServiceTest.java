package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SuppressWarnings("checkstyle:OuterTypeFilename")
@SpringBootTest
@ActiveProfiles("test")
class SmsServiceIntegrationTest {

    @Autowired
    private SmsService smsService;

    @Test
    void testRealSmsSending() {
        UserDto testUser = UserDto.builder()
                .phone("тут был мой номер телефона")
                .build();

        smsService.send(testUser, "Hello this is test");
    }
}

