package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractEventListener<T extends NotificationEvent> {

    public abstract boolean isEventValid(T event);

    @SafeVarargs
    protected final boolean validateObjectNonNullData(Object o, Supplier<Object>... fieldGetters) {
        return Objects.nonNull(o) && Arrays.stream(fieldGetters).map(Supplier::get).noneMatch(Objects::isNull);
    }

    protected boolean isUserDtoValid(UserDto userDto) {
        return validateObjectNonNullData(
                userDto,
                userDto::getId,
                userDto::getUsername
        );
    }
}