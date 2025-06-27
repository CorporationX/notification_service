package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractNotificationService implements NotificationService {

    private final PreferredContact preferredContact;

    public AbstractNotificationService(PreferredContact preferredContact) {
        this.preferredContact = preferredContact;
    }

    @Override
    public PreferredContact getPreferredContact() {
        return preferredContact;
    }
}