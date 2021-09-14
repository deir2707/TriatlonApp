package domain;

import java.io.Serializable;

public class Arbitru extends Entity<Long> implements Serializable {
    private String nume;
    private String user;
    private String pw;
    private Proba proba;

    public Arbitru(String nume, String user, String pw, Proba proba) {
        this.nume = nume;
        this.user = user;
        this.pw = pw;
        this.proba = proba;
    }
    public Arbitru(String user,String pw){
        this.user=user;
        this.pw=pw;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public Proba getProba() {
        return proba;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }
}
