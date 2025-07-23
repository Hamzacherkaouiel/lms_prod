package project.learning_managment_system.learning_managment_system_dev.course_managment.Dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Student_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enrollment_Dto {
    public int enrollemnt_id;
    public LocalDate enrollmentDate;
    public Course_Dto course;
    public Student_Dto student;

    @Override
    public String toString() {
        return "Enrollemnt{" +
                "id='" + enrollemnt_id + '\'' +
                ", date='" + enrollmentDate + '\'' +
                ", student='" + student + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
