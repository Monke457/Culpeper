package com.bwapp.culpeper.model;

import javax.persistence.Entity;

@Entity
public class Plant extends GenericEntity {

    private String latinName;
    private String commonName;
    private String description;
    private String place;
    private String time;
    private String imgPath;
    private String government;
    private String virtue;
    private String otherNames;

    public Plant() {
    }

    public Plant(String latinName, String commonName, String description, String place, String time, String imgPath, String government, String virtue, String otherNames) {
        this.latinName = latinName;
        this.commonName = commonName;
        this.description = description;
        this.place = place;
        this.time = time;
        this.imgPath = imgPath;
        this.government = government;
        this.virtue = virtue;
        this.otherNames = otherNames;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getGovernment() {
        return government;
    }

    public void setGovernment(String government) {
        this.government = government;
    }

    public String getVirtue() {
        return virtue;
    }

    public void setVirtue(String virtue) {
        this.virtue = virtue;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }
}
