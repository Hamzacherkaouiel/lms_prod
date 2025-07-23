package project.learning_managment_system.learning_managment_system_dev.user_managment.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Enrollements;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends User_entity {
    @ColumnDefault("0")
    public Integer progress;
    @OneToMany(mappedBy = "student")
    public List<Enrollements> enrollementsList;

}
