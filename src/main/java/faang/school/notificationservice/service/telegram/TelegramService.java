package faang.school.notificationservice.service.telegram;

public interface TelegramService {

    void executeMessage(long chatId, String messageText);
}