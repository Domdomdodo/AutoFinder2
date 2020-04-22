package com.leonvirus.autofinder2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class SearchTask {

    public ObservableList<Car> cars;
    public ObservableList<CarSearchResult> results;


    public SearchTask(List<Car> cars){
        this.cars = FXCollections.observableList(cars);
        results = FXCollections.observableList(new ArrayList<>());
    }

    public void addResult(CarSearchResult result){
        results.add(result);
    }

    public List<Car> getCars() {
        return cars;
    }

    public List<CarSearchResult> getResults() {
        return results;
    }

    public int getFoundCars(){
        int found  = 0;
        for (CarSearchResult result: results){
            if (result.isFound()){
                found++;
            }
        }
        return found;
    }

    public int getNotFoundCars(){
        int notFound  = 0;
        for (CarSearchResult result: results){
            if (!result.isFound()){
                notFound++;
            }
        }
        return notFound;
    }

    public int getTotalCars(){
        return cars.size();
    }
}
