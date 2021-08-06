package com.bwapp.culpeper.model;

import javax.persistence.Entity;

@Entity
public class Virtue extends GenericEntity {

    public enum Hot {ZERO, ONE, TWO, THREE, FOUR}
    public enum Cold {ZERO, ONE, TWO, THREE, FOUR}
    public enum Dry {ZERO, ONE, TWO, THREE, FOUR}
    public enum Moist {ZERO, ONE, TWO, THREE, FOUR}

    private Virtue.Hot hot;
    private Virtue.Cold cold;
    private Virtue.Dry dry;
    private Virtue.Moist moist;

    public Virtue() {
    }

    public Virtue(Virtue.Hot hot, Virtue.Cold cold, Virtue.Dry dry, Virtue.Moist moist) {
        this.hot = hot;
        this.cold = cold;
        this.dry = dry;
        this.moist = moist;
    }

    public Hot getHot() {
        return hot;
    }

    public void setHot(Hot hot) {
        this.hot = hot;
    }

    public Cold getCold() {
        return cold;
    }

    public void setCold(Cold cold) {
        this.cold = cold;
    }

    public Dry getDry() {
        return dry;
    }

    public void setDry(Dry dry) {
        this.dry = dry;
    }

    public Moist getMoist() {
        return moist;
    }

    public void setMoist(Moist moist) {
        this.moist = moist;
    }
}
