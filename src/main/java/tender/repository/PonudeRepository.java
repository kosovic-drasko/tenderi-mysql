package tender.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tender.domain.Ponude;

/**
 * Spring Data SQL repository for the Ponude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PonudeRepository extends JpaRepository<Ponude, Long>, JpaSpecificationExecutor<Ponude> {
    @Modifying
    @Transactional
    @Query("delete from Ponude p where p.sifraPonude=:sifraPonude")
    void deletePonudeSifraPonude(@Param("sifraPonude") Integer sifraPonude);

    @Query("select p from Ponude p where p.sifraPostupka=:sifraPostupka")
    List<Ponude> findBySifraPostupkaList(@Param("sifraPostupka") Integer sifraPostupka);

    @Query("select p from Ponude p where p.sifraPostupka=:sifraPostupka")
    List<?> findBySifraPostupka(@Param("sifraPostupka") Integer sifraPostupka);

    @Query("select  p from Ponude p where p.sifraPostupka=:sifraPostupka and p.sifraPonude=:sifraPonude")
    List<Ponude> findPonudaPostupak(@Param("sifraPostupka") Integer sifraPostupka, @Param("sifraPonude") Integer sifraPonude);

    @Modifying
    @Transactional
    @Query("DELETE from Ponude p WHERE p.selected = true")
    void deleteBySelected();

    @Modifying
    @Transactional
    @Query("UPDATE Ponude p SET p.selected=true WHERE p.id = :Id")
    void updateSlected(@Param("Id") Long Id);

    @Query(
        value = "SELECT * FROM (SELECT \n" +
        "  ponude.*,\n" +
        "  ponudjaci.naziv_ponudjaca,\n" +
        "  ROW_NUMBER() over(partition BY ponude.sifra_ponude ORDER BY \n" +
        "ponude.id DESC)rn\n" +
        "FROM\n" +
        "  ponude\n" +
        "  \n" +
        "  INNER JOIN ponudjaci ON (ponude.ponudjaci_id = ponudjaci.id\n" +
        " )WHERE ponude.sifra_postupka=:sifra)a\n" +
        "WHERE rn=1",
        nativeQuery = true
    )
    List<Ponude> findBySifraPostupkaPonudjaci(@Param("sifra") Integer sifra);
}
