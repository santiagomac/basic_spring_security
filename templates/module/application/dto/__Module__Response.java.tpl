package __BASE__.modules.__module__.application.dto;

import __BASE__.modules.__module__.domain.__Module__;

import java.time.LocalDateTime;
import java.util.UUID;

public record __Module__Response(UUID id, String name, String description, LocalDateTime createdAt) {

    public static __Module__Response from(__Module__ __module__) {
        return new __Module__Response(__module__.id(), __module__.name(), __module__.description(), __module__.createdAt());
    }
}
