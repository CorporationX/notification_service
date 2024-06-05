package faang.school.notificationservice.integration.listener;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.integration.IntegrationTestBase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.Topic;
import org.springframework.test.annotation.DirtiesContext;

class MentorshipAcceptedEventListenerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static WireMockServer wireMockServer;

    private static final Long MENTEE_ID = 1L;
    private static final Long MENTOR_ID = 2L;
    private static final Long REQUEST_ID = 3L;
    private static final String jsonMentee = """
            {
                "id": 1,
                "username": "JohnDoe",
                "email": "johndoe@example.com",
                "preference": "EMAIL"
            }
            """;
    private static final String jsonMentor = """
            {
                "id": 2,
                "username": "JaneSmith",
                "email": "janesmith@example.com",
                "preference": "EMAIL"
            }
            """;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080));
        wireMockServer.start();

        wireMockServer.stubFor(WireMock.get("/users/1")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonMentee)));

        wireMockServer.stubFor(WireMock.get("/users/2")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonMentor)));
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @DirtiesContext
    @Test
    public void sendMessageWhenMentorshipAcceptedEvent() {
        Topic topic = () -> "mentorship_accepted_channel";
        MentorshipAcceptedEvent eventMessage = new MentorshipAcceptedEvent(MENTEE_ID, MENTOR_ID, REQUEST_ID);
        redisTemplate.convertAndSend(topic.getTopic(), eventMessage);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}