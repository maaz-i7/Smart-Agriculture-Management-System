package src.Users;

import src.Actuators.Manual_Actuators.DrainPipe;
import src.Actuators.Manual_Actuators.IrrigationPump;
import src.Actuators.Manual_Actuators.Shade;
import src.Actuators.Manual_Actuators.SodiumLamp;

public class Farmer extends Person {
    
    /*
     * Overloaded Constructors
     */
    public Farmer(String name, String username, String password, long phoneNum) {
        super(name, username, "Farmer", password, phoneNum, 50000);
    }

    public Farmer(String name, String username, String password, long phoneNum, double salary) {
        super(name, username, "Farmer", password, phoneNum, salary);
    }

    public static void coverWithShade(String timeStamp) {
        Shade.turnOn(timeStamp);
    }

    public static void removeShade(String timeStamp) {
        Shade.turnOff(timeStamp);
    }

    public static void turnSodiumLampOn(String timeStamp) {
        SodiumLamp.turnOn(timeStamp);
    }

    public static void turnSodiumLampOff(String timeStamp) {
        SodiumLamp.turnOff(timeStamp);
    }

    public static void startIrrigation(String timeStamp) {
        IrrigationPump.turnOn(timeStamp);
    }

    public static void stopIrrigation(String timeStamp) {
        IrrigationPump.turnOff(timeStamp);
    }

    public static void openDrainPipe(String timeStamp) {
        DrainPipe.turnOn(timeStamp);
    }

    public static void closeDrainPipe(String timeStamp) {
        DrainPipe.turnOff(timeStamp);
    }
}