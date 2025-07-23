package project.learning_managment_system.learning_managment_system_dev.user_managment.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserDTO;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.AuthService.Authentication_Service;

@RestController
@RequestMapping("/sign")
public class AuthController {
    @Autowired
    public Authentication_Service service;
    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<UserDTO> createStudent(@Valid @RequestBody UserCreation userCreation) {
        System.out.println(userCreation.getPassword());
        System.out.println(userCreation.getRole());
        System.out.println(userCreation.getFirstname());
        System.out.println(userCreation.getLastname());
        System.out.println(userCreation.getMail());
        UserDTO user=this.service.creaUser(userCreation);
        return ResponseEntity.status(201).body(user);
    }
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreation userCreation){
        UserDTO user=this.service.orchestrator(userCreation);
        return ResponseEntity.status(201).body(user);
    }
}
