package com.bwapp.culpeper.model;

import javax.persistence.Entity;

@Entity
public class Resource extends GenericEntity {
    private String path;

    public Resource() {
    }

    public Resource(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
