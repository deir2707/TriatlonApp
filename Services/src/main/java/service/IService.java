package service;

import domain.Arbitru;
import domain.Participant;
import domain.Rezultat;

public interface IService extends Observable {

    public Arbitru login(String user, String pw);
    public Iterable<Participant> getAllParticipantiOrdonati();
    public Participant findOnePart(Long id);
    public void AdaugaRezUpPart(Rezultat r, Participant p);
    public Iterable<Participant> getParticipantiMe(Arbitru a);

}
