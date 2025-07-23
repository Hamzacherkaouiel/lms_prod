package project.learning_managment_system.learning_managment_system_dev.course_managment.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Course_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Impl.Service_Course;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class Course_Controller {
    public Service_Course serviceCourse;
    public Course_Controller(Service_Course service_course){
        this.serviceCourse=service_course;
    }
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<List<Course_Dto>> getCourses(){
        return ResponseEntity.ok(this.serviceCourse.getAllData());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<Course_Dto> getSingleCourse(@PathVariable int id){
        return ResponseEntity.ok(this.serviceCourse.getSingleData(id));
    }
    @GetMapping("/teacher/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<List<Course_Dto>> getCourseByOwnerId(@PathVariable int id ){
        return ResponseEntity.ok(this.serviceCourse.getCourseByOwnerId(id));
    }
    @GetMapping("/teacher/mail/{email}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<List<Course_Dto>> getCourseByOwnerMail(@PathVariable String email ){
        return ResponseEntity.ok(this.serviceCourse.getCourseByOwnerMail(email));
    }
    @PostMapping("/teacher/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Course_Dto> createCourse(@RequestBody Course_Dto courseDto,@PathVariable int id){
        return ResponseEntity.status(201)
                .body(this.serviceCourse.createData(courseDto,id));
    }
    @PostMapping("/teacher/mail/{email}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Course_Dto> createCourseByMail(@RequestBody Course_Dto courseDto,@PathVariable String email){
        return ResponseEntity.status(201)
                .body(this.serviceCourse.createCourseByMail(courseDto,email));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Course_Dto> updateCourse(@RequestBody Course_Dto courseDto,@PathVariable int id){
        return ResponseEntity.ok(this.serviceCourse.updateData(courseDto,id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<String> deleteCourse(@PathVariable int id){
        this.serviceCourse.deleteData(id);
        return ResponseEntity.status(204).body("COURSE DELETED");
    }
}
