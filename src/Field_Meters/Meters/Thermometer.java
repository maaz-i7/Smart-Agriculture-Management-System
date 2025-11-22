package src.Field_Meters.Meters;

import src.Actuators.Automatic_Actuators.ExhaustFan;
import src.Actuators.Automatic_Actuators.Heater;
import src.Field_Meters.*;
import src.Loggers.MeterLogger;
import src.Users.*;

// A meter is made of sensor and data logger
public class Thermometer implements Sensor, DataLogger {
        
    public double data;
    // Transducer senses data and sends it to Sensor
    public void senseData(String timeStamp, double value) {
        this.sendData(timeStamp, value);
    }

    // Sensor receives data sensed from Transducer and sends it to data logger
    public void sendData(String timeStamp, double value) {
        this.receiveData(timeStamp, value);
    }

    // Data logger receives the sensed data from Sensor and logs it on the display
    public void receiveData(String timeStamp, double value) {
        this.logData(timeStamp, value);
    }

    // Data logger logs and displays the data to Agronomist and ExhaustFan and Heater
    public void logData(String timeStamp, double value) {
        
        this.data = value;
        MeterLogger.logTemperature(value);
        Agronomist.readAndAnalyzeTemperatureData(timeStamp, this);
        ExhaustFan.readAndAnalyzeTemperatureData(timeStamp, value);
        Heater.readAndAnalyzeTemperatureData(timeStamp, value);
    }
}
