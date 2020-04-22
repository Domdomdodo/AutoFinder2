package com.leonvirus.autofinder2.model;

import javax.naming.directory.SearchResult;
import java.util.List;

public class ResultWindow {

    private List<CarSearchResult> results;

    public ResultWindow(List<CarSearchResult> results){
        this.results = results;
    }

    public List<CarSearchResult> getResults() {
        return results;
    }
}
