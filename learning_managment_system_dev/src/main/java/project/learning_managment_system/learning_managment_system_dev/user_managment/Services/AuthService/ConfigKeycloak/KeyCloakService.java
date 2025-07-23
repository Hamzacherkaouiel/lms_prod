package project.learning_managment_system.learning_managment_system_dev.user_managment.Services.AuthService.ConfigKeycloak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;

@Service
public class KeyCloakService {
    @Autowired
    public KeyCloakOperation keyCloakOperation;

    public void createUser(UserCreation user)  {
        String user_id=this.keyCloakOperation.createUserInKeycloak(user.getMail(),user.getPassword(),user.getFirstname(),user.getLastname());
        this.keyCloakOperation.assignRole(user.getRole(),user_id);
    }
}
