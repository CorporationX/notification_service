package faang.school.notificationservice.util;

public class MessageUtils {

    public static String truncateContent(String content, int maxLength) {
        if (content.length() > maxLength) {
            return content.substring(0, maxLength - 3) + "...";
        }
        return content;
    }
}
