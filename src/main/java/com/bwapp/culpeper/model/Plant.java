package com.bwapp.culpeper.model;

import javax.persistence.Entity;

@Entity
public class Plant extends GenericEntity{

    private String latinName;
    private String commonName;
    private String description;

    public Plant() {
    }

    public Plant(String latinName, String commonName, String description) {
        this.latinName = latinName;
        this.commonName = commonName;
        this.description = description;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
