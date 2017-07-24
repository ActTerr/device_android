package mac.yk.devicemanagement.bean;

/**
 * Created by mac-yk on 2017/7/19.
 */

public class EndLine {
    String station;
    int number;
    int temperature;
    int sensor;
    int radio_station;
    long time;
    int battery;

    public EndLine(String station, int number, int temperature, int sensor, int radio_station, long time, int battery) {
        this.station = station;
        this.number = number;
        this.temperature = temperature;
        this.sensor = sensor;
        this.radio_station = radio_station;
        this.time = time;
        this.battery = battery;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

    public int getRadio_station() {
        return radio_station;
    }

    public void setRadio_station(int radio_station) {
        this.radio_station = radio_station;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "EndLine{" +
                "station='" + station + '\'' +
                ", number=" + number +
                ", tempreture=" + temperature +
                ", sensor=" + sensor +
                ", radio_station=" + radio_station +
                ", time=" + time +
                '}';
    }

    public EndLine() {
    }
}
