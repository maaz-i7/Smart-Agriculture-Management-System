package src.Secure_Authentication.Authentication_Exceptions;

public class InvalidNameException extends Exception {
    
    public static String conditions = "\n\tName must contain only a-z and A-Z letters";

    public InvalidNameException() {

        super(conditions);
    }
}