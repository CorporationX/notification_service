package faang.school.notificationservice.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для указания канала Redis, на который будет подписан слушатель.
 * Используется для автоматической регистрации слушателей в конфигурации Redis.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisChannel {
    String value();
}