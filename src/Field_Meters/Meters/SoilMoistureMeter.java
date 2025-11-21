package src.Field_Meters.Meters;

import src.Field_Meters.*;
import src.Users.*;

// A meter is made of sensor and data logger
public class SoilMoistureMeter implements Sensor, DataLogger {
        
    public double data;
    // Transducer senses data and sends it to Sensor
    public void senseData(String timeStamp, double value) {
        this.sendData(timeStamp, value);
    }

    // Sensor receives data sensed from Transducer and sends it to data logger
    public void sendData(String timeStamp, double value) {
        this.receiveData(timeStamp, value);
    }

    // Data logger receives the sensed data and logs it on the display
    public void receiveData(String timeStamp, double value) {
        this.logData(timeStamp, value);
    }

    // Data logger logs and displays the data to Agronomist
    public void logData(String timeStamp, double value) {
        this.data = value;
        Agronomist.readAndAnalyzeSoilMoistureData(timeStamp, this);
    }
}
