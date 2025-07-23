package project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String msg){
        super(msg);
    }
}
