package src.Secure_Authentication.Authentication_Exceptions;

public class WeakPasswordException extends Exception {
    
    public static String conditions = "\n\tPassword must be atleast 8 characters in length\n\tPassword must contain atleast one letter a-z\n\tPassword must contain atleast one letter from A-Z\n\tPassword must contain atleast one number from 0-9\n\tPassword must contain ateast one special character";

    public WeakPasswordException() {

        super(conditions);
    }
}
