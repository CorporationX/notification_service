package faang.school.notificationservice.utils;

import faang.school.notificationservice.exception.impl.non_retryable.ListSizeNotOneException;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ListUtil {
    public <T> T requireSingleElement(List<T> list, Class<?> targetClass, Class<?> eventType) {
        int size = list.size();

        if (size == 0) {
            String error = String.format("No %s found for %s",
                    targetClass.getSimpleName(), eventType.getSimpleName());
            log.error(error);
            throw new NotFoundElementException(error);
        }

        if (size != 1) {
            String error = String.format("Expected exactly 1 %s for: %s, but got %d",
                    targetClass.getSimpleName(), eventType.getSimpleName(), size);
            log.error(error);
            throw new ListSizeNotOneException(error);
        }

        return list.get(0);
    }
}
