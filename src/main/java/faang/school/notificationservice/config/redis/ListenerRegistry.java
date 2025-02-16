package faang.school.notificationservice.config.redis;

import java.util.ArrayList;
import java.util.List;

public class ListenerRegistry {
    private final List<ListenerRegistryItem> listenerRegistryItems = new ArrayList<>();

    public void addListenerRegistryItem(ListenerRegistryItem item) {
        listenerRegistryItems.add(item);
    }
}
