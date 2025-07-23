package project.learning_managment_system.learning_managment_system_dev.user_managment.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Teacher extends User_entity {
    public String speciality;
    @OneToMany(mappedBy = "teacher")
    public List<Course> course;
}
