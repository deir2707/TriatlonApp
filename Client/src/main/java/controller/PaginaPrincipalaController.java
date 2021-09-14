package controller;

import domain.Arbitru;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.IService;
import service.Service;

public class PaginaPrincipalaController {
    Stage stageback;
    Stage stage;
    private IService service;
    Arbitru a;

    @FXML
    Text NumeArbitru;

    @FXML
    TabPane Tabpane;

    @FXML
    Tab TabAdaugare;

    @FXML
    Tab TabRaport;

    public void setService(Stage stageback, Stage stage, IService service, Arbitru a) {
        this.stageback = stageback;
        this.stage = stage;
        this.service = service;
        this.a=a;
        NumeArbitru.setText(a.getNume());
        init_tabs();
    }

    public void init_tabs()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Adaugare.fxml"));
            AnchorPane root = loader.load();
            AdaugareController adaugareController = loader.getController();
            Stage newstage = new Stage();
            newstage.setScene(new Scene(root, 566, 400, Color.TRANSPARENT));
            adaugareController.setService(this.service,this.a);
            TabAdaugare.setContent(newstage.getScene().getRoot());
        }catch(Exception e){
            System.out.println("Nu se incarca Tabul 1 "+e.getMessage());
        }
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Raport.fxml"));
            AnchorPane root = loader.load();
            RaportController raportController = loader.getController();
            Stage newstage = new Stage();
            newstage.setScene(new Scene(root, 566, 400, Color.TRANSPARENT));
            raportController.setService(this.service,this.a);
            TabRaport.setContent(newstage.getScene().getRoot());

        }catch(Exception e){
        System.out.println(e.getMessage());
    }



    }

    public void Logout(ActionEvent actionEvent) {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Login.fxml"));
            AnchorPane root = loader.load();
            LoginController loginController = loader.getController();
            Stage newstage = new Stage();
            newstage.setScene(new Scene(root,600,400, Color.TRANSPARENT));
            newstage.setTitle("Login");
            loginController.setService(newstage,this.service);
            this.stage.hide();
            newstage.show();
        }catch(Exception e){
            System.out.println("Nu merge Logout ul "+e.getMessage());
        }
    }
}
