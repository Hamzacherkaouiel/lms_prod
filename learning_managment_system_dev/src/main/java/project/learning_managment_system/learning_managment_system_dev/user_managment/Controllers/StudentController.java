package project.learning_managment_system.learning_managment_system_dev.user_managment.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Student_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.KafkaConfig.Producer;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceImpl.ServiceStudent;

import java.util.List;

@RestController
@RequestMapping("/Student")
public class StudentController extends ManagmentController<Student_Dto>{
    public ServiceStudent student;

    public StudentController(ServiceStudent serviceStudent){
        this.student=serviceStudent;
        this.serviceUser=serviceStudent;
    }
    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<Student_Dto> updateUser(@RequestBody Student_Dto user,@PathVariable int id){
        return super.updateUser(user,id);
    }
    @Override
    @GetMapping("/profile")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<Student_Dto> getProfile(@AuthenticationPrincipal Jwt jwt){
        return super.getProfile(jwt);
    }
    @Override
    @PutMapping("/password")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal Jwt jwt,@RequestBody UserCreation user){
        return super.updatePassword(jwt,user);

    }
    @Override
    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity deleteProfile(@AuthenticationPrincipal Jwt jwt){
        return super.deleteProfile(jwt);
    }

    @GetMapping("/not-enrolled/{courseId}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<List<Student_Dto>> getNotEnrolledStudents(@PathVariable int courseId ){
        return ResponseEntity.ok(this.student.getNotEnrolledStudents(courseId));
    }
    @GetMapping("/enrolled/{courseId}")
    @PreAuthorize("hasAnyRole('teacher','student')")
    public ResponseEntity<List<Student_Dto>> getEnrolledStudents(@PathVariable int courseId ){
        return ResponseEntity.ok(this.student.getEnrolledStudents(courseId));
    }

}
