package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.SmsResponse;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsIntegrationException;
import faang.school.notificationservice.service.provider.SmsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    private static final String VALID_PHONE = "+1234567890";
    private static final String MESSAGE = "Test message";

    @Mock
    private SmsProvider smsProvider;

    @InjectMocks
    private SmsService smsService;

    @Test
    void send_ValidRequest_Success() {
        UserDto user = createValidUser();
        when(smsProvider.sendSms(anyString(), anyString(), anyString()))
                .thenReturn(createSuccessResponse());

        assertDoesNotThrow(() -> smsService.send(user, MESSAGE));

        verify(smsProvider).sendSms(
                eq(SmsService.NAME_FROM),
                eq(VALID_PHONE),
                eq(MESSAGE)
        );
    }

    @Test
    void send_ProviderError_ThrowsException() {
        UserDto user = createValidUser();
        when(smsProvider.sendSms(anyString(), anyString(), anyString()))
                .thenReturn(createErrorResponse());

        assertThrows(SmsIntegrationException.class,
                () -> smsService.send(user, MESSAGE));
    }

    @Test
    void sendGroup_AllSuccess_NoExceptionsThrown() {
        List<UserDto> users = List.of(createValidUser(), createValidUser());
        when(smsProvider.sendSms(anyString(), anyString(), anyString()))
                .thenReturn(createSuccessResponse());

        assertDoesNotThrow(() -> smsService.sendGroup(users, MESSAGE));

        verify(smsProvider, times(2)).sendSms(
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void getPreferredContactShouldReturnPhone() {
        assertThat(smsService.getPreferredContact())
                .isEqualTo(UserDto.PreferredContact.PHONE);
    }

    private UserDto createValidUser() {
        return UserDto.builder()
                .phone(VALID_PHONE)
                .preference(UserDto.PreferredContact.PHONE)
                .build();
    }

    private SmsResponse createSuccessResponse() {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(true);
        smsResponse.setProviderId("123");
        return smsResponse;
    }

    private SmsResponse createErrorResponse() {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(false);
        smsResponse.setErrorMessage("Invalid number");
        return smsResponse;
    }
}
