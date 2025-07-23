package project.learning_managment_system.learning_managment_system_dev.user_managment.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Teacher_Dto extends UserDTO{
    public String speciality;

}
