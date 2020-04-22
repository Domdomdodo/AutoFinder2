package com.leonvirus.autofinder2.controller;

import com.jfoenix.controls.*;
import com.leonvirus.autofinder2.model.Car;
import com.leonvirus.autofinder2.model.CarSearchResult;
import com.leonvirus.autofinder2.model.MainWindow;
import com.leonvirus.autofinder2.model.ResultWindow;
import com.leonvirus.autofinder2.resource.CarReader;
import com.leonvirus.autofinder2.search.SearchTaskRunnable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Toggle;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SearchTaskController {

    private SearchTaskRunnable searchTaskRunnable;
    private MainWindowController mainWindowController;


    @FXML
    private JFXToggleButton togglePause;

    @FXML
    private Text statusText;

    @FXML
    private Text carAmount;

    @FXML
    private Text foundCars;

    @FXML
    private Text notFoundCars;

    @FXML
    JFXProgressBar progressBar;

    public SearchTaskController(){

    }

    @FXML
    public void initialize(){
    }

    public void init(SearchTaskRunnable searchTaskRunnable, int taskNum, MainWindowController mainWindowController){
        carAmount.setText("");
        foundCars.setText("");
        notFoundCars.setText("");
        statusText.setText("");
        this.mainWindowController = mainWindowController;
        this.searchTaskRunnable = searchTaskRunnable;
        setProgress(0);
        this.carAmount.setText(String.valueOf(searchTaskRunnable.getCarCount()));

        searchTaskRunnable.progress.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setProgress(newValue.doubleValue());
                    }
                });
            }
        });

        ChangeListener<Boolean> booleanChangeListener = (observable, oldValue, newValue) -> Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateStatus();
            }
        });

        searchTaskRunnable.finished.addListener(booleanChangeListener);
        searchTaskRunnable.waiting.addListener(booleanChangeListener);
        searchTaskRunnable.pause.addListener(booleanChangeListener);

        searchTaskRunnable.getSearchTask().results.addListener((ListChangeListener<CarSearchResult>) c -> {
                foundCars.setText(String.valueOf(searchTaskRunnable.getSearchTask().getFoundCars()));
                notFoundCars.setText(String.valueOf(searchTaskRunnable.getSearchTask().getNotFoundCars()));
                });

        searchTaskRunnable.getSearchTask().cars.addListener((ListChangeListener<Car>) c ->
                carAmount.setText(String.valueOf(searchTaskRunnable.getCarCount())));

    }

    private void setProgress(double progress){
        progressBar.setProgress(progress);
    }

    @FXML
    private void remove(){
        mainWindowController.removeSearchTaskRunnable(searchTaskRunnable);
    }

    @FXML
    private void setPause(){
        if (togglePause.isSelected()){
            searchTaskRunnable.setPause(true);
        } else {
            searchTaskRunnable.setPause(false);
        }
    }

    private void updateStatus(){
        if (searchTaskRunnable.finished.getValue()){
            statusText.setText("Finished");
        } else {
            if (searchTaskRunnable.pause.getValue()) {
                statusText.setText("Paused");
            } else if (searchTaskRunnable.waiting.getValue()){
                    statusText.setText("Waiting");
                } else {
                statusText.setText("Searching");
            }
        }
    }

    @FXML
    private void openResults(){
        List<CarSearchResult> results = searchTaskRunnable.getSearchTask().getResults();
        Stage resultWindow = new Stage();
        resultWindow.setTitle("Search result");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ResultWindow.fxml"));
        try {
            JFXDecorator decorator = new JFXDecorator(resultWindow, loader.load());
            ResultWindowController controller = loader.getController();
            String uri = getClass().getResource("/css/styles.css").toExternalForm();
            Scene scene = new Scene(decorator);
            scene.getStylesheets().add(uri);
            resultWindow.setScene(scene);
            resultWindow.show();
            controller.init(new ResultWindow(results));
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
