package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService();
    }

    @Test
    public void testPreferredContact() {
        UserDto.PreferredContact actualValue = emailService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.EMAIL, actualValue);
    }
}