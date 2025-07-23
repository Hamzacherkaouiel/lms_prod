package project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Enrollements;

import java.util.List;
import java.util.Optional;

public interface Enrollemnt_Repo extends JpaRepository<Enrollements,Integer> {
    List<Enrollements> findByStudent_Id(int id);
    List<Enrollements> findByStudentIdAndCourseTeacherId(Long studentId, Long teacherId);
    boolean existsByStudent_IdAndCourse_Id(int idstudent,int idcourse);
    Optional<Enrollements> findByStudent_IdAndCourse_Id(int student_id, int course_id);
    void deleteByStudent_Id(int id);

}
