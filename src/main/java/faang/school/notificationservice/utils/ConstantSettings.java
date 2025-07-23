package faang.school.notificationservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantSettings {
    public static final String START_STRING = "/start";
    public static final int START_SYMBOLS_AMOUNT = 6;
    public static final String NO_USER_ID = "There is no user id";
    public static final String TELEGRAM_CONNECTED = "Telegram is connected to your account";
    public static final String INCORRECT_USER_ID = "Wrong format of user id. Number is expected";

    public static final String NO_CHAT_ID_ERROR = "There is no info about chatId for user with id %s";
}
