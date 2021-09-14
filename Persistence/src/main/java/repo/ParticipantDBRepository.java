package repo;

import domain.Participant;
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

public class ParticipantDBRepository implements ParticipantRepository{
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    public ParticipantDBRepository(Properties props)
    {
        logger.info("Initializing ParticipantDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Iterable<Participant> findAll()
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        List<Participant> participanti=new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Participant"))
        {
            try(ResultSet resultSet=preStmt.executeQuery())
            {
                while(resultSet.next())
                {
                    Long id=Long.valueOf(resultSet.getInt("ID_participant"));
                    String nume=resultSet.getString("nume");
                    Integer punctajtotal=resultSet.getInt("punctajtotal");
                    Participant p=new Participant(nume,punctajtotal);
                    p.setId(id);
                    participanti.add(p);
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
        return participanti;

    }
    @Override
    public void save(Participant entity)
    {
        logger.traceEntry("save participant {}");
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("INSERT INTO Participant(nume,punctajtotal) values(?,?)")){
            preStmt.setString(1,entity.getNume());
            preStmt.setInt(2,entity.getPunctajtotal());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} participant", result);
        }catch(SQLException exception)
        {
            logger.error(exception);
            System.out.println("Error DB"+exception);
        }

    }

    @Override
    public void delete(Long id){
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("DELETE FROM Participant WHERE ID_participant=?")) {
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
    public void update(Participant entity)
    {
        logger.traceEntry("update participant {}",entity);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("UPDATE Participant SET nume=?,punctajtotal=? WHERE ID_participant=?")) {
            preStmt.setString(1,entity.getNume());
            preStmt.setInt(2,entity.getPunctajtotal());
            int id1= (int)(long)entity.getId();
            preStmt.setInt(3,id1);
            int result=preStmt.executeUpdate();
            logger.trace("Update {} instances",result);

        }catch(SQLException exception)
        {
            logger.error(exception);
            System.out.println("Error DB"+exception);
        }

    }
    @Override
    public Participant findOne(Long id)
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        Participant p=null;
        try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Participant WHERE ID_participant=?"))
        {
            int id1= (int)(long)id;
            preStmt.setInt(1,id1);
            try(ResultSet resultSet=preStmt.executeQuery())
            {
                while(resultSet.next())
                {
                    Long id2=Long.valueOf(resultSet.getInt("ID_participant"));
                    String nume=resultSet.getString("nume");
                    Integer punctajtotal=resultSet.getInt("punctajtotal");
                    p=new Participant(nume,punctajtotal);
                    p.setId(id2);
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
        logger.traceExit(p);
        return p;

    }
    public Iterable<Participant> findAllAlfabetic()
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        List<Participant> participanti=new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Participant ORDER BY nume ASC"))
        {
            try(ResultSet resultSet=preStmt.executeQuery())
            {
                while(resultSet.next())
                {
                    Long id=Long.valueOf(resultSet.getInt("ID_participant"));
                    String nume=resultSet.getString("nume");
                    Integer punctajtotal=resultSet.getInt("punctajtotal");
                    Participant p=new Participant(nume,punctajtotal);
                    p.setId(id);
                    participanti.add(p);
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
        return participanti;

    }

}
