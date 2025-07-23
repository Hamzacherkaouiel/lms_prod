package project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Modules;

import java.util.List;

public interface Modules_Repo extends JpaRepository<Modules,Integer> {
    List<Modules> findByCourse_Id(int id);

}