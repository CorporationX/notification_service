package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.EmailEvent;
import faang.school.notificationservice.mapper.EmailEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EmailMessageBuilder implements MessageBuilder<EmailEvent, SimpleMailMessage> {

    private final EmailEventMapper emailEventMapper;
    private final MessageSource messageSource;

    @Value("${spring.mail.properties.from}")
    private String from;

    @Override
    public Class<EmailEvent> getInstance() {
        return EmailEvent.class;
    }

    @Override
    public SimpleMailMessage buildMessage(EmailEvent event, Locale locale) {
        String subject = messageSource.getMessage(event.getSubject() + ".subject", null, locale);
        String text = messageSource.getMessage(event.getSubject(), null, locale);

        SimpleMailMessage message = emailEventMapper.toSimpleMailMessage(event);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(text);

        return message;
    }
}
