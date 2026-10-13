package __BASE__.modules.__module__.infrastructure;

import __BASE__.modules.__module__.application.ports.__Module__Gateway;
import __BASE__.modules.__module__.domain.__Module__;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class __Module__GatewayAdapter implements __Module__Gateway {

    private final __Module__JpaRepository repository;

    @Override
    public __Module__ save(__Module__ __module__) {
        return toModel(repository.save(toEntity(__module__)));
    }

    @Override
    public Optional<__Module__> findById(UUID id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public List<__Module__> findAll() {
        return repository.findAll().stream().map(this::toModel).toList();
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    private __Module__Entity toEntity(__Module__ __module__) {
        return new __Module__Entity(__module__.id(), __module__.name(), __module__.description(), __module__.createdAt());
    }

    private __Module__ toModel(__Module__Entity entity) {
        return new __Module__(entity.getId(), entity.getName(), entity.getDescription(), entity.getCreatedAt());
    }
}
