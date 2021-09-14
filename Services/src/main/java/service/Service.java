package service;

import domain.Arbitru;
import domain.Participant;
import domain.Rezultat;
import repo.ArbitruRepository;
import repo.ParticipantRepository;
import repo.ProbaRepository;
import repo.RezultatRepository;


import java.util.ArrayList;
import java.util.List;


public class Service implements IService {
    private ArbitruRepository repoarbitru;
    private RezultatRepository reporezultat;
    private ProbaRepository repoproba;
    private ParticipantRepository repopart;
    List<Observer> observers=new ArrayList<>();
    @Override
    public void add_observer(Observer o)
    {
        if(o==null) throw new IllegalArgumentException("Observer cannot be null");
        observers.add(o);
    }
    @Override
    public void notify_observer(){
        observers.forEach(Observer::execute_update);
    }
    @Override
    public void remove_observers(){observers.clear();}

    @Override
    public void remove_observer(Observer o){
        observers.remove(o);
    }

    public Service(ArbitruRepository repoarbitru, RezultatRepository reporezultat, ProbaRepository repoproba, ParticipantRepository repopart) {
        this.repoarbitru = repoarbitru;
        this.reporezultat = reporezultat;
        this.repoproba = repoproba;
        this.repopart = repopart;
    }

    public Arbitru login(String user,String pw)
    {
        return repoarbitru.findUserPw(user,pw);

    }

    public Iterable<Participant> getAllParticipantiOrdonati(){
        return repopart.findAllAlfabetic();
    }

    public Participant findOnePart(Long id){
        return repopart.findOne(id);
    }

    public void AdaugaRezUpPart(Rezultat r, Participant p){
        reporezultat.save(r);
        repopart.update(p);
        notify_observer();
    }

    public Iterable<Participant> getParticipantiMe(Arbitru a){
        List<Participant> participanti=new ArrayList<>();
        Iterable<Rezultat> rezultats=reporezultat.findAll();
        for(Rezultat r :rezultats){
            if(r.getProba().getId().equals(a.getProba().getId())){
                Participant p=repopart.findOne(r.getParticipant().getId());
                Participant p2=new Participant(p.getNume(),r.getPunctajtotal());
                p2.setId(p.getId());
                participanti.add(p2);
            }
        }
        for(Participant p3:participanti){
            for(Participant p4:participanti)
                if(p3.getPunctajtotal()>p4.getPunctajtotal()){
                    Participant aux=new Participant(p3.getNume(), p3.getPunctajtotal());
                    aux.setId(p3.getId());
                    p3.setId(p4.getId());
                    p3.setPunctajtotal(p4.getPunctajtotal());
                    p3.setNume(p4.getNume());
                    p4.setId(aux.getId());
                    p4.setPunctajtotal(aux.getPunctajtotal());
                    p4.setNume(aux.getNume());
                }

        }
        return participanti;
    }

}
