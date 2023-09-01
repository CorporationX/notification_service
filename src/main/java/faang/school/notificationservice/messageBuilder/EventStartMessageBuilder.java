package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.event.EventDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class EventStartMessageBuilder implements MessageBuilder<EventDto, String> {
    @Override
    public String buildMessage(EventDto eventDto, String remainedTime) {
        Yaml yaml = new Yaml();
        ClassPathResource resource = new ClassPathResource("messages.yaml");

        try (InputStream inputStream = resource.getInputStream()) {
            Map<String, Map<String, String>> messagesConfig = yaml.load(inputStream);
            String eventMessagePattern = messagesConfig.get("event").get("start");
            String beautified = beautify(remainedTime);

            return String.format(eventMessagePattern, eventDto.getUserDto().getUsername(),
                    eventDto.getTitle(), beautified,
                    eventDto.getLocation(), eventDto.getDescription());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String beautify(String remainedTime) {
        long millis = Long.parseLong(remainedTime);

        if (TimeUnit.MINUTES.toMillis(1) > millis) {
            return "is starting";

        } else if (TimeUnit.HOURS.toMillis(1) > millis) {
            return String.format("will start in %s minutes", TimeUnit.MILLISECONDS.toMinutes(millis));

        } else if (TimeUnit.DAYS.toMillis(1) > millis) {
            return String.format("will start in %s hours", TimeUnit.MILLISECONDS.toHours(millis));

        } else if (TimeUnit.DAYS.toMillis(1) <= millis && TimeUnit.DAYS.toMillis(31) >= millis) {
            return String.format("will start in %s days", TimeUnit.MILLISECONDS.toDays(millis));

        } else {
            return "will start very soon";
        }
    }
}
