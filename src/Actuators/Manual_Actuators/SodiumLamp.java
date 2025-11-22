package src.Actuators.Manual_Actuators;

import src.Loggers.ActuatorLogger;

public class SodiumLamp {
    
    /*
     * effect           - the change in value that machine  brings out
     * operationCharges - cost in ₹/hour
     * active           - working status
     * manualSwitch     - can take value from {-1, 0, 1}
     *                    -1 - manually turned OFF
     *                     0 - set to operate automatically
     *                     1 - manually turned ON
     */
    public static double effect = 10000; 
    public static double operationCharges = 100;
    public static boolean active = false;
    public static int manualSwitch = 0;

    public static void turnOn(String timeStamp) {

        if(active)
            return;

        ActuatorLogger.log(timeStamp + ": Sodium Lamp Turned ON | Charges: ₹" + operationCharges + "/hour");
        active = true;
    }

    public static void turnOff(String timeStamp) {
        
        if(!active)
            return;

        ActuatorLogger.log(timeStamp + ": Sodium Lamp Turned OFF");
        active = false;
    }
}
