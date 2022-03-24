package org.example.issuetracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

@Data
@Entity(name = "TACHE")
@AllArgsConstructor
@NoArgsConstructor
public class Tache {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String title;
    private String owner;
    private Date created;
    private Integer effort;
    private Date completionDate;
    @Enumerated(EnumType.STRING)
    private TacheStatus status;
}
