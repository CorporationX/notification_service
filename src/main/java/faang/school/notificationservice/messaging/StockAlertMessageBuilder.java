package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.StockAlertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Slf4j
@Component
@RequiredArgsConstructor
public class StockAlertMessageBuilder {

    @Value("${spring.messages.property-stock-alert}")
    private final String stockAlert;
    private final MessageSource messageSource;

    public String buildMessage(StockAlertDto event, Locale locale) {
        return messageSource.getMessage(stockAlert,  new Object[]{event.username()}, locale);
    }
}
