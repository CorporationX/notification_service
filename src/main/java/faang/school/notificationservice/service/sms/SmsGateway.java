package faang.school.notificationservice.service.sms;

public interface SmsGateway {
    void send(String from, String recipientPhoneNumber, String text);
}
