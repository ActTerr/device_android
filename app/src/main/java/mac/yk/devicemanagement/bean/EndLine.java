package mac.yk.devicemanagement.bean;

public class EndLine {
    int station;
    int line_id;
    int temperature;
    int s1;
    int s2;
    int radio_station;
    long time;
    float battery;
    float power;

    public EndLine(int station, int line_id, int s1, int s2, int radio_station, int temperature, float battery, float power, long time
    ) {
        super();
        this.station = station;
        this.line_id = line_id;
        this.temperature = temperature;
        this.s1 = s1;
        this.s2 = s2;
        this.radio_station = radio_station;
        this.time = time;
        this.battery = battery;
        this.power=power;
    }

    public int getStation() {
        return station;
    }
    public void setStation(int station) {
        this.station = station;
    }
    public int getLine_id() {
        return line_id;
    }
    public void setLine_id(int line_id) {
        this.line_id = line_id;
    }
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public int getS1() {
        return s1;
    }
    public void setS1(int s1) {
        this.s1 = s1;
    }
    public int getS2() {
        return s2;
    }
    public void setS2(int s2) {
        this.s2 = s2;
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


    public float getBattery() {
        return battery;
    }

    public void setBattery(float battery) {
        this.battery = battery;
    }

    public EndLine() {
        super();
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "EndLine [station=" + station + ", line_id=" + line_id + ", temperature=" + temperature + ", s1=" + s1
                + ", s2=" + s2 + ", radio_station=" + radio_station + ", time=" + time + ", battery=" + battery + "]";
    }


}