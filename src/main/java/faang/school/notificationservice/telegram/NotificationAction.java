package faang.school.notificationservice.telegram;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface NotificationAction {
    boolean isAcceptable(NotificationActionType type);

    String processAndReturnMessage(Message message);

    InlineKeyboardMarkup getMarkup();
}
