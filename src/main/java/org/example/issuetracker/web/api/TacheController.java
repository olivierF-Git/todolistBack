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
     * Retourne la liste des issues
     */
    @GetMapping(value = "/issues", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TacheDto> getAllIssues(@RequestParam(required = false, value = "status") final String status,
                                                 @RequestParam(required = false, value = "effort_gte") final Integer effortGte,
                                                 @RequestParam(required = false, value = "effort_lte") final Integer effortLte) {
        TacheDto tacheDto = new TacheDto();
        try {
            List<Tache> taches = tacheService.findAll();

            if (status != null) {
                logger.info("> getAllIssues with status : " + status);
                taches = taches.stream().filter(getIssueWithStatus(status)).collect(toList());
            }
            if (effortLte != null) {
                logger.info("> getAllIssues with effort lower than : " + effortLte);
                taches = taches.stream().filter(getIssueWithEffortLowerThan(effortLte)).collect(toList());
            }
            if (effortGte != null) {
                logger.info("> getAllIssues with effort greater than : " + effortGte);
                taches = taches.stream().filter(getIssueWithEffortGreaterThan(effortGte)).collect(toList());
            }

            tacheDto.setRecords(taches);
            tacheDto.getMetadata().setTotalCount(taches.size());
        } catch (Exception e) {
            logger.error(ERROR, e);
            return new ResponseEntity<>(tacheDto, INTERNAL_SERVER_ERROR);
        }

        logger.info("< getAllIssues");
        return new ResponseEntity<>(tacheDto, OK);
    }

    private Predicate<Tache> getIssueWithStatus(String status) {
        return issue -> issue.getStatus().equals(TacheStatus.fromStatus(status));
    }

    private Predicate<Tache> getIssueWithEffortLowerThan(Integer effortLte) {
        return issue -> issue.getEffort() < effortLte;
    }

    private Predicate<Tache> getIssueWithEffortGreaterThan(Integer effortGte) {
        return issue -> issue.getEffort() > effortGte;
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
    public ResponseEntity getIssue(@PathVariable("id") UUID id) {
        logger.info("> getIssue with id : " + id);

        verifyIssue(id);
        Optional<Tache> issue = tacheService.find(id);

        return new ResponseEntity<>(issue, OK);
    }


    /**
     * Ajoute une question.
     *
     * @param tache
     * @return
     */
    @PostMapping(value = "/issues", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Tache> createIssue(@RequestBody Tache tache) {
        logger.info("> createIssue");

        Tache createdTache;
        try {
            createdTache = tacheService.create(tache);
        } catch (Exception e) {
            logger.error(ERROR, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

        logger.info("< createIssue");
        return new ResponseEntity<>(createdTache, HttpStatus.CREATED);
    }

    @PutMapping(value = "/issues/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Tache> updateIssue(@RequestBody Tache tache) {
        logger.info("> updateIssue");

        Tache updatedTache;
        try {
            verifyIssue(tache.getId());
            updatedTache = tacheService.update(tache);
        } catch (Exception e) {
            logger.error(ERROR, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

        logger.info("< updateIssue");
        return new ResponseEntity<>(updatedTache, OK);
    }

    @DeleteMapping(value = "/issues/{id}")
    public ResponseEntity<Tache> deleteIssue(@PathVariable("id") UUID issueId) {
        logger.info("> deleteIssue");

        try {
            verifyIssue(issueId);
            tacheService.delete(issueId);
        } catch (Exception e) {
            logger.error(ERROR, e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

        logger.info("< deleteIssue");
        return new ResponseEntity<>(NO_CONTENT);
    }

    // if no issue found, return 404 status code
    protected void verifyIssue(UUID issueId) throws ResourceNotFoundException {
        tacheService.find(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue with id " + issueId + " not found"));
    }
}
