package domain;

import java.io.Serializable;

public class Participant extends Entity<Long> implements Serializable {
    private String nume;
    private Integer punctajtotal;

    public Participant(String nume, Integer punctajtotal) {
        this.nume = nume;
        this.punctajtotal = punctajtotal;
    }

    public String getNume() {
        return nume;
    }

    public Integer getPunctajtotal() {
        return punctajtotal;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPunctajtotal(Integer punctajtotal) {
        this.punctajtotal = punctajtotal;
    }

}
