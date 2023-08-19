package faang.school.notificationservice.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServiceList;
    private final List<MessageBuilder> messageBuilders;


//getMessage — получает в качестве параметров тип эвента (Class<?>),
// локаль пользователя, а также набор дополнительных аргументов,
// которые могут использоваться в MessageBuilder для формирования сообщения из шаблона.
// Выбирает из набора всех MessageBuilder подходящий для переданного типа сообщения
// и возвращает сообщение, которое строит на основе прочих параметров выбранный MessageBuilder.
//   public String getMessage(Class<?> eventType, Locale userLocale, Map<String, Object> args) {
//       messageBuilders.stream()
//               .filter(messageBuilder -> messageBuilder.isApplicable(eventType))
//               .toList();
//       return getMessage(eventType, userLocale, args);
//   }


//sendNotification — получает id пользователя и текст сообщения.
// Используя UserServiceClient получает всю информацию о пользователе, выбирает из набора всех бино
// в NotificationService тот, который подходит под предпочитаемый пользователем способ коммуникации
// (содержится в UserDto), а затем отправляет нотификацию, используя выбранный NotificationService.
//    public void sendNotification(Long userId, String message) {
//        UserDto user = userServiceClient.getUser(userId);
//        notificationServiceList.stream()
//                .filter(notificationService -> notificationService.supports(user.getPreferredNotificationType()))
//    }



    //Критерии приема
    //
    //AbstractEventListener полагается только на интерфейсы.
    //
    //Корректно выбирает MessageBuilder и NotificationService бины под конкретный эвент и предпочитаемый пользователем способ связи.
    //
    //Написаны unit-тесты.

//    public String getMessage(Class<?> eventType, Locale userLocale, Map<String, Object> args) {
//        Optional<MessageBuilder> selectedMessageBuilder = messageBuilders.stream()
//                .filter(builder -> builder.supports(eventType))
//                .findFirst();
//
//        if (selectedMessageBuilder.isPresent()) {
//            MessageBuilder messageBuilder = selectedMessageBuilder.get();
//            return messageBuilder.buildMessage(eventType, userLocale, args);
//        } else {
//            throw new UnsupportedOperationException("No suitable MessageBuilder found for event type: " + eventType);
//        }
//    }
//
//    public void sendNotification(Long userId, String message) {
//        UserDto user = userServiceClient.getUserInfo(userId);
//        NotificationType preferredNotificationType = user.getPreferredNotificationType();
//
//        Optional<NotificationService> selectedNotificationService = notificationServices.stream()
//                .filter(service -> service.supports(preferredNotificationType))
//                .findFirst();
//
//        if (selectedNotificationService.isPresent()) {
//            NotificationService notificationService = selectedNotificationService.get();
//            notificationService.sendNotification(user, message);
//        } else {
//            throw new UnsupportedOperationException("No suitable NotificationService found for preferred notification type: " + preferredNotificationType);
//        }
//    }
}
