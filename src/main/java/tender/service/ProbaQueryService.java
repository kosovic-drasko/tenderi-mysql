package tender.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tender.domain.*; // for static metamodels
import tender.domain.Proba;
import tender.repository.ProbaRepository;
import tender.service.criteria.ProbaCriteria;

/**
 * Service for executing complex queries for {@link Proba} entities in the database.
 * The main input is a {@link ProbaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Proba} or a {@link Page} of {@link Proba} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProbaQueryService extends QueryService<Proba> {

    private final Logger log = LoggerFactory.getLogger(ProbaQueryService.class);

    private final ProbaRepository probaRepository;

    public ProbaQueryService(ProbaRepository probaRepository) {
        this.probaRepository = probaRepository;
    }

    /**
     * Return a {@link List} of {@link Proba} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Proba> findByCriteria(ProbaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Proba> specification = createSpecification(criteria);
        return probaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Proba} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Proba> findByCriteria(ProbaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Proba> specification = createSpecification(criteria);
        return probaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProbaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Proba> specification = createSpecification(criteria);
        return probaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProbaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Proba> createSpecification(ProbaCriteria criteria) {
        Specification<Proba> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Proba_.id));
            }
            if (criteria.getBroj() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBroj(), Proba_.broj));
            }
            if (criteria.getIme() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIme(), Proba_.ime));
            }
        }
        return specification;
    }
}
