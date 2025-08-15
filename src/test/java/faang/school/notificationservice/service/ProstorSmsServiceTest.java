package faang.school.notificationservice.service;

import faang.school.notificationservice.client.ProstorSmsClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageSendException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
class ProstorSmsServiceTest {
    @InjectMocks
    private ProstorSmsService prostorSmsService;
    @Mock
    private ProstorSmsClient client;

    private static final String PHONE = "99991234567";
    private static final String MESSAGE = "Hello from titan-stream-11";

    @Test
    @DisplayName("Успешная отправка SMS по номеру")
    void positive_shouldSendSms() {
        String response = "accepted;6750378829";
        UserDto user = getPreparedUserDto();
        when(client.sendMessage(user.getPhone(), MESSAGE)).thenReturn(response);

        prostorSmsService.send(user, MESSAGE);

        verify(client, times(1)).sendMessage(user.getPhone(), MESSAGE);
    }

    @Test
    @DisplayName("Ошибка отправка SMS по номеру - статус ответа Error")
    void negative_whenStatusError_throwsError() {
        String response = "error;invalid mobile phone";
        UserDto user = getPreparedUserDto();
        when(client.sendMessage(user.getPhone(), MESSAGE)).thenReturn(response);

        assertThrows(MessageSendException.class,
                     () ->  prostorSmsService.send(user, MESSAGE));

        verify(client, times(1)).sendMessage(user.getPhone(), MESSAGE);
    }

    private UserDto getPreparedUserDto() {
        return UserDto.builder()
                .phone(PHONE)
                .build();
    }
}