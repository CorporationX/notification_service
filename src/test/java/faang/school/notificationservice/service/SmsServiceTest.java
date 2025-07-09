package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmsServiceTest {
    private SmsService smsService;

    @BeforeEach
    void setUp() {
        smsService = new SmsService();
    }

    @Test
    public void testPreferredContact() {
        UserDto.PreferredContact actualValue = smsService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.PHONE, actualValue);
    }
}