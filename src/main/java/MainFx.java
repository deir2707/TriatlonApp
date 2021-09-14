import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import repo.*;
import service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainFx extends Application {

    @Override
    public void start (Stage primaryStage ) throws Exception{

        try{
            Service service=GetService();
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("Login.fxml"));
            AnchorPane root = loader.load();
            primaryStage.initStyle(StageStyle.DECORATED);
            LoginController loginController=loader.getController();
            primaryStage.setScene(new Scene(root,600,400, Color.TRANSPARENT));
            loginController.setService(primaryStage,service);
            primaryStage.show();

        }catch(Exception e)
        {

            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Aplicatia nu a pornit" +e);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    private Service GetService()
    {
        Properties pr=new Properties();
        try
        {
            pr.load(new FileReader("bd.config"));
        }catch(IOException e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu se poate gasi bd.config" +e);
            alert.showAndWait();
    }
        ParticipantRepository repopart=new ParticipantDBRepository(pr);
        ProbaRepository repoprob=new ProbaDBRepository(pr);
        ArbitruRepository repoarb=new ArbitruDBRepository(pr,repoprob);
        RezultatRepository reporezultat=new RezultatDBRepository(pr,repoprob,repopart);
        Service service=new Service(repoarb,reporezultat,repoprob,repopart);
        return service;
    }
    public static void main(String[] args){ launch(args);}
}
