package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ExceptionMessages;
import faang.school.notificationservice.exception.feign.UserServiceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceClient userServiceClient;

    public UserDto fetchUser(Long id) {
        UserDto user;
        try {
            user = userServiceClient.getUser(id);
        } catch (FeignException | RestClientException exception) {
            throw new UserServiceException(String.format(ExceptionMessages.USER_DATA_FETCH_FAILURE, id), exception);
        }
        return user;
    }

    public void putChatIdInUser(long id, long chatId){
        userServiceClient.putChatIdInUser(id, chatId);
        log.info("Request to add chatId to table sent");
    }
}
