package faang.school.notificationservice.client;

import faang.school.notificationservice.config.context.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FeignUserInterceptor implements RequestInterceptor {

    private final UserContext userContext;

    @Override
    public void apply(RequestTemplate template) {
        log.info("Подставляем x-user-id {} в RequestTemplate", userContext.getUserId());
        template.header("x-user-id", String.valueOf(userContext.getUserId()));
    }
}
