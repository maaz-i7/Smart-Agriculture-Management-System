package src.Users;

import src.Field_Meters.Meters.Hygrometer;
import src.Field_Meters.Meters.Photometer;
import src.Field_Meters.Meters.SoilMoistureMeter;
import src.Field_Meters.Meters.Thermometer;
import src.Users.Admin.FieldAdmin;

public class Agronomist extends Person {
    
    static double humidity;
    static double lightIntensity;
    static double soilMoisture;
    static double temperature;
    /*
     * Overloaded Constructors
     */
    public Agronomist(String name, String username, String password, long phoneNum) {

        super(name, username, "Agronomist", password, phoneNum, 80000);
    }

    public Agronomist(String name, String username, String password, long phoneNum, double salary) {

        super(name, username, "Agronomist", password, phoneNum, salary);
    }

    // Agronomist reads and analyzes the data, and based on analysis, instructs the farmer to take actions
    public static void readAndAnalyzeHumidityData(String timeStamp, Hygrometer hygrometer) {
         
        humidity = hygrometer.data;
    }

    public static void readAndAnalyzeTemperatureData(String timeStamp, Thermometer thermometer) {
        
        temperature = thermometer.data;
    }

    public static void readAndAnalyzeLightIntensityData(String timeStamp, Photometer photometer) {
        
        lightIntensity = photometer.data;

        if(FieldAdmin.MIN_LIGHT_INTENSITY_THRESHOLD <= lightIntensity && lightIntensity <= FieldAdmin.MAX_LIGHT_INTENSITY_THRESHOLD) {
            
            Farmer.turnSodiumLampOff(timeStamp);
            Farmer.removeShade(timeStamp);
        }

        else if(lightIntensity < FieldAdmin.MIN_LIGHT_INTENSITY_THRESHOLD) {
            
            Farmer.turnSodiumLampOn(timeStamp);
            Farmer.removeShade(timeStamp);
        }

        else if(FieldAdmin.MAX_LIGHT_INTENSITY_THRESHOLD < lightIntensity) {
            
            Farmer.coverWithShade(timeStamp);
            Farmer.turnSodiumLampOff(timeStamp);
        }
    }

    public static void readAndAnalyzeSoilMoistureData(String timeStamp, SoilMoistureMeter soilMoistureMeter) {
        
        soilMoisture = soilMoistureMeter.data;

        if(FieldAdmin.MIN_SOIL_MOISTURE_THRESHOLD <= soilMoisture && soilMoisture <= FieldAdmin.MAX_SOIL_MOISTURE_THRESHOLD) {
            
            Farmer.stopIrrigation(timeStamp);
            Farmer.closeDrainPipe(timeStamp);
        }

        else if(soilMoisture < FieldAdmin.MIN_SOIL_MOISTURE_THRESHOLD) {
            
            Farmer.startIrrigation(timeStamp);
            Farmer.closeDrainPipe(timeStamp);
        }
        
        else if(FieldAdmin.MAX_SOIL_MOISTURE_THRESHOLD < soilMoisture) {
            
            Farmer.stopIrrigation(timeStamp);
            Farmer.openDrainPipe(timeStamp);
        }
    }  
}