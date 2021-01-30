package kclc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("view/mainWindow.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("view/addStudent.fxml"));
        primaryStage.setTitle("KCLC School Management System");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 439, 324));
        //primaryStage.setScene(new Scene(root));//, 1332, 856));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
