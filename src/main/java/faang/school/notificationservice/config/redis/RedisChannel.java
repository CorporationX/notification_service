package faang.school.notificationservice.config.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для указания Redis-топика, на который будет подписан listener (слушатель).
 * Применяется к классам, реализующим {@link org.springframework.data.redis.connection.MessageListener},
 * чтобы указать канал (топик), на который они должны реагировать.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisChannel {
    String value();
}