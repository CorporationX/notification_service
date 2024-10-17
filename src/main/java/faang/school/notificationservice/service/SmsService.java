package faang.school.notificationservice.service;

public interface SmsService {
    void sendSms(String to, String messageText);
}
