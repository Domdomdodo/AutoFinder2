package com.leonvirus.autofinder2;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class AutoApplication extends Application {

    public void startApplication(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image("images/icon.png"));
        primaryStage.setTitle("Auto Finder 2");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MainWindow.fxml"));
        JFXDecorator decorator = new JFXDecorator(primaryStage, loader.load());
        String uri = getClass().getResource("/css/styles.css").toExternalForm();
        Scene scene = new Scene(decorator);
        scene.getStylesheets().add(uri);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
