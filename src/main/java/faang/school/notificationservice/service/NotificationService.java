package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messageBulder.MessageBuilder;
import faang.school.notificationservice.model.PreferredContact;

public interface NotificationService {
    PreferredContact getPreferredContact();

    void send(UserDto user, String message);

}
