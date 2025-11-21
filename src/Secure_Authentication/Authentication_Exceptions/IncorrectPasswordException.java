package src.Secure_Authentication.Authentication_Exceptions;

public class IncorrectPasswordException extends Exception {
    
    public static String conditions = "\n\tPassword entered is incorrect";

    public IncorrectPasswordException() {

        super(conditions);
    }
}