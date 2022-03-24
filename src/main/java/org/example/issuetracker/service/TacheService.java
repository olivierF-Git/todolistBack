package org.example.issuetracker.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.example.issuetracker.model.Tache;
import org.example.issuetracker.repository.TacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class TacheService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private TacheRepository tacheRepository;
    private MeterRegistry counterService;

    public TacheService(TacheRepository tacheRepository,
                        MeterRegistry counterService) {
        this.tacheRepository = tacheRepository;
        this.counterService = counterService;
    }

    /**
     * Search the issue data repository for all Issue entities.
     * @return A List of Issue entities or null if none found.
     */
    public List<Tache> findAll() {
        logger.info("> findAll");

        counterService.counter("services.issueservice.findAll.invoked");


        List<Tache> taches = tacheRepository.findAll();

        logger.info("< findAll");
        return taches;
    }

    /**
     * Search the issue data repository for a single Issue entity by the primary
     * key identifier.
     * @param id An issue primary key identifier.
     * @return An Issue entity or null if not found.
     */
    public Optional<Tache> find(UUID id) {
        logger.info("> find id:{}", id);

        counterService.counter("services.issueservice.find.invoked");

        Optional<Tache> issue = tacheRepository.findById(id);

        logger.info("< find id:{}", id);
        return issue;
    }

    /**
     * Create a new Issue entity in the data repository.
     * @param tache An issue entity to persist.
     * @return The persisted issue entity.
     */
    public Tache create(Tache tache) {
        logger.info("> create");

        counterService.counter("services.issueservice.create.invoked");

        Tache persistedTache = tacheRepository.save(tache);

        logger.info("< create");
        return persistedTache;
    }

    /**
     * Update an Issue entity in the data repository.
     * @param tache An issue entity to update.
     * @return The updated issue entity.
     */
    public Tache update(Tache tache) {
        logger.info("> update");

        counterService.counter("services.issueservice.update.invoked");

        Tache updatedTache = tacheRepository.save(tache);

        logger.info("< update");
        return updatedTache;
    }

    /**
     * Delete an Issue entity from the data repository.
     * @param id The primary key identifier of the issue to delete.
     */
    public void delete(UUID id) {
        logger.info("> delete");

        counterService.counter("services.issueservice.delete.invoked");

        tacheRepository.deleteById(id);

        logger.info("< delete");
    }

}
