package faang.school.notificationservice.service.provider;

import faang.school.notificationservice.dto.SmsResponse;
import faang.school.notificationservice.exception.SmsIntegrationException;

public interface SmsProvider {
    SmsResponse sendSms(String from, String to, String message) throws SmsIntegrationException;
}
