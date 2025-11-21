package src.Actuators.Automatic_Actuators;

import src.Data_Logger.ActuatorLogger;
import src.Users.Admin.FieldAdmin;

public class Humidifier {
    
    /*
     * effect           - the change in value that machine  brings out
     * operationCharges - cost in ₹/hour
     * active           - working status
     * manualSwitch     - can take value from {-1, 0, 1}
     *                    -1 - manually turned OFF
     *                     0 - set to operate automatically
     *                     1 - manually turned ON
     */
    public static double effect = 2; 
    public static double operationCharges = 50;
    public static boolean active = false;
    public static int manualSwitch = 0;

    public static void readAndAnalyzeHumidityData(String timeStamp, double value) {

        if (manualSwitch == -1 || manualSwitch == 1) {
            return;
        }

        if (FieldAdmin.MIN_HUMIDITY_THRESHOLD <= value && value <= FieldAdmin.MAX_HUMIDITY_THRESHOLD) {
            
            Humidifier.turnOff(timeStamp);
            return;
        }

        if (value < FieldAdmin.MIN_HUMIDITY_THRESHOLD) {

            Dehumidifier.turnOff(timeStamp);
            Humidifier.turnOn(timeStamp);
        }
    }

    public static void turnOn(String timeStamp) {

        if (active)
            return;

        ActuatorLogger.log(timeStamp + ": Humidifier turned ON | Charges = ₹" + operationCharges + "/hour");
        active = true;
    }

    public static void turnOff(String timeStamp) {

        if (!active)
            return;

        ActuatorLogger.log(timeStamp + ": Humidifier turned OFF");
        active = false;
    }
}
