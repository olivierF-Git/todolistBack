package org.example.issuetracker.model;

import java.util.Arrays;

public enum TacheStatus {
    NEW("New"),
    OPEN("Open"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String tache;

    TacheStatus(String status) {
        this.tache = status;
    }

    public static TacheStatus fromStatus(String status) {
        return Arrays.stream(TacheStatus.values())
                .filter(s -> s.tache.equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Le status " + status + " est inconnu dans TacheStatus"));
    }

}
