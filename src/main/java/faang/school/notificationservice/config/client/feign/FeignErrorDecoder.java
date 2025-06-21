package faang.school.notificationservice.config.client.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Remote resource error: {}", methodKey);
        return defaultDecoder.decode(methodKey, response);
    }
}

