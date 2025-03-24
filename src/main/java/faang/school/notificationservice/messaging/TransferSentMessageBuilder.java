package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.TransferSentEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TransferSentMessageBuilder implements MessageBuilder<TransferSentEvent> {

    @Override
    public Class<?> getInstance() {
        return TransferSentEvent.class;
    }

    @Override
    public String buildMessage(TransferSentEvent event, Locale locale) {
        return "You have completed the transfer of funds";
    }
}
