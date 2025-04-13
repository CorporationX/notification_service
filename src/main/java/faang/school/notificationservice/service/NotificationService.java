package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    CompletableFuture<Void> send(UserDto user, String message);

    UserDto.PreferredContact getPreferredContact();
}
