package src.Field_Meters;

import java.util.Random;

import src.Actuators.Automatic_Actuators.Dehumidifier;
import src.Actuators.Automatic_Actuators.ExhaustFan;
import src.Actuators.Automatic_Actuators.Heater;
import src.Actuators.Automatic_Actuators.Humidifier;
import src.Actuators.Manual_Actuators.DrainPipe;
import src.Actuators.Manual_Actuators.IrrigationPump;
import src.Actuators.Manual_Actuators.Shade;
import src.Actuators.Manual_Actuators.SodiumLamp;
import src.Field_Meters.Meters.Hygrometer;
import src.Field_Meters.Meters.Photometer;
import src.Field_Meters.Meters.SoilMoistureMeter;
import src.Field_Meters.Meters.Thermometer;
import src.Loggers.ActuatorLogger;
import src.Loggers.MeterLogger;
import src.Users.Admin.FieldAdmin;

public class FarmSimulator {

    // Simulation settings
    // NOTE: MAKE SURE TO SET LOOK UP TIME IN MeterLogger
    private static final int DELAY_MS = 200; // Speed of simulation, equivalent to 15 minutes of the day

    // --- GLOBAL VARIABLES ---
    /*
     *  soilMoisture                 - stores a realistic data of soil moisture based on weather and time
        humidity                     - stores a realistic data of humidity based on weather and time
        lightIntensity               - stores a realistic data of light intensity based on weather and time
        temperature                  - stores a realistic data of temperature based on weather and time
        compoundHumidifierEffect     - adds up the effect of humidifier with time
        compoundDehumidifierEffect   - adds up the effect of dehumidifier with time 
        compoundHeaterEffect         - adds up the effect of heater with time
        compoundExhaustFanEffect     - adds up the effect of exhaust fan with time
        compoundIrrigationPumpEffect - adds up the effect of irrigation pump with time
        compoundDrainPipeEffect      - adds up the effect of drain pipe with time
     */
    public static double humidity;
    public static double lightIntensity;
    public static double soilMoisture;
    public static double temperature;
    public static double compoundHumidifierEffect = 0;
    public static double compoundDehumidifierEffect = 0;
    public static double compoundHeaterEffect = 0;
    public static double compoundExhaustFanEffect = 0;
    public static double compoundIrrigationPumpEffect = 0;
    public static double compoundDrainPipeEffect = 0;

    // three types of weather conditions to simulate
    public enum WeatherType {
        SUNNY_DRY, COLD_RAINY, HOT_HUMID
    }

    public static void start() throws InterruptedException {

        // set current day weather
        WeatherType currentDay = WeatherType.COLD_RAINY;

        String actuatorslogPath = "src/data_logs/actuator_activities/actuator_activities.txt";
        ActuatorLogger.init(actuatorslogPath);

        String meterslogPathTxt = "src/data_logs/sensors_data/sensors_data.txt";
        String meterslogPathCsv = "src/data_logs/sensors_data/farm_data.csv";
        MeterLogger.init(meterslogPathTxt, meterslogPathCsv);

        Hygrometer hygrometer = new Hygrometer();
        Photometer photometer = new Photometer();
        SoilMoistureMeter soilMoistureMeter = new SoilMoistureMeter();
        Thermometer thermometer = new Thermometer();

        for (int hour = 0; hour < 24; hour++) {
            for (int min = 0; min < 60; min += 15) {

                double timeVal = hour + (min / 60.0);
                String timeStr = String.format("%02d:%02d", hour, min);

                // Generate realistic random values of the 4 parameters
                generateRealisticData(timeVal, currentDay);

                hygrometer.senseData(timeStr, humidity);
                photometer.senseData(timeStr, lightIntensity);
                soilMoistureMeter.senseData(timeStr, soilMoisture);
                thermometer.senseData(timeStr, temperature);

                Thread.sleep(DELAY_MS);
            }
        }

        System.out.println("Simulation completed for 24 hours.");

        // logs total money spent on machines for a day
        System.out.println("\nOpen: Smart-Agriculture-Management-System\\src\\data_logs\\sensors_data\\farm_data.csv to plot graphs and study data");
        System.out.println("\nTotal money spent today: ₹" + FieldAdmin.totalCost);
        ActuatorLogger.log("\nTotal money spent today: ₹" + FieldAdmin.totalCost);

        // Close safely on exit
        ActuatorLogger.close();
        MeterLogger.close();
        // Runtime.getRuntime().addShutdownHook(new Thread(ActuatorLogger::close));
    }

