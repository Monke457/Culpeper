package com.bwapp.culpeper.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Ailment extends GenericEntity {

    private String ailmentName;
    private String bodyPart;
    private String virtue;

    @ManyToMany(mappedBy = "ailments")
    private List<Use_> uses;

    public Ailment() {
    }

    public Ailment(String ailmentName, String bodyPart, String virtue, List<Use_> uses) {
        this.ailmentName = ailmentName;
        this.bodyPart = bodyPart;
        this.virtue = virtue;
        this.uses = uses;
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

    public String getVirtue() {
        return virtue;
    }

    public void setVirtue(String virtue) {
        this.virtue = virtue;
    }

    public List<Use_> getUses() {
        return uses;
    }

    public void setUses(List<Use_> uses) {
        this.uses = uses;
    }
}
