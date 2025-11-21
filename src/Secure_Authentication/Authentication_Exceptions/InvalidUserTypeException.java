package src.Secure_Authentication.Authentication_Exceptions;

public class InvalidUserTypeException extends Exception {
    
    public static String conditions = "\n\tInvalid user type";

    public InvalidUserTypeException() {
        super(conditions);
    }
}
