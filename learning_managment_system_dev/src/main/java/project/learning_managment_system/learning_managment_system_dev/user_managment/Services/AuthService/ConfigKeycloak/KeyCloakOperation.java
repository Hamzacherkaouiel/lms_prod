package project.learning_managment_system.learning_managment_system_dev.user_managment.Services.AuthService.ConfigKeycloak;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class KeyCloakOperation {
    @Autowired
    KeycloakConfig keycloakConfig;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.realm}")
    private String realm;
    public String createUserInKeycloak(String email, String password,String firstname,String lastname ){
        UserRepresentation userRepresentation=this.mapToUser(email
        ,firstname,lastname
        );
        userRepresentation.setCredentials(this.createCredentials(password,false));
        this.getKeycloakInstance().realm(realm).users()
                .create(userRepresentation);
        return this.getUserByUsername(userRepresentation.getUsername());

    }
    private String getUserByUsername(String username){
        UserRepresentation userRepresentation=this.getKeycloakInstance()
                .realm(realm).users().search(username).getFirst();
        return userRepresentation.getId();
    }
    public void assignRole(String role,String userId){
        String id=this.getClientUIID();
        RoleRepresentation roleRepresentation=this.getKeycloakInstance().realm(realm)
                .clients().get(id).roles().get(role).toRepresentation();
         this.getKeycloakInstance().realm(realm).
                users().get(userId).roles().clientLevel(id)
                .add(List.of(roleRepresentation));
    }
    private String getClientUIID(){
        return this.getKeycloakInstance().realm(realm)
                .clients().findByClientId(this.clientId)
                .getFirst().getId();
    }
    private UserRepresentation mapToUser(String email, String firstname,String lastname){
        UserRepresentation userRepresentation=new UserRepresentation();
        userRepresentation.setEmail(email);
        userRepresentation.setFirstName(firstname);
        userRepresentation.setUsername(email);
        userRepresentation.setLastName(lastname);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        return userRepresentation;
    }
    private List<CredentialRepresentation> createCredentials(String password,boolean temporary){
        List<CredentialRepresentation> creds=new ArrayList<>();
        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setTemporary(temporary);
        credentialRepresentation.setValue(password);
        creds.add(credentialRepresentation);
        return creds;
    }
    public Keycloak getKeycloakInstance(){
        return this.keycloakConfig.getInstance();
    }
}
