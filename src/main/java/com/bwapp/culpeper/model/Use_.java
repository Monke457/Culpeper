package com.bwapp.culpeper.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "use_")
public class Use_ extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    private String part;
    private String direction;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "use_ailment",
            joinColumns = @JoinColumn(name = "use_id"),
            inverseJoinColumns = @JoinColumn(name = "ailment_id"))
    private List<Ailment> ailments;

    public Use_() {
    }

    public Use_(Plant plant, String part, String direction, List<Ailment> ailments) {
        this.plant = plant;
        this.part = part;
        this.direction = direction;
        this.ailments = ailments;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Ailment> getAilments() {
        return ailments;
    }

    public void setAilments(List<Ailment> ailments) {
        this.ailments = ailments;
    }
}
