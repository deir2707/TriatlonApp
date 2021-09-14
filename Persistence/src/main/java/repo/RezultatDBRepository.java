package repo;

import domain.Participant;
import domain.Proba;
import domain.Rezultat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repo.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RezultatDBRepository implements RezultatRepository {
    private JdbcUtils dbUtils;
    private ProbaRepository repoproba;
    private ParticipantRepository repopart;
    private static final Logger logger= LogManager.getLogger();
    public RezultatDBRepository(Properties props, ProbaRepository repoproba1,ParticipantRepository repopart1)
    {
        logger.info("Initializing RezultatDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        repoproba=repoproba1;
        repopart=repopart1;

    }
    @Override
    public Iterable<Rezultat> findAll()
    {
        {
            logger.traceEntry();
            Connection connection=dbUtils.getConnection();
            List<Rezultat> rezultate=new ArrayList<>();
            try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Rezultat"))
            {
                try(ResultSet resultSet=preStmt.executeQuery())
                {
                    while(resultSet.next())
                    {
                        Long id=Long.valueOf(resultSet.getInt("ID_rezultat"));
                        Long id_proba=Long.valueOf(resultSet.getInt("ID_proba"));
                        Integer punctajtotal=resultSet.getInt("punctajtotal");
                        Long id_participant=Long.valueOf(resultSet.getInt("ID_participant"));
                        Proba proba=repoproba.findOne(id_proba);
                        Participant participant=repopart.findOne(id_participant);
                        Rezultat r=new Rezultat(proba,punctajtotal,participant);
                        r.setId(id);
                        rezultate.add(r);
                    }

                }catch(SQLException exception)
                {
                    logger.error(exception);
                    System.out.println("Error DB"+exception);
                }
            }catch(SQLException exception)
            {
                logger.error(exception);
                System.out.println("Error DB"+exception);
            }
            logger.traceExit();
            return rezultate;

        }
    }
    @Override
    public Rezultat findOne(Long id)
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        Rezultat r=null;
        try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Rezultat WHERE ID_rezultat=?"))
        {
            int id1= (int)(long)id;
            preStmt.setInt(1,id1);
            try(ResultSet resultSet=preStmt.executeQuery())
            {
                while(resultSet.next())
                {
                    Long id2=Long.valueOf(resultSet.getInt("ID_rezultat"));
;
                    Long id_proba=Long.valueOf(resultSet.getInt("ID_proba"));
                    Integer punctajtotal=resultSet.getInt("punctajtotal");
                    Long id_participant=Long.valueOf(resultSet.getInt("ID_participant"));
                    Proba proba=repoproba.findOne(id_proba);
                    Participant participant=repopart.findOne(id_participant);
                    r=new Rezultat(proba,punctajtotal,participant);
                    r=new Rezultat(proba,punctajtotal,participant);
                    r.setId(id2);
                }

            }catch(SQLException exception)
            {
                logger.error(exception);
                System.out.println("Error DB"+exception);
            }
        }catch(SQLException exception)
        {
            logger.error(exception);
            System.out.println("Error DB"+exception);
        }
        logger.traceExit(r);
        return r;
    }
    @Override
    public void save(Rezultat entity)
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("INSERT INTO Rezultat(ID_proba,punctajtotal,ID_participant) values(?,?,?)")){
            int id1= (int)(long)entity.getProba().getId();
            preStmt.setInt(1,id1);
            int id2= (int)(long)entity.getParticipant().getId();
            preStmt.setInt(3,id2);
            preStmt.setInt(2,entity.getPunctajtotal());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} rezultat", result);
        }catch(SQLException exception)
        {
            logger.error(exception);
            System.out.println("Error DB"+exception);
        }
    }
    @Override
    public void delete(Long id)
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("DELETE FROM Rezultat WHERE ID_rezultat=?")) {
            int id1= (int)(long)id;
            preStmt.setInt(1,id1);
            int result=preStmt.executeUpdate();
            logger.trace("Deleted {} instances",result);
        }catch(SQLException exception)
        {
            logger.error(exception);
            System.out.println("Error DB"+exception);
        }
    }
    @Override
    public Rezultat  update(Rezultat entity)
    {
        Rezultat r=null;
        logger.traceEntry("update rezultat {}",entity);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("UPDATE Rezultat SET ID_proba=?,punctajtotal=?,ID_participant=? WHERE ID_arbitru=?")) {
            int id1= (int)(long)entity.getProba().getId();
            preStmt.setInt(1,id1);
            int id2= (int)(long)entity.getParticipant().getId();
            preStmt.setInt(3,id2);
            preStmt.setInt(2,entity.getPunctajtotal());
            int result=preStmt.executeUpdate();
            logger.trace("Update {} instances",result);

        }catch(SQLException exception)
        {
            logger.error(exception);
            System.out.println("Error DB"+exception);

        }
        return r;
    }

    }

