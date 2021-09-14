package repo;

import domain.Arbitru;

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

public class ArbitruDBRepository implements ArbitruRepository {
    private JdbcUtils dbUtils;
    private ProbaRepository repoproba;
    private static final Logger logger= LogManager.getLogger();
    public ArbitruDBRepository(Properties props,ProbaRepository repoproba1)
    {
        logger.info("Initializing ArbitruDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        repoproba=repoproba1;

    }

    @Override
    public Iterable<Arbitru> findAll()
    {
        {
            logger.traceEntry();
            Connection connection=dbUtils.getConnection();
            List<Arbitru> arbitri=new ArrayList<>();
            try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Arbitru"))
            {
                try(ResultSet resultSet=preStmt.executeQuery())
                {
                    while(resultSet.next())
                    {
                        Long id=Long.valueOf(resultSet.getInt("ID_arbitru"));
                        String nume=resultSet.getString("nume");
                        String user=resultSet.getString("user");
                        String pw=resultSet.getString("pw");
                        Long id_proba=Long.valueOf(resultSet.getInt("ID_proba"));
                        Proba proba=repoproba.findOne(id_proba);
                        Arbitru a=new Arbitru(nume,user,pw,proba);
                        a.setId(id);
                        arbitri.add(a);
                        int result=preStmt.executeUpdate();
                        logger.trace("Saved {} arbitru" +
                                "", result);
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
            return arbitri;

        }
    }

    @Override
    public Arbitru findOne(Long id)
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        Arbitru a=null;
        try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Arbitru WHERE ID_arbitru=?"))
        {
            int id1= (int)(long)id;
            preStmt.setInt(1,id1);
            try(ResultSet resultSet=preStmt.executeQuery())
            {
                while(resultSet.next())
                {
                    Long id2=Long.valueOf(resultSet.getInt("ID_arbitru"));
                    String nume=resultSet.getString("nume");
                    String user=resultSet.getString("user");
                    String pw=resultSet.getString("pw");
                    Long id_proba=Long.valueOf(resultSet.getInt("ID_proba"));
                    Proba proba=repoproba.findOne(id_proba);
                    a=new Arbitru(nume,user,pw,proba);
                    a.setId(id2);
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
        logger.traceExit(a);
        return a;


    }
    @Override
    public void save(Arbitru entity)
    {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("INSERT INTO Arbitru(nume,user,pw,ID_proba) values(?,?,?,?)")){
            preStmt.setString(1,entity.getNume());
            preStmt.setString(2,entity.getUser());
            preStmt.setString(3,entity.getPw());
            int id1= (int)(long)entity.getProba().getId();
            preStmt.setInt(4,id1);
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
        try(PreparedStatement preStmt = connection.prepareStatement("DELETE FROM Arbitru WHERE ID_arbitru=?")) {
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
    public void update(Arbitru entity)
    {
        logger.traceEntry("update arbitru {}",entity);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt = connection.prepareStatement("UPDATE Arbitru SET nume=?,user=?,pw=?,ID_proba=? WHERE ID_arbitru=?")) {
            preStmt.setString(1,entity.getNume());
            preStmt.setString(2,entity.getUser());
            preStmt.setString(3,entity.getPw());
            int id1= (int)(long)entity.getProba().getId();
            preStmt.setInt(4,id1);
            int result=preStmt.executeUpdate();
            logger.trace("Update {} instances",result);

        }catch(SQLException exception)
        {
            logger.error(exception);
            System.out.println("Error DB"+exception);
        }
    }
    @Override
    public Arbitru findUserPw(String user,String pw){
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        Arbitru a=null;
        try(PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Arbitru WHERE user=? AND pw=?"))
        {
            preStmt.setString(1,user);
            preStmt.setString(2,pw);
            try(ResultSet resultSet=preStmt.executeQuery())
            {
                while(resultSet.next())
                {
                    Long id2=Long.valueOf(resultSet.getInt("ID_arbitru"));
                    String nume=resultSet.getString("nume");
                    String user1=resultSet.getString("user");
                    String pw1=resultSet.getString("pw");
                    Long id_proba=Long.valueOf(resultSet.getInt("ID_proba"));
                    Proba proba=repoproba.findOne(id_proba);
                    a=new Arbitru(nume,user1,pw1,proba);
                    a.setId(id2);
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
        logger.traceExit(a);
        return a;

    }


}
