package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.event.EventStartEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class EventStartMessageBuilder implements MessageBuilder<EventStartEvent> {

    @Override
    public Class<?> getInstance() {
        return EventStartEvent.class;
    }

    @Override
    public String buildMessage(EventStartEvent event, Locale locale) {
        // Персонализируем сообщение для каждого участника
        String eventId = event.getEventId();
        List<String> participantIds = event.getParticipantIds();

        // Генерация текста уведомления
        StringBuilder message = new StringBuilder("Событие ");
        message.append(eventId).append(" начинается прямо сейчас!");

        // Здесь можно добавить локализацию для разных языков, если нужно
        if (locale != null && locale.getLanguage().equals("ru")) {
            message.append(" Присоединяйтесь к нам!");
        }

        return message.toString();
    }
}
