package com.bwapp.culpeper.model;

import javax.persistence.Entity;

@Entity
public class Ailment extends GenericEntity {

    private String ailmentName;
    private String bodyPart;

    public Ailment() {
    }

    public Ailment(String ailmentName, String bodyPart) {
        this.ailmentName = ailmentName;
        this.bodyPart = bodyPart;
    }

    public String getAilmentName() {
        return ailmentName;
    }

    public void setAilmentName(String ailmentName) {
        this.ailmentName = ailmentName;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }
}
