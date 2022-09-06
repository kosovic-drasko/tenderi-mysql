package tender.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Proba.
 */
@Entity
@Table(name = "proba")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Proba implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "broj")
    private Integer broj;

    @Column(name = "ime")
    private String ime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proba id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBroj() {
        return this.broj;
    }

    public Proba broj(Integer broj) {
        this.setBroj(broj);
        return this;
    }

    public void setBroj(Integer broj) {
        this.broj = broj;
    }

    public String getIme() {
        return this.ime;
    }

    public Proba ime(String ime) {
        this.setIme(ime);
        return this;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proba)) {
            return false;
        }
        return id != null && id.equals(((Proba) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proba{" +
            "id=" + getId() +
            ", broj=" + getBroj() +
            ", ime='" + getIme() + "'" +
            "}";
    }
}
