package com.leonvirus.autofinder2.controller;

import com.leonvirus.autofinder2.model.CarSearchResult;
import com.leonvirus.autofinder2.model.ResultWindow;
import com.leonvirus.autofinder2.search.SearchTaskRunnable;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.web.WebView;
import java.io.IOException;

public class ResultWindowController {

    private ResultWindow resultWindow;

    @FXML
    private WebView webView;


    @FXML
    private ListView carList;


    @FXML
    public void initialize(){
    }

    public void init(ResultWindow resultWindow){
        this.resultWindow = resultWindow;
        String url = resultWindow.getResults().get(0).getSearchUrl();
        webView.getEngine().load(url);

        carList.getItems().removeAll(carList.getItems());
        for (CarSearchResult result: resultWindow.getResults()){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/SearchResultView.fxml"));
            try {
                carList.getItems().add(loader.load());
                SearchResultViewController controller = loader.getController();
                controller.init(result);
            } catch (IOException e){
                e.getMessage();
            }
        }

        carList.getSelectionModel().getSelectedItems().addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                changeSelection();
            }
        });
    }

    private void changeSelection(){
        int index = carList.getSelectionModel().getSelectedIndex();
        webView.getEngine().load(resultWindow.getResults().get(index).getSearchUrl());
    }

}
