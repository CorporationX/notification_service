package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Реализация построителя сообщений для событий о новых подписчиках.
 * <p>
 * Формирует локализованное сообщение о новом подписчике, используя:
 * <ul>
 *   <li>Данные о подписчике из UserService</li>
 *   <li>Локализованные шаблоны сообщений через MessageSource</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEventDto> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    /**
     * Возвращает тип обрабатываемых событий.
     *
     * @return класс FollowerEventDto
     */
    @Override
    public Class<FollowerEventDto> getInstance() {
        return FollowerEventDto.class;
    }

    /**
     * Строит сообщение о новом подписчике.
     *
     * @param event  событие о подписке
     * @param locale локаль для локализации сообщения (если null - используется системная)
     * @return готовое текстовое сообщение
     */
    @Override
    public String buildMessage(FollowerEventDto event, Locale locale) {
        UserDto follower = userServiceClient.getUser(event.getFollowerId());
        String followerName = follower != null ? follower.getUsername() : "EN";

        return messageSource.getMessage(
                "follower.new",
                new Object[]{followerName},
                locale != null ? locale : Locale.getDefault()
        );
    }
}