package tender.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tender.IntegrationTest;
import tender.domain.Proba;
import tender.repository.ProbaRepository;
import tender.service.criteria.ProbaCriteria;

/**
 * Integration tests for the {@link ProbaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProbaResourceIT {

    private static final Integer DEFAULT_BROJ = 1;
    private static final Integer UPDATED_BROJ = 2;
    private static final Integer SMALLER_BROJ = 1 - 1;

    private static final String DEFAULT_IME = "AAAAAAAAAA";
    private static final String UPDATED_IME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/probas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProbaRepository probaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProbaMockMvc;

    private Proba proba;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proba createEntity(EntityManager em) {
        Proba proba = new Proba().broj(DEFAULT_BROJ).ime(DEFAULT_IME);
        return proba;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proba createUpdatedEntity(EntityManager em) {
        Proba proba = new Proba().broj(UPDATED_BROJ).ime(UPDATED_IME);
        return proba;
    }

    @BeforeEach
    public void initTest() {
        proba = createEntity(em);
    }

    @Test
    @Transactional
    void createProba() throws Exception {
        int databaseSizeBeforeCreate = probaRepository.findAll().size();
        // Create the Proba
        restProbaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proba)))
            .andExpect(status().isCreated());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeCreate + 1);
        Proba testProba = probaList.get(probaList.size() - 1);
        assertThat(testProba.getBroj()).isEqualTo(DEFAULT_BROJ);
        assertThat(testProba.getIme()).isEqualTo(DEFAULT_IME);
    }

    @Test
    @Transactional
    void createProbaWithExistingId() throws Exception {
        // Create the Proba with an existing ID
        proba.setId(1L);

        int databaseSizeBeforeCreate = probaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProbaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proba)))
            .andExpect(status().isBadRequest());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProbas() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList
        restProbaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proba.getId().intValue())))
            .andExpect(jsonPath("$.[*].broj").value(hasItem(DEFAULT_BROJ)))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)));
    }

    @Test
    @Transactional
    void getProba() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get the proba
        restProbaMockMvc
            .perform(get(ENTITY_API_URL_ID, proba.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proba.getId().intValue()))
            .andExpect(jsonPath("$.broj").value(DEFAULT_BROJ))
            .andExpect(jsonPath("$.ime").value(DEFAULT_IME));
    }

    @Test
    @Transactional
    void getProbasByIdFiltering() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        Long id = proba.getId();

        defaultProbaShouldBeFound("id.equals=" + id);
        defaultProbaShouldNotBeFound("id.notEquals=" + id);

        defaultProbaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProbaShouldNotBeFound("id.greaterThan=" + id);

        defaultProbaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProbaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProbasByBrojIsEqualToSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where broj equals to DEFAULT_BROJ
        defaultProbaShouldBeFound("broj.equals=" + DEFAULT_BROJ);

        // Get all the probaList where broj equals to UPDATED_BROJ
        defaultProbaShouldNotBeFound("broj.equals=" + UPDATED_BROJ);
    }

    @Test
    @Transactional
    void getAllProbasByBrojIsNotEqualToSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where broj not equals to DEFAULT_BROJ
        defaultProbaShouldNotBeFound("broj.notEquals=" + DEFAULT_BROJ);

        // Get all the probaList where broj not equals to UPDATED_BROJ
        defaultProbaShouldBeFound("broj.notEquals=" + UPDATED_BROJ);
    }

    @Test
    @Transactional
    void getAllProbasByBrojIsInShouldWork() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where broj in DEFAULT_BROJ or UPDATED_BROJ
        defaultProbaShouldBeFound("broj.in=" + DEFAULT_BROJ + "," + UPDATED_BROJ);

        // Get all the probaList where broj equals to UPDATED_BROJ
        defaultProbaShouldNotBeFound("broj.in=" + UPDATED_BROJ);
    }

    @Test
    @Transactional
    void getAllProbasByBrojIsNullOrNotNull() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where broj is not null
        defaultProbaShouldBeFound("broj.specified=true");

        // Get all the probaList where broj is null
        defaultProbaShouldNotBeFound("broj.specified=false");
    }

    @Test
    @Transactional
    void getAllProbasByBrojIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where broj is greater than or equal to DEFAULT_BROJ
        defaultProbaShouldBeFound("broj.greaterThanOrEqual=" + DEFAULT_BROJ);

        // Get all the probaList where broj is greater than or equal to UPDATED_BROJ
        defaultProbaShouldNotBeFound("broj.greaterThanOrEqual=" + UPDATED_BROJ);
    }

    @Test
    @Transactional
    void getAllProbasByBrojIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where broj is less than or equal to DEFAULT_BROJ
        defaultProbaShouldBeFound("broj.lessThanOrEqual=" + DEFAULT_BROJ);

        // Get all the probaList where broj is less than or equal to SMALLER_BROJ
        defaultProbaShouldNotBeFound("broj.lessThanOrEqual=" + SMALLER_BROJ);
    }

    @Test
    @Transactional
    void getAllProbasByBrojIsLessThanSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where broj is less than DEFAULT_BROJ
        defaultProbaShouldNotBeFound("broj.lessThan=" + DEFAULT_BROJ);

        // Get all the probaList where broj is less than UPDATED_BROJ
        defaultProbaShouldBeFound("broj.lessThan=" + UPDATED_BROJ);
    }

    @Test
    @Transactional
    void getAllProbasByBrojIsGreaterThanSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where broj is greater than DEFAULT_BROJ
        defaultProbaShouldNotBeFound("broj.greaterThan=" + DEFAULT_BROJ);

        // Get all the probaList where broj is greater than SMALLER_BROJ
        defaultProbaShouldBeFound("broj.greaterThan=" + SMALLER_BROJ);
    }

    @Test
    @Transactional
    void getAllProbasByImeIsEqualToSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where ime equals to DEFAULT_IME
        defaultProbaShouldBeFound("ime.equals=" + DEFAULT_IME);

        // Get all the probaList where ime equals to UPDATED_IME
        defaultProbaShouldNotBeFound("ime.equals=" + UPDATED_IME);
    }

    @Test
    @Transactional
    void getAllProbasByImeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where ime not equals to DEFAULT_IME
        defaultProbaShouldNotBeFound("ime.notEquals=" + DEFAULT_IME);

        // Get all the probaList where ime not equals to UPDATED_IME
        defaultProbaShouldBeFound("ime.notEquals=" + UPDATED_IME);
    }

    @Test
    @Transactional
    void getAllProbasByImeIsInShouldWork() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where ime in DEFAULT_IME or UPDATED_IME
        defaultProbaShouldBeFound("ime.in=" + DEFAULT_IME + "," + UPDATED_IME);

        // Get all the probaList where ime equals to UPDATED_IME
        defaultProbaShouldNotBeFound("ime.in=" + UPDATED_IME);
    }

    @Test
    @Transactional
    void getAllProbasByImeIsNullOrNotNull() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where ime is not null
        defaultProbaShouldBeFound("ime.specified=true");

        // Get all the probaList where ime is null
        defaultProbaShouldNotBeFound("ime.specified=false");
    }

    @Test
    @Transactional
    void getAllProbasByImeContainsSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where ime contains DEFAULT_IME
        defaultProbaShouldBeFound("ime.contains=" + DEFAULT_IME);

        // Get all the probaList where ime contains UPDATED_IME
        defaultProbaShouldNotBeFound("ime.contains=" + UPDATED_IME);
    }

    @Test
    @Transactional
    void getAllProbasByImeNotContainsSomething() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        // Get all the probaList where ime does not contain DEFAULT_IME
        defaultProbaShouldNotBeFound("ime.doesNotContain=" + DEFAULT_IME);

        // Get all the probaList where ime does not contain UPDATED_IME
        defaultProbaShouldBeFound("ime.doesNotContain=" + UPDATED_IME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProbaShouldBeFound(String filter) throws Exception {
        restProbaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proba.getId().intValue())))
            .andExpect(jsonPath("$.[*].broj").value(hasItem(DEFAULT_BROJ)))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)));

        // Check, that the count call also returns 1
        restProbaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProbaShouldNotBeFound(String filter) throws Exception {
        restProbaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProbaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProba() throws Exception {
        // Get the proba
        restProbaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProba() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        int databaseSizeBeforeUpdate = probaRepository.findAll().size();

        // Update the proba
        Proba updatedProba = probaRepository.findById(proba.getId()).get();
        // Disconnect from session so that the updates on updatedProba are not directly saved in db
        em.detach(updatedProba);
        updatedProba.broj(UPDATED_BROJ).ime(UPDATED_IME);

        restProbaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProba.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProba))
            )
            .andExpect(status().isOk());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
        Proba testProba = probaList.get(probaList.size() - 1);
        assertThat(testProba.getBroj()).isEqualTo(UPDATED_BROJ);
        assertThat(testProba.getIme()).isEqualTo(UPDATED_IME);
    }

    @Test
    @Transactional
    void putNonExistingProba() throws Exception {
        int databaseSizeBeforeUpdate = probaRepository.findAll().size();
        proba.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProbaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proba.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proba))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProba() throws Exception {
        int databaseSizeBeforeUpdate = probaRepository.findAll().size();
        proba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProbaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proba))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProba() throws Exception {
        int databaseSizeBeforeUpdate = probaRepository.findAll().size();
        proba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProbaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proba)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProbaWithPatch() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        int databaseSizeBeforeUpdate = probaRepository.findAll().size();

        // Update the proba using partial update
        Proba partialUpdatedProba = new Proba();
        partialUpdatedProba.setId(proba.getId());

        partialUpdatedProba.broj(UPDATED_BROJ).ime(UPDATED_IME);

        restProbaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProba))
            )
            .andExpect(status().isOk());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
        Proba testProba = probaList.get(probaList.size() - 1);
        assertThat(testProba.getBroj()).isEqualTo(UPDATED_BROJ);
        assertThat(testProba.getIme()).isEqualTo(UPDATED_IME);
    }

    @Test
    @Transactional
    void fullUpdateProbaWithPatch() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        int databaseSizeBeforeUpdate = probaRepository.findAll().size();

        // Update the proba using partial update
        Proba partialUpdatedProba = new Proba();
        partialUpdatedProba.setId(proba.getId());

        partialUpdatedProba.broj(UPDATED_BROJ).ime(UPDATED_IME);

        restProbaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProba))
            )
            .andExpect(status().isOk());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
        Proba testProba = probaList.get(probaList.size() - 1);
        assertThat(testProba.getBroj()).isEqualTo(UPDATED_BROJ);
        assertThat(testProba.getIme()).isEqualTo(UPDATED_IME);
    }

    @Test
    @Transactional
    void patchNonExistingProba() throws Exception {
        int databaseSizeBeforeUpdate = probaRepository.findAll().size();
        proba.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProbaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proba))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProba() throws Exception {
        int databaseSizeBeforeUpdate = probaRepository.findAll().size();
        proba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProbaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proba))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProba() throws Exception {
        int databaseSizeBeforeUpdate = probaRepository.findAll().size();
        proba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProbaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proba)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proba in the database
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProba() throws Exception {
        // Initialize the database
        probaRepository.saveAndFlush(proba);

        int databaseSizeBeforeDelete = probaRepository.findAll().size();

        // Delete the proba
        restProbaMockMvc
            .perform(delete(ENTITY_API_URL_ID, proba.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proba> probaList = probaRepository.findAll();
        assertThat(probaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
