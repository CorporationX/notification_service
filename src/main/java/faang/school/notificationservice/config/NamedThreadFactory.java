package faang.school.notificationservice.config;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Фабрика потоков с заданным префиксом имени.
 * Создает потоки с именами, состоящими из префикса и порядкового номера.
 */
public class NamedThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Создает фабрику потоков с указанным префиксом имени.
     *
     * @param namePrefix префикс имени для создаваемых потоков
     */
    public NamedThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    /**
     * Создает новый поток с заданным Runnable и уникальным именем.
     *
     * @param r задача для выполнения в потоке
     * @return новый поток с заданным именем
     */
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, namePrefix + counter.getAndIncrement());
    }
}