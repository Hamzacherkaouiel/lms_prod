package project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;

import java.util.List;
import java.util.Optional;

public interface Course_Repo extends JpaRepository<Course,Integer> {
     List<Course> findByTeacher_Id(int id );
}
