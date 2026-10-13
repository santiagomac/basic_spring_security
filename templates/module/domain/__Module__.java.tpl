package __BASE__.modules.__module__.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record __Module__(UUID id, String name, String description, LocalDateTime createdAt) {

    public static __Module__ create(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        return new __Module__(null, name.trim(), description, null);
    }

    public __Module__ update(String name, String description) {
        return new __Module__(id, __Module__.create(name, description).name(), description, createdAt);
    }
}
