package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.TransferEvent;

import java.util.Locale;

public class TransferMessageBuilder implements MessageBuilder<TransferEvent> {

    @Override
    public Class<TransferEvent> getInstance() {
        return TransferEvent.class;
    }

    @Override
    public String buildMessage(TransferEvent event, Locale locale) {
        return "You have completed the transfer of funds successfully";
    }
}