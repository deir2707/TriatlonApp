import domain.Participant;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repo.*;
import repo.Hibernate.ParticipantHbnRepository;
import service.IService;
import service.Service;
import utils.AbstractServer;
import utils.ChatRpcConcurrentServer;
import utils.ServerException;
import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {

        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        SessionFactory s=initialize();
        ParticipantRepository repopart=new ParticipantHbnRepository(s);
        ProbaRepository repoprob=new ProbaDBRepository(serverProps);
        ArbitruRepository repoarb=new ArbitruDBRepository(serverProps,repoprob);
        RezultatRepository reporezultat=new RezultatDBRepository(serverProps,repoprob,repopart);
        IService service=new Service(repoarb,reporezultat,repoprob,repopart);
        System.out.println("TEST 1:"+repopart.findAll());
        System.out.println("TEST 2:"+repopart.findOne((long) 1));
        Participant p=new Participant("Andrei Olaru",500);
        p.setId((long)1);
        System.out.println("TESTE 4:"+ repopart.update(p));
        System.out.println("TEST 3:"+repopart.findAllAlfabetic());

        int ServerPort=defaultPort;
        try {
            ServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number "+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+ServerPort);
        AbstractServer server = new ChatRpcConcurrentServer(ServerPort, service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }

    }
    static SessionFactory initialize()
    {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("h.xml") // configures settings from h.xml
                .build();
        try {
            return new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
        return null;
    }
}
