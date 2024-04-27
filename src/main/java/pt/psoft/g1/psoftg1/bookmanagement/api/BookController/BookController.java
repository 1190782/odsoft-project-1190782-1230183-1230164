package pt.psoft.g1.psoftg1;

import java.net.URISyntaxException;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "books", description = "Endpoints for managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
class BarController {

    private static final String IF_MATCH = "If-Match";

    private static final Logger logger = LoggerFactory.getLogger(BarController.class);

    private final BarService service;

    private final BarViewMapper barViewMapper;

    @Operation(summary = "Gets all bars")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            // Use the `array` property instead of `schema`
            array = @ArraySchema(schema = @Schema(implementation = BarView.class))) })
    @GetMapping
    public Iterable<BarView> findAll() {
        return barViewMapper.toBarView(service.findAll());
    }

    @Operation(summary = "Gets a specific Bar")
    @GetMapping(value = "/{id}")
    public ResponseEntity<BarView> findById(
            @PathVariable("id") @Parameter(description = "The id of the Bar to find") final Long id) {
        final var bar = service.findOne(id).orElseThrow(() -> new NotFoundException(Bar.class, id));

        return ResponseEntity.ok().eTag(Long.toString(bar.getVersion())).body(barViewMapper.toBarView(bar));
    }

    @Operation(summary = "Creates a new Bar")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BarView> create(@Valid @RequestBody final CreateBarRequest resource) {

        final var bar = service.create(resource);

        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(bar.getId().toString())
                .build().toUri();

        return ResponseEntity.created(newbarUri).eTag(Long.toString(bar.getVersion()))
                .body(barViewMapper.toBarView(bar));
    }

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

    /**
     * PUT is used either to fully replace an existing resource or create a new
     * resource, i.e., UPSERT.
     * <p>
     * In this case, since the id is autogenerated by the server we do not allow
     * "PUT as insert" - to create a new Bar the client must issue a POST to /bar/
     *
     * @param request
     * @param id
     * @param resource
     * @return
     * @throws URISyntaxException
     */
    //
    @Operation(summary = "Fully replaces an existing bar. If the specified id does not exist does nothing and returns 400.")
    @PutMapping(value = "/{id}")
    public ResponseEntity<BarView> upsert(final WebRequest request,
                                          @PathVariable("id") @Parameter(description = "The id of the bar to replace/create") final Long id,
                                          @Valid @RequestBody final EditBarRequest resource) {
        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            // no if-match header was sent, so we are in INSERT mode
            return ResponseEntity.badRequest().build();
        }
        // if-match header was sent, so we are in UPDATE mode
        final var bar = service.update(id, resource, getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(bar.getVersion())).body(barViewMapper.toBarView(bar));
    }

    @Operation(summary = "Partially updates an existing bar")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<BarView> partialUpdate(final WebRequest request,
                                                 @PathVariable("id") @Parameter(description = "The id of the bar to update") final Long id,
                                                 @Valid @RequestBody final EditBarRequest resource) {
        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        final var bar = service.partialUpdate(id, resource, getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(bar.getVersion())).body(barViewMapper.toBarView(bar));
    }

    @Operation(summary = "Deletes an existing bar")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<BarView> delete(final WebRequest request,
                                          @PathVariable("id") @Parameter(description = "The id of the bar to delete") final Long id) {
        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional DELETE using 'if-match'");
        }
        final int count = service.deleteById(id, getVersionFromIfMatchHeader(ifMatchValue));

        // TODO check if we can distinguish between a 404 and a 409
        return count == 1 ? ResponseEntity.noContent().build() : ResponseEntity.status(409).build();
    }
}
