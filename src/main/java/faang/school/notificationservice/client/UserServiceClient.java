package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.ExtendedContactDto;
import faang.school.notificationservice.dto.TgContactDto;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @GetMapping("/{id}")
    UserDto getUser(@PathVariable("id") long id);

    @GetMapping("/user/{id}")
    UserDto getUserNoPublish(@PathVariable("id") long id);

    @PostMapping("/get-by-ids")
    List<UserDto> getUsersByIds(@RequestBody List<Long> ids);

    @GetMapping("/events/{id}")
    EventDto getEvent(@PathVariable("id") long id);

    @GetMapping("/notify/{id}")
    UserDto getUserNotify(@PathVariable("id") long id);

    @PostMapping("/contacts")
    void updateUserContact(@RequestBody TgContactDto tgContactDto);

    @GetMapping("/{id}/contacts")
    ExtendedContactDto getUserContact(@PathVariable("id") Long userId);

    @GetMapping("/get-by-phone")
    Long findUserIdByPhoneNumber(@RequestParam(name = "phone") String phoneNumber);
}
