package project.learning_managment_system.learning_managment_system_dev.NotificationContext.KeycloakNotification;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserDTO;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.AuthService.ConfigKeycloak.KeycloakConfig;

import java.util.List;

@Service
public class KeycloakSync {
    @Value("${keycloak.realm}")
    private String realm;
    @Autowired
    public KeycloakConfig keycloakConfig;


    public void syncUser(UserCreation  userCreation){
        switch (userCreation.getOperation()){
            case "DELETE":
                this.deleteUser(userCreation.getMail());
                break;
            case "UPDATE_PROFILE":
                this.updateUser(userCreation);
                break;
            case "UPDATE_PASSWORD":
                this.resetPassword(userCreation.getPassword(), userCreation.getMail());
                break;
            default:
                throw new IllegalArgumentException("Unknown operation");
        }
    }

    private UserRepresentation getUserByMail(String mail){

        List<UserRepresentation> users = this.getKeycloakInstance()
                .realm(realm).users().
                search(mail,0,1);

        if (users == null || users.isEmpty()) {
            throw new UserNotFound("NO USER WITH THIS MAIL " + mail);
        }
        return users.getFirst();
    }
    private void updateUser(UserCreation userDTO){

            UserRepresentation user = this.getUserByMail(userDTO.getMail());
            updateAttributes(userDTO, user);
            this.getKeycloakInstance().realm(realm)
                    .users().get(user.getId())
                    .update(user);

    }


    private void updateAttributes(UserDTO userDTO,UserRepresentation user){
        user.setEmail(userDTO.getMail()!=null? userDTO.getMail() : user.getEmail());
        user.setFirstName(userDTO.getFirstname()!=null? userDTO.getFirstname() : user.getFirstName());
        user.setLastName(userDTO.getLastname()!=null? userDTO.getLastname() : user.getLastName());
    }
    private Keycloak getKeycloakInstance(){
        return this.keycloakConfig.getInstance();
    }
    private void resetPassword(String password,String mail){
        String id=this.getUserByMail(mail).getId();
        UserResource userResource=this.getKeycloakInstance().realm(realm).users()
                .get(id);
        CredentialRepresentation newPassword=new CredentialRepresentation();
        newPassword.setType(CredentialRepresentation.PASSWORD);
        newPassword.setValue(password);
        newPassword.setTemporary(false);
        userResource.resetPassword(newPassword);
        System.out.println("password reset");

    }
    private void deleteUser(String mail){
        UserRepresentation userRepresentation=this.getUserByMail(mail);
        this.getKeycloakInstance()
                .realm(realm).users().get(userRepresentation.getId())
                .remove();
    }

}
