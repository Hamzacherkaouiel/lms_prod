package project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Lessons;

import java.util.List;

public interface Lessons_Repo extends JpaRepository<Lessons,Integer> {
    List<Lessons> findByModule_Id(int id);


}
