package faang.school.notificationservice.config.tg;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:tg_sample.properties")
public class TgSampleProperties {

    @Value("${message.ask-id}")
    private String askId;

    @Value("${message.invalid-id}")
    private String incorrectId;

    @Value("${message.already-registered}")
    private String alreadyRegistered;

    @Value("${message.stop}")
    private String stop;

    @Value("${message.stop.answer}")
    private String answerStop;

    @Value("${message.unknown-command}")
    private String unknownCommand;
}
