package faang.school.notificationservice.service.notification.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ExternalServiceException;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {
    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        TextMessage smsMessage = new TextMessage("Vonage APIs",
                user.getPhone(),
                message
        );

        SmsSubmissionResponse response = vonageClient.getSmsClient()
                .submitMessage(smsMessage);

        checkMessagesOnErrors(response.getMessages());
    }

    private void checkMessagesOnErrors(List<SmsSubmissionResponseMessage> responseMessages) {
        List<String> errors = new ArrayList<>(responseMessages.size());
        for (var responseMessage : responseMessages) {
            if (responseMessage.getStatus() != MessageStatus.OK) {
                errors.add(responseMessage.getErrorText());
            }
        }
        if (!errors.isEmpty()) {
            log.error("Failed to send SMS, errors: {}", errors);
            throw new ExternalServiceException(
                    "Failed to send SMS: "
                            + String.join(",", errors)
            );
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
