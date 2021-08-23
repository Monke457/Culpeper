package com.bwapp.culpeper.model;

import javax.persistence.Entity;
import java.sql.Blob;

@Entity
public class Resource extends GenericEntity {
    private String fileName;
    private Blob file;

    public Resource() {
    }

    public Resource(String fileName, Blob file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }
}
