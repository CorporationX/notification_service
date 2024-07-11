package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.NotificationData;
import faang.school.notificationservice.dto.TariffRateChangeEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class TariffRateChangeMessageBuilder implements MessageBuilder<TariffRateChangeEvent>{
    private MessageSource messageSource;
    @Override
    public Class<?> getInstance() {
        return TariffRateChangeEvent.class;
    }

    @Override
    public String buildMessage(TariffRateChangeEvent event, Locale locale, NotificationData notificationData) {
        return messageSource.getMessage("tariff-rate-change.new",
                new Object[]{notificationData.getAccountOwner()}, locale);
    }
}