package src.Field_Meters;

// interface that Meters will extend from
public interface DataLogger {

    String manufacturer = "Mitsubishi";
    int lifeSpan = 1825; //in days

    void receiveData(String timeStamp, double value);
    void logData(String timeStamp, double value);
}
