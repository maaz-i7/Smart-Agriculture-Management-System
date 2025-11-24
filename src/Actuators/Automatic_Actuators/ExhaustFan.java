package src.Actuators.Automatic_Actuators;

import src.Loggers.ActuatorLogger;
import src.Users.Admin.FieldAdmin;

public class ExhaustFan {
    
    /*
     * effect           - the change in value that machine  brings out
     * operationCharges - cost in ₹/hour
     * active           - working status
     * manualSwitch     - can take value from {-1, 0, 1}
     *                    -1 - manually turned OFF
     *                     0 - set to operate automatically
     *                     1 - manually turned ON
     */
    public static double effect = -0.5; 
    public static double operationCharges = 10;
    public static boolean active = false;
    public static int manualSwitch = 0; 

    public static void readAndAnalyzeTemperatureData(String timeStamp, double value) {
            
        if (FieldAdmin.MIN_TEMPERATURE_THRESHOLD <= value && value <= FieldAdmin.MAX_TEMPERATURE_THRESHOLD) {
            
            ExhaustFan.turnOff(timeStamp);
            return;
        }

        if (FieldAdmin.MAX_TEMPERATURE_THRESHOLD < value) {

            ExhaustFan.turnOn(timeStamp);
            Heater.turnOff(timeStamp);
        }
    }

    public static void turnOn(String timeStamp) {
        
        if (active || manualSwitch == 1 || manualSwitch == -1)
            return;

        ActuatorLogger.log(timeStamp + ": Exhaust Fan turned ON | Charges = ₹" + operationCharges + "/hour");
        active = true;
    }

    public static void turnOff(String timeStamp) {
         
        if (!active || manualSwitch == 1 || manualSwitch == -1)
            return;

        ActuatorLogger.log(timeStamp + ": Exhaust Fan turned OFF");
        active = false;
    }
}
