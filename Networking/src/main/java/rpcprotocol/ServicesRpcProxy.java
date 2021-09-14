package rpcprotocol;

import domain.Arbitru;
import domain.Lista;
import domain.Participant;
import domain.Rezultat;
import dto.ArbitruDTO;
import service.IService;
import service.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements IService {
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String host;
    private int port;
    private List<Observer> clients=new ArrayList<>();
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ServicesRpcProxy(String host,int port)
    {
        this.host=host;
        this.port=port;
        this.clients=new ArrayList<>();
        qresponses=new LinkedBlockingQueue<Response>();
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            clients=new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }

    private Response readResponse() throws Exception {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection()  {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response)
    {
        for (Observer o:clients)
            o.execute_update();
    }
    private boolean isUpdate(Response response){
        return response.type()== ResponseType.UPDATE;
    }

    @Override
    public Arbitru login(String user, String pw)   {
        initializeConnection();
        Arbitru a=new Arbitru(user,pw);
        Request request=new Request.Builder().type(RequestType.LOGIN).data(a).build();
        try
        {
            sendRequest(request);
        }catch(Exception e){e.printStackTrace();}

        Response response=null;
        try
        {
            response=readResponse();
        }catch (Exception e){e.printStackTrace();}
        if(response.type()==ResponseType.OK)
        {
            a= (Arbitru) response.data();
            return a;
        }
        closeConnection();
        throw new IllegalArgumentException(response.data().toString());
    }

    @Override
    public Iterable<Participant> getAllParticipantiOrdonati() {
        Request request=new Request.Builder().type(RequestType.GET_PARTICIPANTI_ORDONATI).build();
        try{
            sendRequest(request);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Response response=null;
        try
        {
            response=readResponse();
        }catch (Exception e){e.printStackTrace();}
        if(response.type()==ResponseType.OK)
        {
            Iterable<Participant> participants=(Iterable<Participant>)response.data();
            return participants;
        }
        return null;
    }

    @Override
    public Participant findOnePart(Long id) {
        Request request=new Request.Builder().type(RequestType.FIND_ONE_PART).data(id).build();
        try{
            sendRequest(request);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Response response=null;
        try
        {
            response=readResponse();
        }catch (Exception e){e.printStackTrace();}
        if(response.type()==ResponseType.OK)
        {
            Participant p=(Participant)response.data();
            return p;
        }
        return null;
    }

    @Override
    public void AdaugaRezUpPart(Rezultat r, Participant p) {
        Lista l=new Lista(r,p);
        Request request=new Request.Builder().type(RequestType.ADAUGA_REZULTAT).data(l).build();
        try{
            sendRequest(request);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Response response=null;
        try
        {
            response=readResponse();
        }catch (Exception e){e.printStackTrace();}
        if(response.type()==ResponseType.OK)
        {
            System.out.println("S-a efectuat adaugarea");
        }


    }

    @Override
    public Iterable<Participant> getParticipantiMe(Arbitru a) {
        Request request=new Request.Builder().type(RequestType.GET_PARTICIPANTI_AR).data(a).build();
        try{
            sendRequest(request);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Response response=null;
        try
        {
            response=readResponse();
        }catch (Exception e){e.printStackTrace();}
        if(response.type()==ResponseType.OK)
        {
            Iterable<Participant> participants=(Iterable<Participant>)response.data();
            return participants;
        }
        return null;
    }

    @Override
    public void add_observer(Observer o)
    {
        if(o==null) throw new IllegalArgumentException("Observer cannot be null");
        clients.add(o);
    }
    @Override
    public void notify_observer(){
        clients.forEach(Observer::execute_update);
    }
    @Override
    public void remove_observers(){clients.clear();}

    @Override
    public void remove_observer(Observer o){
        clients.remove(o);
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

}
