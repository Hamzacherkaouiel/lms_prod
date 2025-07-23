package project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;

import java.util.List;
import java.util.Optional;

public interface Student_Repo extends JpaRepository<Student,Integer> {
    Optional<Student> findByMail(String email);
    void deleteByMail(String mail);
    @Query("SELECT s FROM Student s WHERE s.id NOT IN ( SELECT e.student.id FROM Enrollements e WHERE e.course.id = :courseId)")
    List<Student> findStudentsNotEnrolledInCourse(@Param("courseId") int courseId);
    @Query("SELECT s FROM Student s WHERE s.id  IN ( SELECT e.student.id FROM Enrollements e WHERE e.course.id = :courseId)")
    List<Student> findStudentsEnrolledInCourse(@Param("courseId") int courseId);

}
