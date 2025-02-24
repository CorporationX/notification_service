package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserServiceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    private final SmsService smsService = new SmsService();
    UserServiceDto userServiceDto = UserServiceDto.builder().build();
    @Test
    void testSend() {
        smsService.send(userServiceDto, "Test message");
    }

    @Test
    void testGetPreferredContact() {
        UserServiceDto.PreferredContact preferredContact = smsService.getPreferredContact();

        assertEquals(UserServiceDto.PreferredContact.SMS, preferredContact);
    }
}