package src.Secure_Authentication.Authentication_Exceptions;

public class UserDoesNotExistException extends Exception {
    
    public static String conditions = "\n\tNo such user exists";

    public UserDoesNotExistException() {

        super(conditions);
    }
}

