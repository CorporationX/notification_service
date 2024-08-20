package faang.school.notificationservice.subscriber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.Message;

import static org.assertj.core.api.Assertions.assertThat;

public class RedisCommentSubscriberTest {

    private RedisCommentSubscriber redisCommentSubscriber;

    @BeforeEach
    @DisplayName("Setup RedisCommentSubscriber and clear eventList before each test")
    public void setUp() {
        redisCommentSubscriber = new RedisCommentSubscriber();
        redisCommentSubscriber.eventList.clear();
    }

    @Test
    @DisplayName("onMessage() should add message body to eventList")
    public void onMessage_ShouldAddMessageBodyToEventList() {
        byte[] channel = "TestChannel".getBytes();
        byte[] body = "Test message".getBytes();
        TestMessage message = new TestMessage(body, channel);

        redisCommentSubscriber.onMessage(message, null);

        String expectedMessageString = message.toString();
        assertThat(redisCommentSubscriber.eventList).hasSize(1);
        assertThat(redisCommentSubscriber.eventList.get(0)).isEqualTo(expectedMessageString);
    }

    @Test
    @DisplayName("eventList should remain empty if no messages received")
    public void eventList_ShouldRemainEmptyIfNoMessagesReceived() {
        assertThat(redisCommentSubscriber.eventList).isEmpty();
    }
}

class TestMessage implements Message {
    private final byte[] body;
    private final byte[] channel;

    public TestMessage(byte[] body, byte[] channel) {
        this.body = body;
        this.channel = channel;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public byte[] getChannel() {
        return channel;
    }
}