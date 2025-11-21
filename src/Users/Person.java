package src.Users;
import src.Secure_Authentication.Encryptor;

public abstract class Person {

    /*      
     * name              - full name of the person
     * username          - unique username for each person
     * accountType       - the type of account, viz, {"Admin", "Agronomist", "Farmer"}
     * hashedPassword[]  - an array of numbers with password hashed in it
     * phoneNum          - phoneNumber of the person
     * salary            - salary of the person
     */
    public String name;
    public String username;
    public String accountType;
    private int[] hashedPassword;
    private long phoneNum;
    private double salary;
    
    public Person(String name, String username, String accountType, String password, long phoneNum, double salary) {

        this.name = name;
        this.username = username;
        this.accountType = accountType;
        this.hashedPassword = Encryptor.hashPassword(password);
        this.phoneNum = phoneNum;
        this.salary = salary;
    }
    
    /*
     * Checks if the user entered a correct password to login
     * Calls isPasswordMatch() of Encryptor to check match
     * If password matches, returns true
     * Otherwise, returns false
     */
    public boolean isPasswordMatch(String enteredPassword) {
        return Encryptor.isPasswordMatch(this, enteredPassword);
    }

    public int[] getHashedPassword() {
        return this.hashedPassword.clone();
    }

    public long getPhoneNum() {
        return this.phoneNum;
    }

    public double getSalary() {
        return this.salary;
    }

    public void updatePassword(String newPassword) {
        this.hashedPassword = Encryptor.hashPassword(newPassword);
    }

    public void updatePhoneNum(long newPhoneNum) {
        this.phoneNum = newPhoneNum;
    }
}