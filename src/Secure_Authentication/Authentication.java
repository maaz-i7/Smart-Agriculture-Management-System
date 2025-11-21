package src.Secure_Authentication;

import java.util.*;
import java.io.*;
import src.Secure_Authentication.Authentication_Exceptions.*;
import src.Users.*;

public interface Authentication {

    /*
     * users - stores the list of all users, initialized to a size of 100
     * usersTypes - stores the three types of users, viz "Admin", "Agronomist",
     * "Farmer"
     */
    public static Person[] users = new Person[100];
    public static String[] usersTypes = { "Admin", "Agronomist", "Farmer" };

    /*
     * checks if the username already exists in the database
     * If true, returns the user
     * else, returns null
     */
    static Person doesUsernameExist(String username) {

        if (username == null)
            return null;

        for (int i = 0; i < Admin.currentUsersCount; i++) {
            if (users[i].username.equals(username))
                return users[i];
        }

        return null;
    }

    /*
     * Checks the validity of username as per rules:
     * - Username must contain only letters a-z or A-Z
     * - Username must contain only digits 0-9
     * - Username must contain only symbols _ or .
     * - Username must not start with .
     * 
     * If username is valid, returns
     * Otherwise, throws InvalidUsernameException()
     */
    static void checkValidUsername(String username) throws Exception {

        if (username == null || username.charAt(0) == '.')
            throw new InvalidUsernameException();

        for (int i = 0; i < username.length(); i++) {

            if (Character.isLetterOrDigit(username.charAt(i)))
                continue;

            if (username.charAt(i) == '.' || username.charAt(i) == '_')
                continue;

            throw new InvalidUsernameException();
        }
    }

    /*
     * Checks if name of user entered is valid:
     * 
     * If name has only letters a-z and A-Z are present, returns
     * Otherwise throws InvalidNameException()
     */
    static void checkValidName(String name) throws Exception {

        if (name == null)
            throw new InvalidNameException();

        for (int i = 0; i < name.length(); i++) {
            if (Character.isLetter(name.charAt(i)) || name.charAt(i) == ' ')
                continue;

            throw new InvalidNameException();
        }
    }

    /*
     * Checks password strength based on rules:
     * - Password must be atleast 8 characters in length
     * - Password must contain atleast one letter a-z
     * - Password must contain atleast one letter from A-Z
     * - Password must contain atleast one number from 0-9
     * - Password must contain ateast one special character
     * 
     * If above all conditions are met, password is strong and returns
     * Otherwise, throws WeakPasswordException()
     */
    static void checkPasswordStrength(String password) throws Exception {

        if (password == null || password.length() < 8)
            throw new WeakPasswordException();

        boolean lowerCasePresent = false;
        boolean upperCasePresent = false;
        boolean numberPresent = false;
        boolean specialCharPresent = false;

        for (int i = 0; i < password.length(); i++) {

            if (Character.isLowerCase(password.charAt(i)))
                lowerCasePresent = true;

            else if (Character.isUpperCase(password.charAt(i)))
                upperCasePresent = true;

            else if (Character.isDigit(password.charAt(i)))
                numberPresent = true;

            else
                specialCharPresent = true;
        }

        if (!lowerCasePresent || !upperCasePresent || !numberPresent || !specialCharPresent)
            throw new WeakPasswordException();
    }

    /*
     * Creates a new user in database
     */
    public static void signUp() throws Exception {

        Scanner input = new Scanner(System.in);
        Console consoleInput = System.console();

        System.out.println("\nEnter the type of user to register");
        System.out.println("\t0 - " + usersTypes[0]);
        System.out.println("\t1 - " + usersTypes[1]);
        System.out.println("\t2 - " + usersTypes[2]);
        int employeeType = input.nextInt();

        if (employeeType > 2)
            throw new InvalidUserTypeException();

        String name = consoleInput.readLine("\nEnter your full name: ");
        checkValidName(name);

        System.out.print("\nCreate a username: ");
        System.out.println(InvalidUsernameException.conditions);
        String username = input.next();
        checkValidUsername(username);

        while(doesUsernameExist(username) != null) {

            System.out.println("\nUsername already taken!");
            username = input.next();
        }

        System.out.print("\nCreate a strong password");
        System.out.println(WeakPasswordException.conditions);
        char[] passChars = consoleInput.readPassword("\nCreate password here: ");
        String password = String.valueOf(passChars);
        checkPasswordStrength(password);

        System.out.print("\nEnter your phone number: ");
        long phoneNum = input.nextLong();

        Person person;

        if (employeeType == 0)
            person = new Admin(name, username, password, phoneNum);

        else if (employeeType == 1)
            person = new Agronomist(name, username, password, phoneNum);

        else
            person = new Farmer(name, username, password, phoneNum);

        users[Admin.currentUsersCount++] = person;
    }

    /*
     * Checks and returns an already existing user
     */
    public static Person signIn() throws Exception {

        Scanner input = new Scanner(System.in);
        Console consoleInput = System.console();

        System.out.print("\nEnter username: ");
        String username = input.next();

        Person person = doesUsernameExist(username);

        if (person == null)
            throw new UserDoesNotExistException();

        char[] passChars = consoleInput.readPassword("\nEnter password: ");
        String password = String.valueOf(passChars);

        if (!person.isPasswordMatch(password))
            throw new IncorrectPasswordException();

        return person;
    }

    /*
     * Displays all the users in the database
     */
    static void logAllDetailsOfUsers() throws Exception {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\data_logs\\users_data\\users_data.txt", false))) {
            
            writer.write("Data of users:\n");

            int count = 0;

            for (Person person : users) {

                if (person == null)
                    break;

                writer.write("\n\nUser#" + count++);
                writer.write("\nUser type       : " + person.accountType);
                writer.write("\nUsername        : " + person.username);
                writer.write("\nFull name       : " + person.name);
                writer.write("\nHashed Password : ");

                for (int i : person.getHashedPassword()) {
                    if (i == -1)
                        break;

                    writer.write(i);
                }

                writer.write("\nPhone Number    : " + person.getPhoneNum());
                writer.write("\nSalary          : ₹" + person.getSalary());
            }
        }

        catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    // To be implemented by UsersAdmin class
    void updatePasswordOfUser() throws Exception;

    void deleteUser() throws Exception;
}