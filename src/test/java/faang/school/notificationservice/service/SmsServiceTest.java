package faang.school.notificationservice.service;

import faang.school.notificationservice.client.SmsClient;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @Mock
    private SmsClient smsClient;
    @InjectMocks
    private SmsService smsService;

    @Test
    void testSendSmsWithUserPhoneNull() {
        UserDto userDto = new UserDto();
        String msg = "qwe";

        assertThrows(IllegalArgumentException.class, () -> smsService.send(userDto, msg));
    }

    @Test
    void testSendSmsWithUserPhoneNotNull() {
        UserDto userDto = new UserDto();

        String userPhone = "79999999999";
        userDto.setPhone(userPhone);

        String resultPhone = "+" + userPhone;
        String msg = "qwe";

        smsService.send(userDto, msg);

        verify(smsClient, times(1)).sendingSms(resultPhone, msg);
    }
}