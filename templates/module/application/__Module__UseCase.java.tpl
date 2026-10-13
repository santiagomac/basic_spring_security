package __BASE__.modules.__module__.application;

import __BASE__.common.exception.NotFoundException;
import __BASE__.modules.__module__.application.dto.__Module__Request;
import __BASE__.modules.__module__.application.dto.__Module__Response;
import __BASE__.modules.__module__.application.ports.__Module__Gateway;
import __BASE__.modules.__module__.domain.__Module__;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class __Module__UseCase {

    private final __Module__Gateway __module__Gateway;

    @Transactional
    public __Module__Response create(__Module__Request request) {
        return __Module__Response.from(__module__Gateway.save(__Module__.create(request.name(), request.description())));
    }

    @Transactional(readOnly = true)
    public __Module__Response get(UUID id) {
        return __Module__Response.from(find(id));
    }

    @Transactional(readOnly = true)
    public List<__Module__Response> list() {
        return __module__Gateway.findAll().stream().map(__Module__Response::from).toList();
    }

    @Transactional
    public __Module__Response update(UUID id, __Module__Request request) {
        __Module__ updated = find(id).update(request.name(), request.description());
        return __Module__Response.from(__module__Gateway.save(updated));
    }

    @Transactional
    public void delete(UUID id) {
        find(id);
        __module__Gateway.deleteById(id);
    }

    private __Module__ find(UUID id) {
        return __module__Gateway.findById(id).orElseThrow(() -> new NotFoundException("__Module__ not found: " + id));
    }
}
