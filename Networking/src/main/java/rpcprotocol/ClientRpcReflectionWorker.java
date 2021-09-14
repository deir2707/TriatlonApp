package rpcprotocol;

import domain.Arbitru;
import domain.Lista;
import domain.Participant;
import domain.Rezultat;
import org.graalvm.compiler.graph.NodeWorkList;
import service.IService;
import service.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

public class ClientRpcReflectionWorker implements Runnable, Observer {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        server.add_observer(this);
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("HandlerName " + handlerName);
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void execute_update() {
        try {
            sendResponse(new Response.Builder().type(ResponseType.UPDATE).build());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Response handleLOGIN(Request request) {
        try {
            Arbitru a = (Arbitru) request.data();
            a = server.login(a.getUser(), a.getPw());
            Response response = new Response.Builder().type(ResponseType.OK).data(a).build();
            return response;
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_PARTICIPANTI_ORDONATI(Request request) {
        try {
            Iterable<Participant> participants = server.getAllParticipantiOrdonati();
            Response response = new Response.Builder().type(ResponseType.OK).data(participants).build();
            return response;
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_PARTICIPANTI_AR(Request request)
    {
        try {
            Arbitru a=(Arbitru) request.data();
            Iterable<Participant> participants = server.getParticipantiMe(a);
            Response response = new Response.Builder().type(ResponseType.OK).data(participants).build();
            return response;
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleFIND_ONE_PART(Request request)
    {
        try {
            Long id=(Long) request.data();
            Participant p=server.findOnePart(id);
            Response response = new Response.Builder().type(ResponseType.OK).data(p).build();
            return response;
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleADAUGA_REZULTAT(Request request)
    {
        try {
            Lista list=(Lista)request.data();
            Rezultat r=list.getR();
            Participant p=list.getParticipant();
            server.AdaugaRezUpPart(r,p);
            Response response = new Response.Builder().type(ResponseType.OK).build();
            return response;
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
}
