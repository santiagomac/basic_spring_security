package __BASE__.modules.__module__.infrastructure;

import __BASE__.modules.__module__.application.__Module__UseCase;
import __BASE__.modules.__module__.application.dto.__Module__Request;
import __BASE__.modules.__module__.application.dto.__Module__Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/__modules__")
@RequiredArgsConstructor
public class __Module__Controller {

    private final __Module__UseCase useCase;

    @PostMapping
    public ResponseEntity<__Module__Response> create(@Valid @RequestBody __Module__Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.create(request));
    }

    @GetMapping
    public List<__Module__Response> list() {
        return useCase.list();
    }

    @GetMapping("/{id}")
    public __Module__Response get(@PathVariable UUID id) {
        return useCase.get(id);
    }

    @PutMapping("/{id}")
    public __Module__Response update(@PathVariable UUID id, @Valid @RequestBody __Module__Request request) {
        return useCase.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        useCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
