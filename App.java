import java.util.Scanner;

import src.Field_Meters.FarmSimulator;
import src.Secure_Authentication.Authentication;
import src.Users.Admin;
import src.Users.Agronomist;
import src.Users.Farmer;

public class App {

    public static void main(String[] args) throws Exception {

        System.out.println("-------------------SMART AGRICULTURE MANAGEMENT SYSTEM-------------------");

        System.out.println("\n--- Create Users Admin Account ---");
        Authentication.signUp();

        System.out.println("\n--- Create Field Admin Account ---");
        Authentication.signUp();

        System.out.println("\n--- Create Agronomist Account ---");
        Authentication.signUp();

        System.out.println("\n--- Create Farmer Account ---");
        Authentication.signUp();

        System.out.println("\nAll accounts created successfully!");

        // Sign in for Admin, Agronomist and Farmer
        System.out.print("\nUsers Admin Login:");
        Admin admin1 = (Admin) Authentication.signIn();
        Admin.UsersAdmin usersAdmin = admin1.new UsersAdmin();
        
        Scanner sc = new Scanner(System.in);

        // Ask for deleteUser
        String ans1 = "y";
        while (ans1.equals("y")) {

            System.out.print("Do you want to delete a user? (y/n): ");
            ans1 = sc.next().trim().toLowerCase();

            if (ans1.equals("y")) {
                usersAdmin.deleteUser();
            }
        }

        // Ask for updatePasswordOfUser
        String ans2 = "y";
        while (ans2.equals("y")) {

            System.out.print("Do you want to update a user's password? (y/n): ");
            ans2 = sc.next().trim().toLowerCase();

            if (ans2.equals("y")) {
                usersAdmin.updatePasswordOfUser();
            }
        }

        System.out.print("\nAgronomist Login:");
        Agronomist agronomist = (Agronomist) Authentication.signIn();
        System.out.print("\nFarmer Login:");
        Farmer farmer = (Farmer) Authentication.signIn();

        if (agronomist == null) {
            System.out.println("Agronomist is absent");
            return;
        }

        if (farmer == null) {
            System.out.println("Farmer is absent");
            return;
        }

        System.out.print("\nField Admin Login:");
        Admin admin2 = (Admin) Authentication.signIn();
        Admin.FieldAdmin fieldAdmin = admin2.new FieldAdmin();

        System.out.print("Enter Min Humidity Threshold: ");
        fieldAdmin.setMinHumidityThreshold(sc.nextDouble());

        System.out.print("Enter Max Humidity Threshold: ");
        fieldAdmin.setMaxHumidityThreshold(sc.nextDouble());

        System.out.print("Enter Min Light Intensity Threshold: ");
        fieldAdmin.setMinLightIntensityThreshold(sc.nextDouble());

        System.out.print("Enter Max Light Intensity Threshold: ");
        fieldAdmin.setMaxLightIntensityThreshold(sc.nextDouble());

        System.out.print("Enter Min Soil Moisture Threshold: ");
        fieldAdmin.setMinSoilMoistureThreshold(sc.nextDouble());

        System.out.print("Enter Max Soil Moisture Threshold: ");
        fieldAdmin.setMaxSoilMoistureThreshold(sc.nextDouble());

        System.out.print("Enter Min Temperature Threshold: ");
        fieldAdmin.setMinTemperatureThreshold(sc.nextDouble());

        System.out.print("Enter Max Temperature Threshold: ");
        fieldAdmin.setMaxTemperatureThreshold(sc.nextDouble());

        System.out.println("All thresholds updated successfully.");

        // Log all users to usersData.txt
        Authentication.logAllDetailsOfUsers();

        // Animate loading
        System.out.print("\nStarting Simulation");
        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            Thread.sleep(200);
        }

        FarmSimulator.start();
    }

}
