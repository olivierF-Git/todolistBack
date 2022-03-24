package org.example.issuetracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

import com.github.javafaker.Faker;
import org.example.issuetracker.model.Tache;
import org.example.issuetracker.model.TacheStatus;
import org.example.issuetracker.repository.TacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    final
    ConfigurableApplicationContext context;

    public Application(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void run(String... args) {
        TacheRepository repository = context.getBean(TacheRepository.class);
        repository.deleteAll();
        log.info("> Inserting new data...");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Faker faker = new Faker();

            for (int i = 0; i < 10; i++) {
                Tache fakeTache = new Tache(
                        UUID.randomUUID(),
                        faker.lorem().sentence(10),
                        faker.name().fullName(),
                        faker.date().birthday(),
                        faker.number().numberBetween(1, 8),
                        null,
                        TacheStatus.values()[new Random().nextInt(TacheStatus.values().length)]);

                if (fakeTache.getStatus() == TacheStatus.DONE) {
                    if (fakeTache.getCompletionDate() == null) {
                        fakeTache.setCompletionDate(faker.date().birthday());
                    }
                    while (fakeTache.getCompletionDate().before(fakeTache.getCreated())) {
                        fakeTache.setCompletionDate(faker.date().birthday());
                    }

                }
                repository.save(fakeTache);
            }
            Tache tache1 = new Tache();
            tache1.setStatus(TacheStatus.IN_PROGRESS);
            tache1.setOwner("Guillaume");
            tache1.setCreated(df.parse("2017-09-29"));
            tache1.setEffort(1);
            tache1.setTitle("Tester le générateur d'appli FUN");
            repository.save(tache1);

            Tache tache2 = new Tache();
            tache2.setStatus(TacheStatus.OPEN);
            tache2.setOwner("Guillaume");
            tache2.setCreated(df.parse("2017-10-02"));
            tache2.setEffort(1);
            tache2.setCompletionDate(df.parse("2017-10-03"));
            tache2.setTitle("Importer le projet Admin REFCG dans SVN");
            repository.save(tache2);

            Tache tache3 = new Tache();
            tache3.setStatus(TacheStatus.ASSIGNED);
            tache3.setOwner("Georges");
            tache3.setCreated(df.parse("2017-10-02"));
            tache3.setEffort(5);
            tache3.setCompletionDate(df.parse("2017-10-06"));
            tache3.setTitle("Réaliser une maquette de l'appli en React");
            repository.save(tache3);

            Tache tache4 = new Tache();
            tache4.setStatus(TacheStatus.ASSIGNED);
            tache4.setOwner("Georges");
            tache4.setCreated(df.parse("2017-10-03"));
            tache4.setEffort(3);
            tache4.setCompletionDate(df.parse("2017-10-06"));
            tache4.setTitle("Récupérer le generator-funapp à jour");
            repository.save(tache4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}