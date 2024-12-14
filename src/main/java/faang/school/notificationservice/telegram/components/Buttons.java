package faang.school.notificationservice.telegram.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");
    private static final InlineKeyboardButton LOG_BUTTON = new InlineKeyboardButton("Log in");

    public static InlineKeyboardMarkup secondInlineMarkup() {
        LOG_BUTTON.setCallbackData("/log_in");
        HELP_BUTTON.setCallbackData("/help");

        List<InlineKeyboardButton> rowInline = List.of(LOG_BUTTON, HELP_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
