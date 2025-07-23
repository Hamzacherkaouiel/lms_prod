package project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException;

public class InvalidUser extends RuntimeException{
    public InvalidUser(String msg){
        super(msg);
    }
}
