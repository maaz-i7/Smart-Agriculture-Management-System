package src.Field_Meters;

// interface that Meters will extend from
public interface Sensor {

    String manufacturer = "Mitsubishi";
    int lifeSpan = 365; //in days

    void senseData(String timeStamp, double value);
    void sendData(String timeStamp, double value); 
}