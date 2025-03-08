package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserProfileDto;

public interface NotificationService {
    void send(UserProfileDto user, String message);
    UserProfileDto.PreferredContact getPreferredContact();
    boolean supports(UserProfileDto.PreferredContact preferredContact);
}