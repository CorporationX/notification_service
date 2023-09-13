package faang.school.notificationservice.client;

import faang.school.notificationservice.dto.ExtendedContactDto;
import faang.school.notificationservice.dto.TgContactDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @GetMapping("/{id}")
    UserDto getUser(@PathVariable("id") long userId);

    @PostMapping("/contacts")
    void updateUserContact(@RequestBody TgContactDto tgContactDto);

    @GetMapping("/{id}/contacts")
    ExtendedContactDto getUserContact(@PathVariable("id") Long userId);

    @GetMapping("/get-by-phone")
    Long findUserIdByPhoneNumber(@RequestParam(name = "phone") String phoneNumber);

}
