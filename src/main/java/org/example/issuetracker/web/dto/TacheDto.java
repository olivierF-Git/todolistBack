package org.example.issuetracker.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.issuetracker.model.Tache;

import java.util.ArrayList;
import java.util.List;

public class TacheDto {

    @JsonProperty("_metadata")
    private TacheMetadata metadata;
    private List<Tache> records;

    public TacheDto() {
        this.records = new ArrayList<>();
        this.metadata = new TacheMetadata();
    }

    public TacheMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TacheMetadata metadata) {
        this.metadata = metadata;
    }

    public List<Tache> getRecords() {
        return records;
    }

    public void setRecords(List<Tache> records) {
        this.records = records;
    }
}
