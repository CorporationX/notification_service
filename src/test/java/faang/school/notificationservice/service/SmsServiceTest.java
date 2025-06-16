package faang.school.notificationservice.service;

import faang.school.notificationservice.client.SmsClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @Mock
    private SmsClient smsClient;
    @Mock
    private UserServiceClient userServiceClient;
    @InjectMocks
    private SmsService smsService;

    @Test
    void testSendSmsWithUserPhoneNull() {
        UserDto userDto = new UserDto();
        long userId = 1L;
        String msg = "qwe";

        when(userServiceClient.getUser(userId)).thenReturn(userDto);

        assertThrows(IllegalArgumentException.class, () -> smsService.sendSms(msg, userId));
    }

    @Test
    void testSendSmsWithUserPhoneNotNull(){
        UserDto userDto = new UserDto();
        String userPhone = "79999999999";
        userDto.setPhone(userPhone);
        long userId = 1L;
        String msg = "qwe";
        String responseService = "result";

        when(userServiceClient.getUser(userId)).thenReturn(userDto);
        when(smsClient.sendingSms(any(), any())).thenReturn(responseService);

        String result = smsService.sendSms(msg, userId);

        assertEquals(responseService, result);
    }
}