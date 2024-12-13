package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record PostDto(
        @NotNull Long id,
        @NotBlank String content,
        @Nullable Long authorId,
        @Nullable Long projectId,
        @Nullable List<Long> likesIds,
        @Nullable List<Long> commentsIds,
        @NotNull boolean published,
        @NotNull boolean deleted,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Nullable LocalDateTime publishedAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Nullable LocalDateTime createdAt
) {
}

