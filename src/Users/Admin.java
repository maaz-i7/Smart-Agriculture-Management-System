package src.Users;

import java.io.Console;

import src.Secure_Authentication.Authentication;
import src.Secure_Authentication.Authentication_Exceptions.*;

public class Admin extends Person {

    /*
     * currentUsersCount - stores the count of current number of users
     */
    public static int currentUsersCount = 0;

    /*
     * Overloaded Constructors
     */
    public Admin(String name, String username, String password, long phoneNum) {

        super(name, username, "Admin", password, phoneNum, 100000);
    }

    public Admin(String name, String username, String password, long phoneNum, double salary) {

        super(name, username, "Admin", password, phoneNum, salary);
    }

    /*
     * Nested inner class
     * UsersAdmin - admin of all users, takes actions related to users
     */
    public class UsersAdmin implements Authentication {

        /*
         * Updates the password of a user.
         * First asks the username of the user
         */
        public void updatePasswordOfUser() throws Exception {

            Console consoleInput = System.console();

            Person person = Authentication.signIn();

            System.out.print("\nCreate a new password: ");
            System.out.println(WeakPasswordException.conditions);
            char[] passChars = consoleInput.readPassword("\nEnter new password: ");
            String newPassword = String.valueOf(passChars);
            Authentication.checkPasswordStrength(newPassword);
            person.updatePassword(newPassword);
        }

        /*
         * Deletes a user from the database
         */
        public void deleteUser() throws Exception {

            System.out.println("\nSign in with the user to be deleted:");

            Person person = Authentication.signIn();

            int i = 0;
            for (; i < currentUsersCount; i++) {
                if (users[i].username.equals(person.username))
                    break;
            }

            for (; i < currentUsersCount - 1; i++) {
                Person tempUser = users[i];
                users[i] = users[i + 1];
                users[i + 1] = tempUser;
            }

            users[i] = null;
            currentUsersCount--;
        }
    }

    /*
     * Nested inner class
     * FieldAdmin - admin of the farm, takes farm related actions
     */
    public class FieldAdmin {

        public static double MIN_HUMIDITY_THRESHOLD = 0;
        public static double MAX_HUMIDITY_THRESHOLD = 10;
        public static double MIN_SOIL_MOISTURE_THRESHOLD = 80;
        public static double MAX_SOIL_MOISTURE_THRESHOLD = 90;
        public static double MIN_LIGHT_INTENSITY_THRESHOLD = 5000;
        public static double MAX_LIGHT_INTENSITY_THRESHOLD = 6000;
        public static double MIN_TEMPERATURE_THRESHOLD = 20;
        public static double MAX_TEMPERATURE_THRESHOLD = 30;
        public static double totalCost = 0;
        
        public void setMinHumidityThreshold(double value) {
            MIN_HUMIDITY_THRESHOLD = value;
        }
        
        public void setMinLightIntensityThreshold(double value) {
            MIN_LIGHT_INTENSITY_THRESHOLD = value;
        }

        public void setMinSoilMoistureThreshold(double value) {
            MIN_SOIL_MOISTURE_THRESHOLD = value;
        }

        public void setMinTemperatureThreshold(double value) {
            MIN_TEMPERATURE_THRESHOLD = value;
        }

        
        public void setMaxHumidityThreshold(double value) {
            MAX_HUMIDITY_THRESHOLD = value;
        }
        
        public void setMaxLightIntensityThreshold(double value) {
            MAX_LIGHT_INTENSITY_THRESHOLD = value;
        }

        public void setMaxSoilMoistureThreshold(double value) {
            MAX_SOIL_MOISTURE_THRESHOLD = value;
        }
    
        public void setMaxTemperatureThreshold(double value) {
            MAX_TEMPERATURE_THRESHOLD = value;
        }
    }
}