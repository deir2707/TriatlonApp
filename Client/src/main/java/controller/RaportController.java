package controller;

import javafx.application.Platform;
import service.IService;
import service.Observer;
import domain.Arbitru;
import domain.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.Service;

public class RaportController implements Observer {
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


    public void setService(IService service, Arbitru a){
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
        Iterable<Participant> participants=service.getParticipantiMe(this.a);
        participants.forEach(participant -> modelParticipanti.add(participant));
        }
    @Override
    public void execute_update() {
        Platform.runLater(()->{
            modelParticipanti.clear();initMode();});
    }




}

