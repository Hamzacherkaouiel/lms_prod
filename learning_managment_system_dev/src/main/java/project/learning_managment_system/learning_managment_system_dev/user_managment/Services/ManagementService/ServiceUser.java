package project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService;

import org.springframework.security.oauth2.jwt.Jwt;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;

import java.util.List;

public interface ServiceUser<T> {
    public List<T> getUsers();
    public T getSingleUser(int id);
    public T updateUser(T user,int id);
    public void deleteUser(int id);
    public T getMyProfile(Jwt token);
    public UserCreation mapTo(T user);
    public void deleteMyProfile(Jwt token);
    public void updatePassword(UserCreation userCreation,Jwt jwt);
}
