package domain;

import java.io.Serializable;

public class Rezultat extends Entity<Long> implements Serializable {

    private Proba proba;
    private Integer punctajtotal;
    private Participant participant;

    public Rezultat(Proba proba, Integer punctajtotal, Participant participant) {
        this.proba = proba;
        this.punctajtotal = punctajtotal;
        this.participant = participant;
    }

    public Proba getProba() {
        return proba;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    public Integer getPunctajtotal() {
        return punctajtotal;
    }

    public void setPunctajtotal(Integer punctajtotal) {
        this.punctajtotal = punctajtotal;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
