package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Реализация {@link MessageBuilder} для создания сообщений о событиях лайков постов.
 * Создает сообщения в соответствии с языковыми настройками, используя данные об авторе поста и пользователе, поставившем лайк.
 */
@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikePostEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    /**
     * Возвращает класс события, которое данный билдер может обрабатывать.
     *
     * @return класс события LikePostEvent
     */
    @Override
    public Class<?> getInstance() {
        return LikePostEvent.class;
    }

    /**
     * Создает сообщение о лайке поста.
     *
     * @param event  событие лайка поста
     * @param locale язык и регион для формирования сообщения
     * @return текст уведомления с учетом языковых настроек
     */
    @Override
    public String buildMessage(LikePostEvent event, Locale locale) {
        UserDto postAuthor = userServiceClient.getUser(event.getPostAuthorId());
        UserDto liker = userServiceClient.getUser(event.getLikerId());

        return messageSource.getMessage("postLike.new.2",
                new Object[]{
                        postAuthor.getUsername(),
                        liker.getUsername()},
                locale);
    }
}
