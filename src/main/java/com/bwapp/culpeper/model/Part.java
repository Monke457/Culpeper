package com.bwapp.culpeper.model;

import javax.persistence.Entity;

@Entity
public class Part extends GenericEntity {

    private String partName;

    public Part() {
    }

    public Part(String partName) {
        this.partName = partName;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }
}
