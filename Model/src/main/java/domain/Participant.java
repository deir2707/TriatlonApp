package domain;

import java.io.Serializable;
import javax.persistence.*;
@Entity
public class Participant extends MEntity<Long> implements Serializable {
    private String nume;
    private Integer punctajtotal;

    public Participant(String nume, Integer punctajtotal) {
        this.nume = nume;
        this.punctajtotal = punctajtotal;
    }

    public Participant()
    {
        // this form used by Hibernate
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
    @Override
    public String toString() {
        return "Participant{" +
                "nume='" + nume + '\'' +
                ", punctajtotal='" + punctajtotal + '\'' +
                '}';
    }
}
