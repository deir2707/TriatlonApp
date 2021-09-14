package repo.Hibernate;

import domain.Participant;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repo.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;


public class ParticipantHbnRepository implements ParticipantRepository
{

    private static SessionFactory sessionFactory;

    public ParticipantHbnRepository(SessionFactory sessionFactory)
    {
        ParticipantHbnRepository.sessionFactory = sessionFactory;
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }





    @Override
    public Participant findOne(Long aLong) {

        Participant participant = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participant = session.createQuery("FROM Participant WHERE id = "+aLong, Participant.class).setMaxResults(1).uniqueResult();
                tx.commit();

            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return participant;
    }

    @Override
    public Iterable<Participant> findAll()
    {
        List<Participant> participants = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participants = session.createQuery(
                        "FROM Participant", Participant.class).list();
                System.out.println( participants.size() + " participants(s) found:" );
                tx.commit();

            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return participants;
    }




    @Override
    public void save(Participant entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(Long aLong)
    {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                Participant participant= session.createQuery("FROM Participant WHERE id="+aLong, Participant.class).setMaxResults(1).uniqueResult();
                System.out.println("Delete the participant"+participant);
                session.delete(participant);
                tx.commit();
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public Participant update(Participant entity) {


        Participant participant = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participant = session.createQuery("FROM Participant WHERE id = "+entity.getId(), Participant.class).setMaxResults(1).uniqueResult();
                participant.setPunctajtotal(entity.getPunctajtotal());
                participant.setNume(entity.getNume());
                tx.commit();

            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return participant;


    }


    @Override
    public Iterable<Participant> findAllAlfabetic()
    {
        List<Participant> participants = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participants = session.createQuery("FROM Participant ORDER BY nume", Participant.class).list();
                System.out.println( participants.size() + " participant(s) found:" );
                tx.commit();

            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return participants;
    }

}
