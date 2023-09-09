//package faang.school.notificationservice.messageBuilder;
//
//import faang.school.notificationservice.dto.notification.NotificationData;
//import faang.school.notificationservice.dto.redis.LikeEventDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Component;
//
//import java.util.Locale;
//
//@Component
//@RequiredArgsConstructor
//public class LikeMessageBuilder implements MessageBuilder<LikeEventDto, String> {
//    private final MessageSource messageSource;
//
//    public String getPredefinedMessage(NotificationData data, Locale locale) {
//        return messageSource.getMessage("like.new", new Object[]{data.getFrom()}, locale);
//    }
//
//    @Override
//    public String buildMessage(NotificationData data, Locale locale) {
//        return getPredefinedMessage(data, locale);
//    }
//
//    @Override
//    public boolean supportsEventType(LikeEventDto eventType) {
//        return true;
//    }
//}
