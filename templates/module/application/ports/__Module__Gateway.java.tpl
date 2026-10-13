package __BASE__.modules.__module__.application.ports;

import __BASE__.modules.__module__.domain.__Module__;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface __Module__Gateway {

    __Module__ save(__Module__ __module__);

    Optional<__Module__> findById(UUID id);

    List<__Module__> findAll();

    void deleteById(UUID id);
}