    // --- PHYSICS LOGIC ---
    public static void generateRealisticData(double time, WeatherType type) {

        Random rand = new Random();
        double noise = rand.nextDouble(); //little fluctuations in the data

        // 1. LIGHT
        if (time >= 6 && time <= 18) {
            double peakLux = (type == WeatherType.SUNNY_DRY) ? 60000 : 15000;
            double lightCurve = Math.sin((time - 6) * (Math.PI / 12));
            lightIntensity = (peakLux * lightCurve) + (rand.nextInt(500));
        } else {
            lightIntensity = 0;
        }

        // includes the effects of sodium lamps and shade on light intensity
        lightIntensity += ((Shade.active ? Shade.effect : 0) + (SodiumLamp.active ? SodiumLamp.effect : 0));
        lightIntensity = Math.max(0, lightIntensity);

        // 2. TEMP & HUMIDITY
        double timeRad = (time - 14) * (Math.PI / 12);
        double tempBase, tempVar, humBase, humVar;

        // temperature and humidity fluctuations on three weather conditions
        switch (type) {
            case COLD_RAINY:
                tempBase = 15;
                tempVar = 3;
                humBase = 90;
                humVar = 5;
                break;
            case HOT_HUMID:
                tempBase = 30;
                tempVar = 5;
                humBase = 80;
                humVar = 10;
                break;
            case SUNNY_DRY:
            default:
                tempBase = 28;
                tempVar = 10;
                humBase = 40;
                humVar = 15;
                break;
        }

        // includes the effects of Heater and Exhaust fans on temperature
        compoundHeaterEffect = updateCompoundEffectIncreaser(Heater.class, compoundHeaterEffect);
        compoundExhaustFanEffect = updateCompoundEffectDecreaser(ExhaustFan.class, compoundExhaustFanEffect);
        temperature = tempBase + (tempVar * Math.cos(timeRad)) + (noise * 2 - 1);
        temperature += (compoundExhaustFanEffect + compoundHeaterEffect);

        // includes the effect of humidifier and dehumidifier on humidity
        humidity = humBase - (humVar * Math.cos(timeRad)) + (noise * 5 - 2.5);
        compoundHumidifierEffect = updateCompoundEffectIncreaser(Humidifier.class, compoundHumidifierEffect);
        compoundDehumidifierEffect = updateCompoundEffectDecreaser(Dehumidifier.class, compoundDehumidifierEffect);
        humidity += (compoundHumidifierEffect + compoundDehumidifierEffect);
        humidity = Math.max(0, Math.min(100, humidity));

        // 3. SOIL MOISTURE
        if (time == 0.0)
            soilMoisture = (type == WeatherType.COLD_RAINY) ? 80 : 60;

        if (type == WeatherType.COLD_RAINY) {
            soilMoisture += (rand.nextDouble() * 0.5);
        } else {
            double evaporationRate = (type == WeatherType.SUNNY_DRY) ? 0.2 : 0.05;
            if (lightIntensity > 1000)
                evaporationRate *= 2;
            soilMoisture -= (rand.nextDouble() * evaporationRate);
        }

        // includes the effect of irrigation and drain on soil moisture
        compoundIrrigationPumpEffect = updateCompoundEffectIncreaser(IrrigationPump.class, compoundIrrigationPumpEffect);
        compoundDrainPipeEffect = updateCompoundEffectDecreaser(DrainPipe.class, compoundDrainPipeEffect);
        soilMoisture += (compoundIrrigationPumpEffect + compoundDrainPipeEffect);
        soilMoisture = Math.max(0, Math.min(100, soilMoisture));
    }

    // calculates the gradual effect of machines that decrease a parameter over a period of time
    public static double updateCompoundEffectDecreaser(Class<?> Machine, double value) {

        try {
            boolean active = (boolean) Machine.getField("active").getBoolean(null);
            double effect = (double) Machine.getField("effect").getDouble(null);
            double cost = (double) Machine.getField("operationCharges").getDouble(null);

            // add to cost the cost of operating for 15 minutes
            FieldAdmin.totalCost += (active ? (cost/4) : 0);

            // If the machine is active, it will add up to the compound effect, otherwise, its effect will reduce gradually
            double compoundEffect = value + (active ? 1 : -1) * effect;
            return (compoundEffect > 0) ? 0 : compoundEffect;
        } catch (Exception e) {
            System.out.println("Error in FarmSimulation.java, line 38");
        }

        return 0;
    }

    // calculates the gradual effect of machines that increase a parameter over a period of time
    public static double updateCompoundEffectIncreaser(Class<?> Machine, double value) {

        try {
            boolean active = (boolean) Machine.getField("active").getBoolean(null);
            double effect = (double) Machine.getField("effect").getDouble(null);
            double cost = (double) Machine.getField("operationCharges").getDouble(null);

            // add to cost the cost of operating for 15 minutes
            FieldAdmin.totalCost += (active ? (cost/4) : 0);

            // If the machine is active, it will add up to the compound effect, otherwise, its effect will reduce gradually
            double compoundEffect = value + (active ? 1 : -1) * effect;
            return (compoundEffect < 0) ? 0 : compoundEffect;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
