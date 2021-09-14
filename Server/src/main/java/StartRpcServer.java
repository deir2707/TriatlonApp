import repo.*;
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

        ParticipantRepository repopart=new ParticipantDBRepository(serverProps);
        ProbaRepository repoprob=new ProbaDBRepository(serverProps);
        ArbitruRepository repoarb=new ArbitruDBRepository(serverProps,repoprob);
        RezultatRepository reporezultat=new RezultatDBRepository(serverProps,repoprob,repopart);
        IService service=new Service(repoarb,reporezultat,repoprob,repopart);

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
}
