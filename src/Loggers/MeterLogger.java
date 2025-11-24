package src.Loggers;

import java.io.*;

/*
 * This class logs the sensors data sensors_data.txt 
 * This class logs the sensors data to farm_data.csv
 */
public final class MeterLogger {

    private static PrintWriter txtWriter;
    private static PrintWriter csvWriter;
    public static String timeStamp = null;
    public static double humidity = -1000;
    public static double lightIntensity = -1000;
    public static double soilMoisture = -1000;
    public static double temperature = -1000;
    static Thread meterLoggerThread;

    // Initialize ONCE at app start
    public static void init(String txtFilePath, String csvFilePath) {

        try {

            File txtFile = new File(txtFilePath);
            File parentTxt = txtFile.getParentFile();

            if (!parentTxt.exists())
                parentTxt.mkdirs();

            File csvFile = new File(csvFilePath);
            File parentCsv = csvFile.getParentFile();

            if (!parentCsv.exists())
                parentCsv.mkdirs();

            // Open file ONCE, overwrite old log
            txtWriter = new PrintWriter(new BufferedWriter(new FileWriter(txtFile, false)), true);
            csvWriter = new PrintWriter(new BufferedWriter(new FileWriter(csvFile, false)), true);

            // log heading to txt file
            txtWriter.println("Time  | Temperature  | Humidity   | Light   | SoilMoisture");
            txtWriter.flush();

            // log heading to csv file
            csvWriter.println("Time,Temperature,Humidity,Light,SoilMoisture");
            csvWriter.flush();
        }

        catch (Exception e) {
            throw new RuntimeException("Failed to initialize ActuatorLogger", e);
        }

        meterLoggerThread = new Thread(() -> {  
            startReceivingData();    
        });
        meterLoggerThread.start();
    }

    public static void startReceivingData() {

        while(!Thread.currentThread().isInterrupted()) {
            while(humidity == -1000 || lightIntensity == -1000 || soilMoisture == -1000 || temperature == -1000 || timeStamp == null) {

                try {Thread.sleep(100);} 
                catch (InterruptedException e) {}
            }

            logData();
        }
    }

    // All meters log their data to Meter Logger here
    public static void logHumidity(double value) {
        humidity = value;
    }

    public static void logLightIntensity(double value) {
        lightIntensity = value;
    }

    public static void logSoilMoisture(double value) {
        soilMoisture = value;
    }

    public static void logTemperature(double value) {
        temperature = value;
    }

    public static void logTime(String time) {
        timeStamp = time;
    }

    public static void logData() {

        // Write all values in txt file
        String rowTxt = String.format(
                "%-5s | %-12.2f | %-10.2f | %-7.0f | %-12.2f",
                timeStamp, temperature, humidity, lightIntensity, soilMoisture);

        txtWriter.println(rowTxt);
        txtWriter.flush();

        // Write all values in csv file
        String rowCsv = String.format("%s,%.2f,%.2f,%.0f,%.2f",
                timeStamp, temperature, humidity, lightIntensity, soilMoisture);

        csvWriter.println(rowCsv);
        csvWriter.flush();

        // set values to -1000, meter waits for new set of data to arrive
        temperature = -1000;
        humidity = -1000;
        lightIntensity = -1000;
        soilMoisture = -1000;
        timeStamp = null;
    }

    // Close at shutdown
    // It prevents two threads writing to the file at the same time, which can
    // corrupt the log.
    public static void close() {

        meterLoggerThread.interrupt();
        
        if (txtWriter != null)
            txtWriter.close();
    

        if (csvWriter != null)
            csvWriter.close();
    }
}
