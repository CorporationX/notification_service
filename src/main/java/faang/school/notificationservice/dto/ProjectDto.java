package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long id;

    @Positive
    private Long ownerId;

    @Positive
    private Long parentId;

    @NotBlank
    @Size(max = 128)
    private String name;

    @NotBlank
    @Size(max = 4096)
    private String description;

    private String coverImageId;

    @NotNull
    private String status;

    @NotNull
    private String visibility;

    private BigInteger storageSize;
}
