package flink.toureventjava;

import lombok.Data;


public class TourEvent {
    //id:Int,eventName:String,price:Double,time:Long
    private int id;
    private String eventName;
    private double price;
    private long time;
    private int volume;

    public TourEvent() {
    }

    public TourEvent(int id, String eventName, double price, long time,int volume) {
        this.id = id;
        this.eventName = eventName;
        this.price = price;
        this.time = time;
        this.volume = volume;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "TourEvent{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", price=" + price +
                ", time=" + time +
                '}';
    }
}
