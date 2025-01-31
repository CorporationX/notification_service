package faang.school.notificationservice.bot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:tgBotMessages.properties")
public class TelegramBotMessageProperties {

    @Value("${start-message}")
    private String startMessage;

    @Value("${stop-message}")
    private String stopMessage;

    @Value("{rejection-message}")
    private String rejectionMessage;


    @Value("${user-chose-not-subscribed}")
    private String userChoseNotSubscribedMessage;

    @Value("${user-already-subscribed}")
    private String userAlreadySubscribedMessage;

    @Value("${subscribe-button-text}")
    private String subscribeButtonText;

    @Value("${success-unsubscribed}")
    private String successUnsubscribedMessage;

    @Value("${offer-to-subscribe}")
    private String offerToSubscribeMessage;

    @Value("${success-subscribed}")
    private String successSubscribedMessage;

    @Value("${user-not-subscribed}")
    private String userNotSubscribedMessage;

    @Value("${user-not-found}")
    private String userNotFoundMessage;
}
