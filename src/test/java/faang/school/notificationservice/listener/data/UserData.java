package faang.school.notificationservice.listener.data;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class UserData {

    public static final UserDto CORRECT_USER_DTO = UserDto.builder()
            .id(1L)
            .email("email")
            .phone("phone")
            .username("username")
            .build();

    public static final UserDto NO_ID_USER_DTO = UserDto.builder()
            .email("email")
            .phone("phone")
            .username("username")
            .build();

    public static final UserDto NO_EMAIL_USER_DTO = UserDto.builder()
            .id(1L)
            .phone("phone")
            .username("username")
            .build();

    public static final UserDto NO_PHONE_USER_DTO = UserDto.builder()
            .id(1L)
            .email("email")
            .username("username")
            .build();

    public static final UserDto NO_USERNAME_USER_DTO = UserDto.builder()
            .id(1L)
            .email("email")
            .phone("phone")
            .build();

    public static Stream<Arguments> invalidNewFollowerEvents() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(CORRECT_USER_DTO, null),
                Arguments.of(null, CORRECT_USER_DTO),
                Arguments.of(NO_ID_USER_DTO, CORRECT_USER_DTO),
                Arguments.of(CORRECT_USER_DTO, NO_ID_USER_DTO),
                Arguments.of(NO_EMAIL_USER_DTO, CORRECT_USER_DTO),
                Arguments.of(CORRECT_USER_DTO, NO_EMAIL_USER_DTO),
                Arguments.of(NO_PHONE_USER_DTO, CORRECT_USER_DTO),
                Arguments.of(CORRECT_USER_DTO, NO_PHONE_USER_DTO),
                Arguments.of(NO_USERNAME_USER_DTO, CORRECT_USER_DTO),
                Arguments.of(CORRECT_USER_DTO, NO_USERNAME_USER_DTO)
        );
    }

    public static Stream<Arguments> invalidUnfollowEvents() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(CORRECT_USER_DTO, null),
                Arguments.of(null, CORRECT_USER_DTO),
                Arguments.of(NO_ID_USER_DTO, CORRECT_USER_DTO),
                Arguments.of(CORRECT_USER_DTO, NO_ID_USER_DTO),
                Arguments.of(NO_EMAIL_USER_DTO, CORRECT_USER_DTO),
                Arguments.of(CORRECT_USER_DTO, NO_EMAIL_USER_DTO),
                Arguments.of(NO_PHONE_USER_DTO, CORRECT_USER_DTO),
                Arguments.of(CORRECT_USER_DTO, NO_PHONE_USER_DTO),
                Arguments.of(NO_USERNAME_USER_DTO, CORRECT_USER_DTO),
                Arguments.of(CORRECT_USER_DTO, NO_USERNAME_USER_DTO)
        );
    }
}
