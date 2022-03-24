package org.example.issuetracker.repository;

import org.example.issuetracker.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TacheRepository extends JpaRepository<Tache, UUID> {

}
