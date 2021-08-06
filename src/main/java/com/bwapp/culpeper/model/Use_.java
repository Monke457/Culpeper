package com.bwapp.culpeper.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Comparator;

@Entity
@Table(name = "use_")
public class Use_ extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    @ManyToOne
    @JoinColumn(name = "ailment_id")
    private Ailment ailment;

    private String direction;

    public Use_() {
    }

    public Use_(Plant plant, Ailment ailment, String direction) {
        this.plant = plant;
        this.ailment = ailment;
        this.direction = direction;
    }

    public Use_(Plant plant, Part part, Ailment ailment, String direction) {
        this.plant = plant;
        this.part = part;
        this.ailment = ailment;
        this.direction = direction;
    }

    public Ailment getAilment() {
        return ailment;
    }

    public void setAilment(Ailment ailment) {
        this.ailment = ailment;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
