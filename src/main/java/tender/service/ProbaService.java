package tender.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tender.domain.Proba;
import tender.repository.ProbaRepository;

/**
 * Service Implementation for managing {@link Proba}.
 */
@Service
@Transactional
public class ProbaService {

    private final Logger log = LoggerFactory.getLogger(ProbaService.class);

    private final ProbaRepository probaRepository;

    public ProbaService(ProbaRepository probaRepository) {
        this.probaRepository = probaRepository;
    }

    /**
     * Save a proba.
     *
     * @param proba the entity to save.
     * @return the persisted entity.
     */
    public Proba save(Proba proba) {
        log.debug("Request to save Proba : {}", proba);
        return probaRepository.save(proba);
    }

    /**
     * Update a proba.
     *
     * @param proba the entity to save.
     * @return the persisted entity.
     */
    public Proba update(Proba proba) {
        log.debug("Request to save Proba : {}", proba);
        return probaRepository.save(proba);
    }

    /**
     * Partially update a proba.
     *
     * @param proba the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Proba> partialUpdate(Proba proba) {
        log.debug("Request to partially update Proba : {}", proba);

        return probaRepository
            .findById(proba.getId())
            .map(existingProba -> {
                if (proba.getBroj() != null) {
                    existingProba.setBroj(proba.getBroj());
                }
                if (proba.getIme() != null) {
                    existingProba.setIme(proba.getIme());
                }

                return existingProba;
            })
            .map(probaRepository::save);
    }

    /**
     * Get all the probas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Proba> findAll() {
        log.debug("Request to get all Probas");
        return probaRepository.findAll();
    }

    /**
     * Get one proba by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Proba> findOne(Long id) {
        log.debug("Request to get Proba : {}", id);
        return probaRepository.findById(id);
    }

    /**
     * Delete the proba by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Proba : {}", id);
        probaRepository.deleteById(id);
    }
}
