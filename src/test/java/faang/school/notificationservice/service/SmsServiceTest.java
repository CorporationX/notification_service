package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SmsServiceTest {

    @Autowired
    SmsService smsService;

    private LogCaptor logCaptor;

    private  UserDto user;
    @Value("${test.phone-number}")
    private String testPhoneNumber;

    @BeforeEach
    public void setUp(){
        user = new UserDto(11, "test user", "some email",
                testPhoneNumber, UserDto.PreferredContact.PHONE);
    }

//    @Test
//    public void loadContext(){
//
//    }

//    @Test
//    public void send() {
//        logCaptor = LogCaptor.forClass(SmsService.class);
//
//        smsService.send(user, "Some message");
//
//        assertThat(logCaptor.getInfoLogs()).anyMatch(log -> log.contains("SMS sent successfully to"));
//    }


}