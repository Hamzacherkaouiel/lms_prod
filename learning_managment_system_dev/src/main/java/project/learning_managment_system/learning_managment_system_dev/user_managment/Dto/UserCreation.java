package project.learning_managment_system.learning_managment_system_dev.user_managment.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserCreation extends UserDTO{
    @NotBlank(message = "you must provide a password")
    private String password;
    private String speciality;
    private String role="student";
    private String operation;
}
