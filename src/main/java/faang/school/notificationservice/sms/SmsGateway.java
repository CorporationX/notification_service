package faang.school.notificationservice.sms;

public interface SmsGateway {
    void send(String to, String text);
}