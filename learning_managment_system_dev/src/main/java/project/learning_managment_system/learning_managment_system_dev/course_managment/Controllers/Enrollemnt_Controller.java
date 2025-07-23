package project.learning_managment_system.learning_managment_system_dev.course_managment.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Course_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Impl.Service_Enrollemnt;

import java.util.List;

@RestController
@RequestMapping("/enrollemnt")
public class Enrollemnt_Controller {
    public Service_Enrollemnt serviceEnrollemnt;

    public Enrollemnt_Controller(Service_Enrollemnt service){
        this.serviceEnrollemnt=service;
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('student','teacher')")
    public ResponseEntity<List<Course_Dto>> getEnrolledCourse(@PathVariable int id){
        return ResponseEntity.ok(this.serviceEnrollemnt.getEnrollmentCourses(id));
    }
    @GetMapping("/mail/{email}")
    @PreAuthorize("hasAnyRole('student','teacher')")
    public ResponseEntity<List<Course_Dto>> getEnrolledCourseByMail(@PathVariable String email){
        return ResponseEntity.ok(this.serviceEnrollemnt.getEnrollmentCoursesByMail(email));
    }
    @PostMapping("/{idcourse}/{idstudent}/single")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Void> createSingleEnrollment(@PathVariable int idcourse,@PathVariable int idstudent){
        this.serviceEnrollemnt.createSingleEnrollement(idstudent,idcourse);
        return ResponseEntity.status(201).build();
    }
    @PostMapping("/{idcourse}/multiple")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Void> createMultipleEnrollment(@PathVariable int idcourse,@RequestBody List<Integer> students){
        this.serviceEnrollemnt.createMultipleEnrollemnts(idcourse, students);
        return ResponseEntity.status(201).build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Void> deleteEnrollement(@PathVariable int id){
        this.serviceEnrollemnt.deleteEnrollemnt(id);
        return ResponseEntity.status(204).build();
    }
    @DeleteMapping("/student/{id}/course/{courseId}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Void> deleteEnrollementByStudentId(@PathVariable int id,@PathVariable int courseId){
        this.serviceEnrollemnt.deleteEnrollemntByStudentAndCourseId(id,courseId);
        return ResponseEntity.status(204).build();
    }

}
