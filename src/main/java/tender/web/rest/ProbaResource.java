package tender.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import tender.domain.Proba;
import tender.repository.ProbaRepository;
import tender.service.ProbaQueryService;
import tender.service.ProbaService;
import tender.service.criteria.ProbaCriteria;
import tender.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tender.domain.Proba}.
 */
@RestController
@RequestMapping("/api")
public class ProbaResource {

    private final Logger log = LoggerFactory.getLogger(ProbaResource.class);

    private static final String ENTITY_NAME = "proba";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProbaService probaService;

    private final ProbaRepository probaRepository;

    private final ProbaQueryService probaQueryService;

    public ProbaResource(ProbaService probaService, ProbaRepository probaRepository, ProbaQueryService probaQueryService) {
        this.probaService = probaService;
        this.probaRepository = probaRepository;
        this.probaQueryService = probaQueryService;
    }

    /**
     * {@code POST  /probas} : Create a new proba.
     *
     * @param proba the proba to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proba, or with status {@code 400 (Bad Request)} if the proba has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/probas")
    public ResponseEntity<Proba> createProba(@RequestBody Proba proba) throws URISyntaxException {
        log.debug("REST request to save Proba : {}", proba);
        if (proba.getId() != null) {
            throw new BadRequestAlertException("A new proba cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proba result = probaService.save(proba);
        return ResponseEntity
            .created(new URI("/api/probas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /probas/:id} : Updates an existing proba.
     *
     * @param id the id of the proba to save.
     * @param proba the proba to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proba,
     * or with status {@code 400 (Bad Request)} if the proba is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proba couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/probas/{id}")
    public ResponseEntity<Proba> updateProba(@PathVariable(value = "id", required = false) final Long id, @RequestBody Proba proba)
        throws URISyntaxException {
        log.debug("REST request to update Proba : {}, {}", id, proba);
        if (proba.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proba.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!probaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Proba result = probaService.update(proba);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proba.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /probas/:id} : Partial updates given fields of an existing proba, field will ignore if it is null
     *
     * @param id the id of the proba to save.
     * @param proba the proba to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proba,
     * or with status {@code 400 (Bad Request)} if the proba is not valid,
     * or with status {@code 404 (Not Found)} if the proba is not found,
     * or with status {@code 500 (Internal Server Error)} if the proba couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/probas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Proba> partialUpdateProba(@PathVariable(value = "id", required = false) final Long id, @RequestBody Proba proba)
        throws URISyntaxException {
        log.debug("REST request to partial update Proba partially : {}, {}", id, proba);
        if (proba.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proba.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!probaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Proba> result = probaService.partialUpdate(proba);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proba.getId().toString())
        );
    }

    /**
     * {@code GET  /probas} : get all the probas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of probas in body.
     */
    @GetMapping("/probas")
    public ResponseEntity<List<Proba>> getAllProbas(ProbaCriteria criteria) {
        log.debug("REST request to get Probas by criteria: {}", criteria);
        List<Proba> entityList = probaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /probas/count} : count all the probas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/probas/count")
    public ResponseEntity<Long> countProbas(ProbaCriteria criteria) {
        log.debug("REST request to count Probas by criteria: {}", criteria);
        return ResponseEntity.ok().body(probaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /probas/:id} : get the "id" proba.
     *
     * @param id the id of the proba to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proba, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/probas/{id}")
    public ResponseEntity<Proba> getProba(@PathVariable Long id) {
        log.debug("REST request to get Proba : {}", id);
        Optional<Proba> proba = probaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proba);
    }

    /**
     * {@code DELETE  /probas/:id} : delete the "id" proba.
     *
     * @param id the id of the proba to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/probas/{id}")
    public ResponseEntity<Void> deleteProba(@PathVariable Long id) {
        log.debug("REST request to delete Proba : {}", id);
        probaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
