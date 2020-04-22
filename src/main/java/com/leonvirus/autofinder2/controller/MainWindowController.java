package com.leonvirus.autofinder2.controller;

import com.jfoenix.controls.*;
import com.leonvirus.autofinder2.model.Car;
import com.leonvirus.autofinder2.model.MainWindow;
import com.leonvirus.autofinder2.model.SearchTask;
import com.leonvirus.autofinder2.resource.CarReader;
import com.leonvirus.autofinder2.search.SearchTaskRunnable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainWindowController {

    MainWindow mainWindow;
    private File selectedFile = null;
    private CarReader carReader;
    private ObservableList<SearchTaskRunnable> searchTaskRunnables;
    public boolean VALID_FILE_SELECTED = false;

    @FXML
    private JFXTextField fileTextField;

    @FXML
    private JFXTreeTableView carTable;

    @FXML
    private JFXListView  taskList;

    public MainWindowController(){

    }

    @FXML
    public void initialize(){
        mainWindow = new MainWindow();
        carReader = new CarReader();
        searchTaskRunnables = FXCollections.observableList(new ArrayList<>());
        fileTextField.setEditable(false);

        searchTaskRunnables.addListener(new ListChangeListener<SearchTaskRunnable>() {
            @Override
            public void onChanged(Change<? extends SearchTaskRunnable> c) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateTaskList();
                    }
                });
            }
        });

        //CarTable
        TreeTableColumn<String, Car> licensePlate   = new TreeTableColumn<>("License plate");
        TreeTableColumn<String, Car> brand = new TreeTableColumn<>("Brand");
        TreeTableColumn<String, Car> model = new TreeTableColumn<>("Model");
        TreeTableColumn<String, Car> type = new TreeTableColumn<>("Type");
        TreeTableColumn<String, Car> carBody = new TreeTableColumn<>("Car body");
        TreeTableColumn<Integer, Car> constructionYear = new TreeTableColumn<>("Construction year");
        TreeTableColumn<Integer, Car> kilometers = new TreeTableColumn<>("Kilometers");
        TreeTableColumn<Integer, Car> doors = new TreeTableColumn<>("Doors");
        TreeTableColumn<String, Car> colour = new TreeTableColumn<>("Colour");
        TreeTableColumn<Double, Car> price = new TreeTableColumn<>("Price");
        TreeTableColumn<Car.FuelType, Car> fuelType   = new TreeTableColumn<>("Fuel type");

        licensePlate.setCellValueFactory(new TreeItemPropertyValueFactory<>("licensePlate"));
        brand.setCellValueFactory(new TreeItemPropertyValueFactory<>("brand"));
        model.setCellValueFactory(new TreeItemPropertyValueFactory<>("model"));
        type.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        carBody.setCellValueFactory(new TreeItemPropertyValueFactory<>("carBody"));
        constructionYear.setCellValueFactory(new TreeItemPropertyValueFactory<>("constructionYear"));
        kilometers.setCellValueFactory(new TreeItemPropertyValueFactory<>("kilometers"));
        doors.setCellValueFactory(new TreeItemPropertyValueFactory<>("doors"));
        colour.setCellValueFactory(new TreeItemPropertyValueFactory<>("colour"));
        price.setCellValueFactory(new TreeItemPropertyValueFactory<>("price"));
        fuelType.setCellValueFactory(new TreeItemPropertyValueFactory<>("fuelType"));

        carTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        carTable.getColumns().addAll(licensePlate, model, brand, type, carBody, constructionYear, kilometers, doors, colour, price, fuelType);
    }


    public void updateCarTable(){
        if (selectedFile != null) {
            List<Car> cars = carReader.fetchCars(selectedFile);
            TreeItem<Car> root = new TreeItem<Car>(cars.get(0));
            for (Car car: cars){
                root.getChildren().add(new TreeItem(car));
            }
            carTable.setRoot(root);
            carTable.setShowRoot(false);
        }
    }


    @FXML
    public void addTask(){
        List<Car> cars = new ArrayList<>();
        for (Object item: carTable.getSelectionModel().getSelectedItems()){
            TreeItem treeItem = (TreeItem) item;
            Car car = (Car)treeItem.getValue();
            cars.add(car);
        }
        SearchTaskRunnable runnable = new SearchTaskRunnable(new SearchTask(cars), 5000);
        searchTaskRunnables.add(runnable);
        new Thread(runnable).start();
    }

    @FXML
    public void updateTaskList(){
        taskList.getItems().removeAll(taskList.getItems());
        int num = 1;
        for (SearchTaskRunnable searchTaskRunnable: searchTaskRunnables){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/SearchTask.fxml"));
            try {
                taskList.getItems().add(loader.load());
                SearchTaskController controller = loader.getController();
                controller.init(searchTaskRunnable, num, this);
                num++;
            } catch (IOException e){
                e.getMessage();
            }
        }
    }

    @FXML
    private void chooseFile(){
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Open Resource File");
        selectedFile = fileChooser.showOpenDialog(carTable.getScene().getWindow());
        if (selectedFile != null) {
            if (selectedFile.isFile() && FilenameUtils.getExtension(selectedFile.getAbsolutePath()).equals("xlsx")) {
                fileTextField.setText(selectedFile.getName());
                VALID_FILE_SELECTED = true;
            } else {
                fileTextField.setText("Invalid file!");
                VALID_FILE_SELECTED = false;
            }
        } else {
            fileTextField.setText("No file selected");
            VALID_FILE_SELECTED = false;
        }
        updateCarTable();
    }

    @FXML
    private void selectAllCars(){
        carTable.getSelectionModel().selectAll();
    }

    @FXML
    private void deselectAllCars(){
        carTable.getSelectionModel().clearSelection();
    }

    public void removeSearchTaskRunnable(SearchTaskRunnable searchTaskRunnable){
        searchTaskRunnables.remove(searchTaskRunnable);
        searchTaskRunnable.stop();
    }
}
