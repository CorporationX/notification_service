package faang.school.notificationservice.config.http_client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class HttpClientConfig {

    @Bean(name = "httpClient")
    public HttpClient client() {
        return HttpClient.newHttpClient();
    }
}
