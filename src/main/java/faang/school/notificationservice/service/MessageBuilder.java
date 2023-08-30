package faang.school.notificationservice.service;

import java.util.Locale;

public interface MessageBuilder<T>{

    String build(T event, Locale locale);
}
