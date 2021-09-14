package controller;

import domain.Arbitru;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import service.IService;
import service.Service;

public class LoginController {
    Stage stage;
    private IService service;
    @FXML
    TextField PwText;

    @FXML
    TextField UserText;

    @FXML
    Button LoginB;

    public void setService(Stage stage1,IService service1){
    this.stage=stage1;
    this.service=service1;
    }

    public void Login(ActionEvent actionEvent)
    {
        String user=UserText.getText();
        String pw= PwText.getText();
        Arbitru a=service.login(user,pw);
        if(a==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Userul sau Pw  gresite", ButtonType.OK);
            alert.setResizable(true);
            alert.show();
            UserText.setText("");
            PwText.setText("");
            return;
        }else{
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/PaginaPrincipala.fxml"));
                AnchorPane root = loader.load();
                PaginaPrincipalaController paginapcontroller = loader.getController();
                Stage newstage = new Stage();
                newstage.setScene(new Scene(root, 811, 607, Color.TRANSPARENT));
                newstage.setTitle(a.getNume());
                paginapcontroller.setService(this.stage,newstage,service,a);
                this.stage.hide();
                newstage.show();
            }catch(Exception e){
                System.out.println("Nu merge Login ul "+e.getMessage());
            }
        }


    }
}
