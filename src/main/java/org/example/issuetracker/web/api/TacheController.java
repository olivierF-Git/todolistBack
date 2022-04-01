package org.example.issuetracker.web.api;

import org.example.issuetracker.configuration.exception.ResourceNotFoundException;
import org.example.issuetracker.model.Tache;
import org.example.issuetracker.model.TacheStatus;
import org.example.issuetracker.service.TacheService;
import org.example.issuetracker.web.dto.TacheDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api")
public class TacheController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    static final String ERROR = "Unexpected Exception caught.";


    /**
     * Retourne la liste des taches
     */
    @GetMapping(value = "/issues", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TacheDto> getAlltaches(@RequestParam(required = false, value = "status") final String status,
                                                 @RequestParam(required = false, value = "effort_gte") final Integer effortGte,
                                                 @RequestParam(required = false, value = "effort_lte") final Integer effortLte) {
        TacheDto tacheDto = new TacheDto();
        try {
            List<Tache> taches = tacheService.findAll();

            if (status != null) {
                logger.info("> getAllTaches with status : " + status);
                taches = taches.stream().filter(getTacheWithStatus(status)).collect(toList());
            }
            if (effortLte != null) {
                logger.info("> getAllTaches with effort lower than : " + effortLte);
                taches = taches.stream().filter(getTacheWithEffortLowerThan(effortLte)).collect(toList());
            }
            if (effortGte != null) {
                logger.info("> getAllTaches with effort greater than : " + effortGte);
                taches = taches.stream().filter(getTacheWithEffortGreaterThan(effortGte)).collect(toList());
            }

            tacheDto.setRecords(taches);
            tacheDto.getMetadata().setTotalCount(taches.size());
        } catch (Exception e) {
            logger.error(ERROR, e);
            return new ResponseEntity<>(tacheDto, INTERNAL_SERVER_ERROR);
        }

        logger.info("< getAllTaches");
        return new ResponseEntity<>(tacheDto, OK);
    }

    private Predicate<Tache> getTacheWithStatus(String status) {
        return tache -> tache.getStatus().equals(TacheStatus.fromStatus(status));
    }

    private Predicate<Tache> getTacheWithEffortLowerThan(Integer effortLte) {
        return tache -> tache.getEffort() < effortLte;
    }

    private Predicate<Tache> getTacheWithEffortGreaterThan(Integer effortGte) {
        return tache -> tache.getEffort() > effortGte;
    }


    @GetMapping(value = "/issues/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public TacheStatus[] getAllStatus() {
        return TacheStatus.values();
    }

    /**
     * Récupère une question grâce à son id
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/issues/{id}")
    public ResponseEntity getTache(@PathVariable("id") UUID id) {
        logger.info("> getTache with id : " + id);

        verifyTache(id);
        Optional<Tache> tache = tacheService.find(id);

        return new ResponseEntity<>(tache, OK);
    }


    /**
     * Ajoute une question.
     *
     * @param tache
     * @return
     */
    @PostMapping(value = "/issues", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Tache> createTache(@RequestBody Tache tache) {
        logger.info("> createTache");

        Tache createdTache;
        try {
            createdTache = tacheService.create(tache);
        } catch (Exception e) {
            logger.error(ERROR, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

        logger.info("< createTache");
        return new ResponseEntity<>(createdTache, HttpStatus.CREATED);
    }

    @PutMapping(value = "/issues/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Tache> updateTache(@RequestBody Tache tache) {
        logger.info("> updateTache");

        Tache updatedTache;
        try {
            verifyTache(tache.getId());
            updatedTache = tacheService.update(tache);
        } catch (Exception e) {
            logger.error(ERROR, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

        logger.info("< updateTache");
        return new ResponseEntity<>(updatedTache, OK);
    }

    @DeleteMapping(value = "/issues/{id}")
    public ResponseEntity<Tache> deleteTache(@PathVariable("id") UUID tacheId) {
        logger.info("> deleteTache");

        try {
            verifyTache(tacheId);
            tacheService.delete(tacheId);
        } catch (Exception e) {
            logger.error(ERROR, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

        logger.info("< deleteTache");
        return new ResponseEntity<>(NO_CONTENT);
    }

    // if no tache found, return 404 status code
    protected void verifyTache(UUID tacheId) throws ResourceNotFoundException {
        tacheService.find(tacheId)
                .orElseThrow(() -> new ResourceNotFoundException("Tache with id " + tacheId + " not found"));
    }
}
