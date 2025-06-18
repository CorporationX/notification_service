package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.client.user_service.UserClientResponseDto;

public interface NotificationService {

    void send(UserClientResponseDto user, String message);

    UserClientResponseDto.PreferredContact getPreferredContact();
}
