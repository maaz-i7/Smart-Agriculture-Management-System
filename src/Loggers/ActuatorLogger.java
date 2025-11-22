package src.Loggers;

import java.io.*;

/*
 * This class opens up the actuator_activities.txt file to log actuator activities
 */
public final class ActuatorLogger {

    private static PrintWriter writer;
    private static final Object lock = new Object();

    // Initialize ONCE at app start
    public static void init(String filePath) {

        try {
            File file = new File(filePath);
            File parent = file.getParentFile();

            if (!parent.exists()) parent.mkdirs();

            // Open file ONCE, overwrite old log
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file, false)), true);

        } 
        
        catch (Exception e) {throw new RuntimeException("Failed to initialize ActuatorLogger", e);}
    }

    // All actuators call this — writes to same file
    public static void log(String msg) {
        // It prevents two threads writing to the file at the same time, which can corrupt the log.
        synchronized (lock) {
            if (writer == null)
                throw new IllegalStateException("ActuatorLogger not initialized!");

            writer.println(msg);
        }
    }

    // Close at shutdown
    // It prevents two threads writing to the file at the same time, which can corrupt the log.
    public static void close() {
        if (writer != null) 
            writer.close();
    }
}

