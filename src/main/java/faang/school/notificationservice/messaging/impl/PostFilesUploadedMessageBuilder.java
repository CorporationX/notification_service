package faang.school.notificationservice.messaging.impl;

import faang.school.notificationservice.event.FilesUploadedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostFilesUploadedMessageBuilder implements MessageBuilder<FilesUploadedEvent> {

    private static final String MESSAGE_KEY = "files.uploaded";

    private final MessageSource messageSource;

    @Override
    public Class<FilesUploadedEvent> supportEventType() {
        return FilesUploadedEvent.class;
    }

    @Override
    public String buildMessage(FilesUploadedEvent event, Locale locale) {
        Map<String, String> keyToFileName = event.getKeyToFileName();
        String message = keyToFileName.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", \n\n"));
        return messageSource.getMessage(MESSAGE_KEY, new Object[] {message}, locale);
    }
}
