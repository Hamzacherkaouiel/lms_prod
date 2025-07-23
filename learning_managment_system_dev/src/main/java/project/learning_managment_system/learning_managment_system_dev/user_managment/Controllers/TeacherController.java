package project.learning_managment_system.learning_managment_system_dev.user_managment.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Student_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Teacher_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.KafkaConfig.Producer;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceImpl.ServiceStudent;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceImpl.ServiceTeacher;

@RestController
@RequestMapping("/Teacher")
public class TeacherController extends ManagmentController<Teacher_Dto> {
    @Autowired
    public Producer producer;
    public TeacherController(ServiceTeacher serviceTeacher){
        this.serviceUser=serviceTeacher;
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    public ResponseEntity<Teacher_Dto> updateUser(@RequestBody Teacher_Dto user, int id){
        return super.updateUser(user,id);
    }
    @Override
    @GetMapping("/profile")
    @PreAuthorize("hasRole('teacher')")
        public ResponseEntity<Teacher_Dto> getProfile(@AuthenticationPrincipal Jwt jwt){
        return super.getProfile(jwt);
    }
    @Override
    @PutMapping("/password")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal Jwt jwt,@RequestBody UserCreation user){
        return super.updatePassword(jwt,user);
    }
    @Override
    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity deleteProfile(@AuthenticationPrincipal Jwt jwt){
        return super.deleteProfile(jwt);
    }

}
