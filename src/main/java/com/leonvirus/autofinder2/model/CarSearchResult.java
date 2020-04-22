package com.leonvirus.autofinder2.model;

public class CarSearchResult {

    private Car car;
    private String searchUrl;
    private boolean found;

    public CarSearchResult(Car car, String searchUrl, boolean found) {
        this.car = car;
        this.searchUrl = searchUrl;
        this.found = found;
    }

    public boolean isFound() {
        return found;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public Car getCar() {
        return car;
    }
}
