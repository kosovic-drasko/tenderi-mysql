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
        value = "\tSELECT *\n" +
        "FROM \n" +
        "    (SELECT ponude.*,\n" +
        "       \n" +
        "         row_number()\n" +
        "        OVER (PARTITION BY ponude.sifra_ponude order by ponudjaci_id) AS row_num\n" +
        "    FROM ponude\n" +
        "\t\t\n" +
        "\t\tJOIN ponudjaci on ponude.ponudjaci_id=ponudjaci.id) t\n" +
        "WHERE row_num =1",
        nativeQuery = true
    )
    List<Ponude> findBySifraPostupkaPonudjaci(@Param("sifra") Integer sifra);
}
