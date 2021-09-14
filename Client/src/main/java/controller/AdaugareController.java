package controller;

import javafx.application.Platform;
import service.IService;
import service.Observer;
import domain.Arbitru;
import domain.Participant;
import domain.Rezultat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.Service;

public class AdaugareController implements Observer {
    private IService service;
    Arbitru a;
    ObservableList<Participant> modelParticipanti = FXCollections.observableArrayList();

    @FXML
    TableView TableParticipant;

    @FXML
    TableColumn ColumnName ;

    @FXML
    TableColumn ColumnID ;
    @FXML
    TableColumn ColumnPunctaj ;

    @FXML
    Button Adauga;

    @FXML
    TextField IDField;

    @FXML
    TextField PunctajField;

    public void setService(IService service,Arbitru a){
        this.service=service;
        this.a=a;
        initMode();
        service.add_observer(this);

    }

    @FXML
    public void initialize() {
        ColumnID.setCellValueFactory(new PropertyValueFactory<Participant,Long>("id"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<Participant,String>("nume"));
        ColumnPunctaj.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("punctajtotal"));
        TableParticipant.setItems(modelParticipanti);

    }

    private void initMode(){
        Iterable<Participant> participants=service.getAllParticipantiOrdonati();
        participants.forEach(participant -> modelParticipanti.add(participant));
    }


    @Override
    public void execute_update() {
        Platform.runLater(()->{modelParticipanti.clear();initMode();});
    }




    public void AdaugaRez(ActionEvent actionEvent) {
        String ID = IDField.getText();
        IDField.setText("");
        String Punctaj = PunctajField.getText();
        long id;
        int punctaj;
        try {
            id = Long.parseLong(ID);
            punctaj = Integer.parseInt(Punctaj);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ID-ul si punctajul trebuie sa fie un numar" );
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        Participant p=service.findOnePart(id);
        if(p!=null){
        Participant p2=new Participant(p.getNume(),p.getPunctajtotal()+punctaj);
        Rezultat r=new Rezultat(a.getProba(),punctaj,p);
        p2.setId(p.getId());
        service.AdaugaRezUpPart(r,p2);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ID-ul introdus nu exista");
            alert.showAndWait();

        }



    }
}
