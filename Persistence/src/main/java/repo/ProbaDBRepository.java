package repo;


import domain.Proba;
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

public class ProbaDBRepository implements ProbaRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    public ProbaDBRepository(Properties props)
    {
        logger.info("Initializing ProbaDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Iterable<Proba> findAll()
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        List<Proba> probe=new ArrayList<>();
        try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Proba"))
        {
            try(ResultSet resultSet=preStmt.executeQuery())
            {
                while(resultSet.next())
                {
                    Long id=Long.valueOf(resultSet.getInt("ID_proba"));
                    String nume=resultSet.getString("nume");
                     Proba p=new Proba(nume);
                    p.setId(id);
                    probe.add(p);
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
        return probe;

    }
    @Override
    public Proba findOne(Long id)
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        Proba p=null;
        try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Proba WHERE ID_proba=?"))
        {
            int id1= (int)(long)id;
            preStmt.setInt(1,id1);
            try(ResultSet resultSet=preStmt.executeQuery())
            {
                while(resultSet.next())
                {
                    Long id2=Long.valueOf(resultSet.getInt("ID_proba"));
                    String nume=resultSet.getString("nume");
                    p=new Proba(nume);
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
    @Override
    public void save(Proba entity)
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("INSERT INTO Proba(nume) values(?)")){
            preStmt.setString(1,entity.getNume());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} proba", result);
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
        try(PreparedStatement preStmt = connection.prepareStatement("DELETE FROM Proba WHERE ID_proba=?")) {
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
    public void update(Proba entity)
    {
        logger.traceEntry("update proba {}",entity);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("UPDATE Proba SET nume=? WHERE ID_proba=?")) {
            preStmt.setString(1,entity.getNume());
            int result=preStmt.executeUpdate();
            logger.trace("Update {} instances",result);

        }catch(SQLException exception)
        {
            logger.error(exception);
            System.out.println("Error DB"+exception);
        }
    }


}
