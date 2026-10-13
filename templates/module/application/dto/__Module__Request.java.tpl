package __BASE__.modules.__module__.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record __Module__Request(@NotBlank @Size(max = 255) String name, @Size(max = 1000) String description) {
}
