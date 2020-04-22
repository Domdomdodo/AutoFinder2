package com.leonvirus.autofinder2.controller;

import com.leonvirus.autofinder2.model.CarSearchResult;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SearchResultViewController {

    CarSearchResult searchResult;

    @FXML
    private Text licensePlate;

    @FXML
    private Text model;

    @FXML
    private Text brand;

    @FXML
    private Text type;

    @FXML
    private Text status;

    @FXML
    public void initialize(){
    }


    public void init(CarSearchResult searchResult){
        this.searchResult = searchResult;

        licensePlate.setText(searchResult.getCar().getLicensePlate());
        model.setText(searchResult.getCar().getModel());
        type.setText(searchResult.getCar().getType());
        brand.setText(searchResult.getCar().getBrand());
        if (searchResult.isFound()){
            status.setText("Found");
            status.setFill(Color.GREEN);
        } else {
            status.setText("Not found");
            status.setFill(Color.RED);
        }
    }

    public SearchResultViewController(){
    }

    @FXML
    private void openSearchResult(){
        try {
            Desktop.getDesktop().browse(new URI(searchResult.getSearchUrl()));
        } catch (URISyntaxException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
