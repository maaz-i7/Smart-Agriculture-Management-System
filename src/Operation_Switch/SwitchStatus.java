package src.Operation_Switch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import src.Actuators.Automatic_Actuators.Dehumidifier;
import src.Actuators.Automatic_Actuators.ExhaustFan;
import src.Actuators.Automatic_Actuators.Heater;
import src.Actuators.Automatic_Actuators.Humidifier;
import src.Actuators.Manual_Actuators.DrainPipe;
import src.Actuators.Manual_Actuators.IrrigationPump;
import src.Actuators.Manual_Actuators.Shade;
import src.Actuators.Manual_Actuators.SodiumLamp;
import src.Field_Meters.FarmSimulator;
import src.Loggers.ActuatorLogger;

public class SwitchStatus {
    
    public static PrintWriter writer;
    public static Thread switchStatusChecker;

    public static void init() {

        try {

            writer = new PrintWriter(new BufferedWriter(new FileWriter("src\\Operation_Switch\\switches.txt", false)), true);
            writer.println("Input the status of machines: ON/OFF/Default");
            writer.println("Humidifier      : Default");
            writer.println("Dehumidifier    : Default");
            writer.println("Heater          : Default");
            writer.println("Exhaust_Fan     : Default");
            writer.println("Irrigation_Pump : Default");
            writer.println("Drain_Pipe      : Default");
            writer.println("Sodium_Lamp     : Default");
            writer.println("Shade           : Default");
        }

        catch(Exception e) {System.out.println("switches.txt is missing");}

        switchStatusChecker = new Thread(() -> {
            checkStatus();
        });
        switchStatusChecker.start();
    }

    private static void setToTurnOn(Class<?> Machine) {
        try {

            if((int)Machine.getField("manualSwitch").getInt(null) == 1)
                return;

            Machine.getField("manualSwitch").setInt(null, 1);
            Machine.getField("active").setBoolean(null, true);
            ActuatorLogger.log(FarmSimulator.currentTime + ": " + Machine.getSimpleName() + " turned ON manually | Charges = ₹" + ((double)Machine.getField("operationCharges").getDouble(null)) + "/hour");
        }
        catch(Exception e) {}
    }

    private static void setToTurnOff(Class<?> Machine) {
        try {

            if((int)Machine.getField("manualSwitch").getInt(null) == -1)
                return;

            Machine.getField("manualSwitch").setInt(null, -1);
            Machine.getField("active").setBoolean(null, false);
            ActuatorLogger.log(FarmSimulator.currentTime + ": " + Machine.getSimpleName() + " turned OFF manually");
        }
        catch(Exception e) {}
    }

    private static void setToDefault(Class<?> Machine) {
        try {
            
            if((int)Machine.getField("manualSwitch").getInt(null) == 0)
                return;

            Machine.getField("manualSwitch").setInt(null, 0);
            ActuatorLogger.log(FarmSimulator.currentTime + ": " + Machine.getSimpleName() + " set to work on Default");
        }
        catch(Exception e) {}
    }
 
    private static void checkStatus() {

        while(!Thread.currentThread().isInterrupted()) {

            try(BufferedReader reader = new BufferedReader(new FileReader("src\\Operation_Switch\\switches.txt"))) {
                
                //skip heading
                reader.readLine();

                // Humidifier status
                String machine = reader.readLine().trim();

                if(machine.substring(machine.length()-2, machine.length()).toLowerCase().equals("on"))
                    setToTurnOn(Humidifier.class);

                else if(machine.substring(machine.length()-3, machine.length()).toLowerCase().equals("off"))
                    setToTurnOff(Humidifier.class);

                else 
                    setToDefault(Humidifier.class);

                // Dehumidifier status
                machine = reader.readLine().trim();
                if(machine.substring(machine.length()-2, machine.length()).toLowerCase().equals("on"))
                    setToTurnOn(Dehumidifier.class);

                else if(machine.substring(machine.length()-3, machine.length()).toLowerCase().equals("off"))
                    setToTurnOff(Dehumidifier.class);

                else 
                    setToDefault(Dehumidifier.class);

                // Heater status
                machine = reader.readLine().trim();
                if(machine.substring(machine.length()-2, machine.length()).toLowerCase().equals("on"))
                    setToTurnOn(Heater.class);

                else if(machine.substring(machine.length()-3, machine.length()).toLowerCase().equals("off"))
                    setToTurnOff(Heater.class);

                else 
                    setToDefault(Heater.class);

                // ExhaustFan status
                machine = reader.readLine().trim();
                if(machine.substring(machine.length()-2, machine.length()).toLowerCase().equals("on"))
                    setToTurnOn(ExhaustFan.class);

                else if(machine.substring(machine.length()-3, machine.length()).toLowerCase().equals("off"))
                    setToTurnOff(ExhaustFan.class);

                else 
                    setToDefault(ExhaustFan.class);

                // IrrigationPump status
                machine = reader.readLine().trim();
                if(machine.substring(machine.length()-2, machine.length()).toLowerCase().equals("on"))
                    setToTurnOn(IrrigationPump.class);

                else if(machine.substring(machine.length()-3, machine.length()).toLowerCase().equals("off"))
                    setToTurnOff(IrrigationPump.class);

                else 
                    setToDefault(IrrigationPump.class);

                // DrainPipe status
                machine = reader.readLine().trim();
                if(machine.substring(machine.length()-2, machine.length()).toLowerCase().equals("on"))
                    setToTurnOn(DrainPipe.class);

                else if(machine.substring(machine.length()-3, machine.length()).toLowerCase().equals("off"))
                    setToTurnOff(DrainPipe.class);

                else 
                    setToDefault(DrainPipe.class);

                // SodiumLamp status
                machine = reader.readLine().trim();
                if(machine.substring(machine.length()-2, machine.length()).toLowerCase().equals("on"))
                    setToTurnOn(SodiumLamp.class);

                else if(machine.substring(machine.length()-3, machine.length()).toLowerCase().equals("off"))
                    setToTurnOff(SodiumLamp.class);

                else 
                    setToDefault(SodiumLamp.class);

                // Shade status
                machine = reader.readLine().trim();
                if(machine.substring(machine.length()-2, machine.length()).toLowerCase().equals("on"))
                    setToTurnOn(Shade.class);

                else if(machine.substring(machine.length()-3, machine.length()).toLowerCase().equals("off"))
                    setToTurnOff(Shade.class);

                else 
                    setToDefault(Shade.class);
            }

            catch(Exception e) {}

            try{Thread.sleep(500);}
            catch(Exception e) {}
        }
    }

    public static void close() {
        switchStatusChecker.interrupt();
    }

    public static void main(String[] args) {
        init();
    }
}
