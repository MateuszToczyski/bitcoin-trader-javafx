package com.trader;

import java.math.*;
import java.util.*;
import java.util.concurrent.*;

public class PriceService {

    private double midPrice;
    private double askPrice;
    private double bidPrice;
    private double spread;
    private boolean started = false;
    private List<PriceObserver> observers = new LinkedList<>();

    public PriceService(double spread) {
        this.spread = spread;
        updateBidAndAsk();
    }

    public void start() {

        CompletableFuture.runAsync(() -> {

            started = true;

            while(started) {

                updatePrices();
                notifyObservers();

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop () {
        started = false;
    }

    public void addObserver(PriceObserver observer) {
        observers.add(observer);
    }

    private void updatePrices() {

        //implementation

//        int randomNumber = new Random().nextInt(2);
//
//        if(randomNumber == 0) {
//            midPrice *= 0.999;
//        } else {
//            midPrice *= 1.001;
//        }
//
//        updateBidAndAsk();
    }

    private void updateBidAndAsk() {
        askPrice = round(midPrice + spread / 2);
        bidPrice = round(midPrice - spread / 2);
    }

    private void notifyObservers() {
        for(PriceObserver observer : observers) {
            observer.update(bidPrice, askPrice);
        }
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
