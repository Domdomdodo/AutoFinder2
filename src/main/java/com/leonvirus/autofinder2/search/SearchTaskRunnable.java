package com.leonvirus.autofinder2.search;

import com.leonvirus.autofinder2.model.Car;
import com.leonvirus.autofinder2.model.CarSearchResult;
import com.leonvirus.autofinder2.model.SearchTask;
import javafx.beans.property.*;
import org.jsoup.select.Elements;

import javax.lang.model.element.Element;
import javax.naming.directory.SearchResult;
import java.util.List;

public class SearchTaskRunnable implements Runnable {

    private SearchTask searchTask;
    public IntegerProperty waitTime;
    public DoubleProperty progress = new SimpleDoubleProperty();
    public BooleanProperty waiting = new SimpleBooleanProperty();
    public BooleanProperty finished = new SimpleBooleanProperty();
    private volatile boolean stop = false;
    public volatile BooleanProperty pause = new SimpleBooleanProperty();

    public SearchTaskRunnable(SearchTask SearchTask, int waitTime){
        this.searchTask = SearchTask;
        this.waitTime = new SimpleIntegerProperty(waitTime);
        finished.setValue(false);
        waiting.setValue(false);
        pause.setValue(false);
    }

    @Override
    public void run() {
        List<Car> cars = searchTask.getCars();
        CarSearcher searcher = new CarSearcher();
        for (Car car: cars){
            if (stop){
                break;
            }
            if (pause.getValue()){
                waiting.setValue(true);
                while (pause.getValue()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e){
                        System.out.println(e.getMessage());
                    }
                }
                waiting.setValue(false);
            }
            searcher.buildSearchUrl(car);
            Elements elements = searcher.search(car);
            searchTask.addResult(new CarSearchResult(car, searcher.buildSearchUrl(car), elements.size() > 0));
            updateProgress();
            try {
                waiting.setValue(true);
                Thread.sleep(waitTime.getValue());
                waiting.setValue(false);
            } catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
        finished.setValue(true);
    }

    private void updateProgress(){
        progress.setValue( (double) searchTask.getResults().size() / searchTask.getCars().size());
    }

    private void setWaitTime(int time){
        waitTime.setValue(time);
    }

    public int getCarCount(){
        return searchTask.getCars().size();
    }

    public void stop(){
        stop = true;
    }

    public void setPause(boolean value){
        pause.setValue(value);
    }

    public SearchTask getSearchTask() {
        return searchTask;
    }
}
