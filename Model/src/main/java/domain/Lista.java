package domain;
import java.io.Serializable;
public class Lista extends MEntity<Long> implements Serializable{
    private Rezultat r;
    private Participant participant;

    public Rezultat getR() {
        return r;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setR(Rezultat r) {
        this.r = r;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Lista(Rezultat r, Participant participant) {
        this.r = r;
        this.participant = participant;
    }
}
