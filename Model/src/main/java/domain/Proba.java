package domain;

import java.io.Serializable;

public class Proba extends Entity<Long> implements Serializable {
    private String nume;


    public String getNume() {
        return nume;
    }

    public Proba(String nume) {
        this.nume = nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}
