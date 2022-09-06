package tender.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tender.domain.Proba;

/**
 * Spring Data SQL repository for the Proba entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProbaRepository extends JpaRepository<Proba, Long>, JpaSpecificationExecutor<Proba> {}
