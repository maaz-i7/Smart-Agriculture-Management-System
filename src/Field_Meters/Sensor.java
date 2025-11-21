package src.Field_Meters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import src.Field_Meters.Meters.Hygrometer;
import src.Field_Meters.Meters.Photometer;
import src.Field_Meters.Meters.SoilMoistureMeter;
import src.Field_Meters.Meters.Thermometer;

// interface that Meters will extend from
public interface Sensor {

    String manufacturer = "Mitsubishi";
    int lifeSpan = 365; //in days

    void senseData(String timeStamp, double value);
    void sendData(String timeStamp, double value); 

    // initiates the sensors to start sensing data from the field
    // also starts the field simulation
    public static void startSensingData() {

        Hygrometer hygrometer = new Hygrometer();
        Photometer photometer = new Photometer();
        SoilMoistureMeter soilMoistureMeter = new SoilMoistureMeter();
        Thermometer thermometer = new Thermometer();

        // Farm Simulator starts producing data in a thread
        Thread farmSimThread = new Thread(() -> {
            try {FarmSimulator.main(null);} 
            catch (InterruptedException e) {}
        });
        farmSimThread.start();

        String filePath = "src\\data_logs\\sensors_data\\sensors_data.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            //Wait for Farm Simulator to start producing data
            try {Thread.sleep(2500);} 
            catch (InterruptedException e) {}

            //Skip the heading
            String line = br.readLine();

            // Keep reading data forever
            while (true) {

                line = br.readLine();

                if (line != null) {

                    String[] dataValues = java.util.Arrays.stream(line.split("\\|")).map(String::trim).toArray(String[]::new);

                    thermometer.senseData(dataValues[0], Double.parseDouble(dataValues[1]));
                    hygrometer.senseData(dataValues[0], Double.parseDouble(dataValues[2]));
                    photometer.senseData(dataValues[0], Double.parseDouble(dataValues[3]));
                    soilMoistureMeter.senseData(dataValues[0], Double.parseDouble(dataValues[4]));
                }

                // If reached end of line, wait for new data to arrive
                else {

                    try {Thread.sleep(1000);} 
                    catch (InterruptedException e) {break;}
                }
            }
        } 
        
        catch (IOException e) {
            System.out.println("sensors_data.txt is missing");
        }
    }
}