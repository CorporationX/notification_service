package faang.school.notificationservice.service.sms;

public interface SmsGateway {
    void send(String to, String text);
}