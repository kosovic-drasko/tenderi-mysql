package tender.web.rest;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import tender.domain.Vrednovanje;
import tender.repository.VrednovanjeRepository;
import tender.service.VrednovanjeQueryService;
import tender.service.VrednovanjeService;
import tender.service.criteria.VrednovanjeCriteria;

/**
 * REST controller for managing {@link Vrednovanje}.
 */
@RestController
@RequestMapping("/api")
public class VrednovanjeResource {

    private final Logger log = LoggerFactory.getLogger(VrednovanjeResource.class);

    private final VrednovanjeService vrednovanjeService;

    private final VrednovanjeRepository vrednovanjeRepository;

    private final VrednovanjeQueryService vrednovanjeQueryService;

    public VrednovanjeResource(
        VrednovanjeService vrednovanjeService,
        VrednovanjeRepository vrednovanjeRepository,
        VrednovanjeQueryService vrednovanjeQueryService
    ) {
        this.vrednovanjeService = vrednovanjeService;
        this.vrednovanjeRepository = vrednovanjeRepository;
        this.vrednovanjeQueryService = vrednovanjeQueryService;
    }

    /**
     * {@code GET  /vrednovanjes} : get all the vrednovanjes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vrednovanjes in body.
     */
    @GetMapping("/vrednovanjes")
    public ResponseEntity<List<Vrednovanje>> getAllVrednovanjes(
        VrednovanjeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Vrednovanjes by criteria: {}", criteria);
        Page<Vrednovanje> page = vrednovanjeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vrednovanjes/count} : count all the vrednovanjes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vrednovanjes/count")
    public ResponseEntity<Long> countVrednovanjes(VrednovanjeCriteria criteria) {
        log.debug("REST request to count Vrednovanjes by criteria: {}", criteria);
        return ResponseEntity.ok().body(vrednovanjeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vrednovanjes/:id} : get the "id" vrednovanje.
     *
     * @param id the id of the vrednovanje to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vrednovanje, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vrednovanjes/{id}")
    public ResponseEntity<Vrednovanje> getVrednovanje(@PathVariable Long id) {
        log.debug("REST request to get Vrednovanje : {}", id);
        Optional<Vrednovanje> vrednovanje = vrednovanjeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vrednovanje);
    }
}
