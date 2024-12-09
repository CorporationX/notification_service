package faang.school.notificationservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Language {
    EN("en"),
    RU("ru"),
    FR("fr"),
    DE("de");

    private final String tag;
}