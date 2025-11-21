package src.Secure_Authentication.Authentication_Exceptions;

public class InvalidUsernameException extends Exception {
    
    public static String conditions = "\n\tUsername must contain only letters a-z or A-Z\n\tUsername must contain only digits 0-9\n\tUsername must contain only symbols _ or .\n\tUsername must not start with .";

    public InvalidUsernameException() {

        super(conditions);
    }
}