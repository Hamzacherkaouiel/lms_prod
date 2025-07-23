package project.learning_managment_system.learning_managment_system_dev.user_managment.Services.AuthService.ConfigKeycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class KeycloakConfig {
    @Value("${keycloak.server-url}")
    private String keycloakBaseUrl;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;
    public Keycloak getInstance(){
        return KeycloakBuilder.builder()
                .clientId("admin-cli")
                .serverUrl(keycloakBaseUrl)
                .realm("master")
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }



}
