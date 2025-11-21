package src.Secure_Authentication;
import java.util.Arrays;

import src.Users.*;

public class Encryptor {
    
    /*
     * Simulates the hashing of a password String
     * Hashing algorithm:
     * First the password is reversed
     * Then, from 0th index of the reversed password String, the characters' (ASCII values + index_number) is hashed in hashedPassword[]
     */
    public static int[] hashPassword(String password) {
        
        int[] hashedPassword = new int[100];
        Arrays.fill(hashedPassword, -1);

        String reversedPassword = "";

        //password string reversed
        for(int i = password.length()-1; 0 <= i; i--)
            reversedPassword += Character.toString(password.charAt(i));

        //(ASCII value of password.charAt(i) + i) is hashed in password[]
        for(int i = 0; i < reversedPassword.length(); i++)
            hashedPassword[i] = ((int)reversedPassword.charAt(i)) + i;

        return hashedPassword;
    }
    
    /*
     * Checks if the user entered a correct password to login
     * dehashes the password and checks it with the enteredPasssword
     * If password matches, returns true
     * Otherwise, returns false
     */
    public static boolean isPasswordMatch(Person person, String enteredPassword) {

        int[] hashedPassword = person.getHashedPassword();

        int i;
        for(i = 0; i < 100; i++) {
            if(hashedPassword[i] == -1) {
                i--;
                break;
            } 

            hashedPassword[i] -= i;
        }

        String password = "";
        for(; 0 <= i; i--) {
            if(hashedPassword[i] == -1)
                break;

            password += Character.toString((char)(hashedPassword[i]));
        }

        return password.equals(enteredPassword);
    }
}