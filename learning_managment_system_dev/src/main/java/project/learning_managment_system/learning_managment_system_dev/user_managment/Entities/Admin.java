package project.learning_managment_system.learning_managment_system_dev.user_managment.Entities;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Admin  extends User_entity{

}
