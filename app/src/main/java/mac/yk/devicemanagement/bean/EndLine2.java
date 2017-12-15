package mac.yk.devicemanagement.bean;

public class EndLine2 {
	int unit;
    int line_id;
    int s1,s2,s3,s4;
  
    int radio_station;
    int temperature;
    float battery;
    float power;
    long time;
	public EndLine2(int unit, int line_id, int s1, int s2, int s3,int s4,int radio_station, int temperature, float battery,
			float power, long time) {
		super();
		this.unit = unit;
		this.line_id = line_id;
		this.s1 = s1;
		this.s2 = s2;
		this.s3=s3;
		this.s4=s4;
		this.radio_station = radio_station;
		this.temperature = temperature;
		this.battery = battery;
		this.power = power;
		this.time = time;
	}
	public int getUnit() {
		return unit;
	}
	public void setUnit(int unit) {
		this.unit = unit;
	}
	public int getLine_id() {
		return line_id;
	}
	public void setLine_id(int line_id) {
		this.line_id = line_id;
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
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public float getBattery() {
		return battery;
	}
	public void setBattery(float battery) {
		this.battery = battery;
	}
	public float getPower() {
		return power;
	}
	public void setPower(float power) {
		this.power = power;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getS3() {
		return s3;
	}
	public void setS3(int s3) {
		this.s3 = s3;
	}
	public int getS4() {
		return s4;
	}
	public void setS4(int s4) {
		this.s4 = s4;
	}
	
}
